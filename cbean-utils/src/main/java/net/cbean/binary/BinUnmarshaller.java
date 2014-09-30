/**
 * @author WuTao
 *
 * 2006-7-28 11:07:28
 */
package net.cbean.binary;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.cbean.exceptions.ParseException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 二进制转化成对象的解析器，调用unmarshal(final byte[] input)方法，返回一个期望的范型对象。<br>
 * 此类是线程不安全的，有必要为每个处理过程new出来一个BinUnmarshaller对象
 */
public class BinUnmarshaller<V> extends BinAbstractFactory{
	private static final Log log = LogFactory.getLog(BinUnmarshaller.class);
	
	private Map<String,Object> arguments = new HashMap<String,Object>();
	private Map<String,Long> childStructreLength = new HashMap<String,Long>();
	
	
	public BinUnmarshaller(BinMapping mapping,ParserHandle parserHandle){
		this.mapping = mapping;
		this.parserHandle = parserHandle;
	}
	
	@SuppressWarnings("unchecked")
	public V unmarshal(final byte[] input) throws ParseException {
		data = ByteBuffer.wrap(input);
		
		Structure structure = mapping.getStructureList().get(0); //从第一个Structrue开始
		
		try {
			return (V) workRound( structure);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException("Can't unmarshal byte array to Object!",e);
		}
	}
	
	/**
	 * 处理
	 * @param data
	 * @param structure
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("unchecked")
	private Object workRound(final Structure structure) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Class clazz = Class.forName(structure.getClazz());
		Object obj = clazz.newInstance();
		
		boolean isCollection = !(structure.getCollection()==null || "".equals(structure.getCollection()));
		
		Long structureLength = childStructreLength.get(structure.getId());
		long endPosition = (structureLength==null) ? 
							data.limit() :
							structureLength + data.position();
							
		while(data.position()<endPosition){   //用于执行collection的情况，如果不是collection，只会执行一次，在方法体后面有判断
			for(Property prop : structure.getProperties()){
				Object value = null;
				
				Long length = getLength(structure,prop.getLength());
				if(length!=null && data.position()+length>endPosition){
					throw new IllegalArgumentException(structure.getId()+"."+prop.getName()+" data out of range. end postion is "+endPosition+", but want to get "+length+" bytes from index "+data.position());
				}
				
				
				Structure child = this.mapping.getStructureMap().get(prop.getType());

				if(length==null && child==null){
					throw new IllegalArgumentException("there is no length at "+structure.getId()+"."+prop.getName());
				}
				
				if(child!=null){
					childStructreLength.put(child.getId(), length);  //首先设置这个子结构的长度，便于下次循环时可以拿到
					value = workRound(child);  //递归，处理子structure
				}else{
					//处理普通属性
					if(length>0){
						byte[] pdata = new byte[(int) length.longValue()];
						data.get(pdata);
						BinParser parser = getParser(structure, prop.getType());
						value = parser.decode(pdata);
					}else if(prop.getValue().matches(PROPERTY_REGEX)){
						value = getArgValue(structure.getId(),prop.getValue());
					}
				}
				
				arguments.put(structure.getId()+"."+prop.getName(), value);
				
				if(!isCollection && !prop.isIgnore()){
					BeanUtils.copyProperty(obj, prop.getName(), value);
				}
			}
			
			String collectionTag = structure.getCollection();
			if(collectionTag!=null && "MAP".equals(collectionTag.toUpperCase())){
				Object key = arguments.get(structure.getId()+"."+"key");
				Object value = arguments.get(structure.getId()+"."+"value");
				((Map)obj).put(key, value);
			}else if(collectionTag!=null && "COLLECTION".equals(collectionTag.toUpperCase())){
				Object value = arguments.get(structure.getId()+"."+"value");
				((Collection)obj).add(value);
			}
			
			if(data.position()>endPosition){
				throw new IllegalStateException("please check if data input correct! expect end position is "+endPosition+" but now is "+data.position());
			}
			//若不是Collection结构，却执行超过一次的循环，表示报文长度超过预期值！但是并不影响解析，只解析一次就跳出。
			if(!isCollection){
				if(data.position()!=endPosition){
					log.warn("structure "+structure.getId()+" end unnormal! end position:"+endPosition+" but now is "+data.position());
				}
				break;
			}
		}
		return obj;
	}

	
	@Override
	Object getArgValue(final String structureId, String argStr) {
		argStr = argStr.substring(1);
		Object argObj = this.arguments.get(
				(argStr.indexOf('.')>0) ? argStr : structureId+'.'+argStr);
		if(argObj==null){
			throw new IllegalArgumentException("can't get argument of "+structureId+'.'+argStr);
		}
		return argObj;
	}
}
