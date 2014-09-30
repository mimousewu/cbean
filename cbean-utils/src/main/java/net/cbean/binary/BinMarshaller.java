/**
 * @author WuTao
 *
 * 2006-7-31 17:45:06
 */
package net.cbean.binary;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.cbean.exceptions.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 对象转化成二进制报文的解析器，调用marshal(V v)方法，获得一个byte[]。<br>
 * 此类是线程不安全的，有必要为每个处理过程new出来一个BinMarshaller对象
 */
public class BinMarshaller<V> extends BinAbstractFactory{
	private static Log log = LogFactory.getLog(BinMarshaller.class);
	
	private static int DEFAULT_BUFFER_LENGTH = 1024*32;
	
	private int bufferLength = DEFAULT_BUFFER_LENGTH;
	
	private Map<String,byte[]> attributes = new HashMap<String,byte[]>();
	private Map<String,Object> attributeValues = new HashMap<String,Object>();
	private Map<Integer,String> positionMeans = new HashMap<Integer,String>();
	
	public BinMarshaller(BinMapping mapping,ParserHandle handle){
		this.mapping = mapping;
		this.parserHandle = handle;
	}
	
	/**
	 * @param bufferLength The bufferLength to set.
	 */
	public void setBufferLength(int bufferLength) {
		this.bufferLength = bufferLength;
	}
	public byte[] marshal(V v) throws ParseException  {
		data = ByteBuffer.allocate(bufferLength);

		Structure structure = mapping.getStructureList().get(0); //从第一个Structrue开始
		
		try{
			workRound(v,structure);
		}catch(Exception e){
			throw new ParseException("Can't marshal object to byte array!",e);
		}
		
		data.flip();
		byte[] re = new byte[data.limit()];
		data.get(re);
		return re;
	}
	
	@SuppressWarnings("unchecked")
	private void workRound(Object object, final Structure structure) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
		int startPostion = data.position();
		
		for(Property prop : structure.getProperties()){
			Object valueObj = (!prop.isIgnore() && object!=null) ? 
				getProperty(object, prop.getName())
				: null;
			
			if(valueObj!=null)
				attributeValues.put(structure.getId()+'.'+prop.getName(), valueObj);
			
			Structure child = this.mapping.getStructureMap().get(prop.getType());
			if(child!=null){
				//处理子结构体的情况
				String collectionTag = child.getCollection();
				if(collectionTag!=null && "COLLECTION".equals(collectionTag.toUpperCase())){
					for(Object it : (Collection)valueObj){
						workRound(new MapItem(null,it),child);
					}
				}else if(collectionTag!=null && "MAP".equals(collectionTag.toUpperCase())){
					for(Map.Entry<Object,Object> entry : ((Map<Object,Object>)valueObj).entrySet()){
						workRound(new MapItem(entry),child);
					}
				}else{
					workRound(valueObj,child);
				}
			}else{
				buildPropetyBytes(structure, prop, valueObj);
			}
		}
		
		int endPosition = data.position();
		dealWithLength(startPostion,endPosition);
	}
	
	/**
	 * 处理 \@length的问题
	 * @param startPostion
	 * @param endPostion
	 */
	private void dealWithLength(int startPostion, int endPosition) {
		
		for(Iterator<Map.Entry<Integer, String>> it = positionMeans.entrySet().iterator();it.hasNext();){
			Map.Entry<Integer, String> entry = it.next();
			if(entry.getKey() >= startPostion && entry.getKey() < endPosition){
				int byteCount = attributes.get(entry.getValue()).length;
				int length = endPosition - entry.getKey() - byteCount;
				byte[] len = DataUtils.encodeInt(length, byteCount);
				data.position(entry.getKey());
				data.put(len);
				data.position(endPosition);
				it.remove();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buildPropetyBytes(final Structure structure, Property prop, Object valueObj) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		long propLen = getLength(structure,prop.getLength());
		byte[] value = null;
		
		if(prop.getValue()==null || "".equals(prop.getValue())){
			//没有设置value值的Property
			BinParser parser = getParser(structure, prop.getType());
			if(propLen>0){
				value = parser.encode(valueObj,(int)propLen);
			}else{
				value = parser.encode(valueObj);
			}
		}else{
			if("@SIZE".equalsIgnoreCase(prop.getValue())){ //表示取此属性下面所有属性值的长度
				positionMeans.put(data.position(), structure.getId()+'.'+prop.getName());
				value = new byte[(int)propLen];
			}else if("IGNORE".equalsIgnoreCase(prop.getValue())){
				value = new byte[0];
			}else if(prop.getValue().matches(METHOD_REGEX)){
				//执行静态方法，此静态方法返回值一定是byte[]
				Object re = invokeStaticMethod(structure,prop.getValue());
				if(re instanceof byte[]){
					value = (byte[])re;
				}else{
					throw new IllegalStateException("propty value can only invoke method returns byte[]! propty: "+structure.getId()+"."+prop.getName());
				}
			}else if(prop.getValue().matches(PROPERTY_REGEX)){  //使用已经存在的属性，支持+-数值的公式
				value = attributes.get(getArgKey(structure.getId(),prop.getValue()));
			}else{
				//其他情况，当成十六进制字符串处理
				value = DataUtils.hexToByte(prop.getValue().replaceAll(" ", ""));
			}
		}
		
		if(propLen>0 && value.length!=propLen){
			throw new IllegalStateException("propty value length not equals configuration! expect "+propLen+" but was "+value.length+". propty: "+structure.getId()+"."+prop.getName());
		}

		attributes.put(structure.getId()+'.'+prop.getName(), value);
		data.put(value);
	}
	
	/**
	 * 从对象中取得属性
	 */
	@Override
	Object getArgValue(String structureId, String argStr) {
		argStr = argStr.substring(1);
		Object argObj = this.attributeValues.get(getArgKey(structureId, argStr));
		if(argObj==null){
			log.error("can't get argument of "+structureId+'.'+argStr);
		}
		return argObj;
	}

	private String getArgKey(String structureId, String argStr) {
		return (argStr.indexOf('.')>0) ? argStr : structureId+'.'+argStr;
	}
	
	/**
	 * 反射机制得到对象的属性值
	 * @param object
	 * @param propertyName
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object getProperty(Object object, String propertyName) throws IllegalAccessException, InvocationTargetException{
		Object value = null;
		if(object==null){
			throw new IllegalArgumentException(propertyName+" value is null.");
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
			for(PropertyDescriptor desc : beanInfo.getPropertyDescriptors()){
				if(propertyName.equals(desc.getName())){
					Method readMethod = desc.getReadMethod();
					value = readMethod.invoke(object, new Object[0]);
					break;
				}
			}
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("can't Introspection beaninfo of "+object,e);
		}
		
		return value;
	}
}
