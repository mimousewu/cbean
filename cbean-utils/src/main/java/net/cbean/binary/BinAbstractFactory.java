/**
 * @author WuTao
 *
 * 2006-8-2 9:51:03
 */
package net.cbean.binary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 为BinMarshaller和BinUnmarshaller提供一些公共的处理方法。这种设计模式并不优雅，以后有时间可以考虑改成委托模式
 */
public abstract class BinAbstractFactory {

	public static final String PROPERTY_REGEX = "(\\$[\\w\\.]+)([+-]*)([\\d]*)";  //例子 $length-2
	public static final String METHOD_REGEX = "([\\w\\.]+)\\.([\\w]+)\\(([\\$\\w,\\.]*)\\)";  //例子classpath.class.method($arg1,$arg2...)
	private static final Pattern METHOD_PATTERN = Pattern.compile(METHOD_REGEX);
	private static final Pattern PROPERTY_PATTERN = Pattern.compile(PROPERTY_REGEX);
	
	protected ParserHandle parserHandle ;
	protected BinMapping mapping;
	protected ByteBuffer data;
	
	/**
	 * 解析属性长度。属性长度len支持数字、改属性以上的所有属性值，或者静态方法调用的结果。<br>
	 * 如果使用静态方法调用，必须保证静态方法的返回值是int。
	 * @param structure
	 * @param strLen
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	protected Long getLength(final Structure structure, String strLen) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(strLen==null) {
			return null;
		}
		long length = 0;
		if(strLen.matches("\\d+")){  //直接的数值
			length = Long.parseLong(strLen);
		}else if("@END".equals(strLen.toUpperCase())){ //直到结束，说明此Property后面不应该存在其他Property了。
			length = data.limit()-data.position();
		}else if(strLen.matches(PROPERTY_REGEX)){  //使用已经存在的属性，支持+-数值的公式
			Matcher matcher = PROPERTY_PATTERN.matcher(strLen.subSequence(0, strLen.length()));
			matcher.find();
			
			Object lengthObj = getArgValue(structure.getId(), matcher.group(1));
			if(lengthObj instanceof Long){
				length =  (Long) lengthObj;
			}else if(lengthObj instanceof Integer){
				length = (long) (Integer)lengthObj;
			}
			if(!matcher.group(3).equals("")){
				int second = Integer.parseInt(matcher.group(3));
				if("+".equals(matcher.group(2))){
					length = length + second;
				}else if("-".equals(matcher.group(2))){
					length = length - second;
				}
			}
		}else if(strLen.matches(METHOD_REGEX)){  //外部静态函数
			Object obj = invokeStaticMethod(structure, strLen);
			if(obj instanceof Long){
				length = (Long) obj;
			}else if (obj instanceof Integer){
				length = (Integer)obj;
			}else{
				throw new IllegalStateException("Can't parse to length: "+obj);
			}
		}else if(strLen==null || "".equals(strLen)){
			length = 0;
		}else{
			throw new IllegalArgumentException("Can't parse propety length: "+strLen);
		}
		return length;
	}
	
	/**
	 * 解析属性的解析器，通过type配置从ParserManager中取得。
	 * @param structure
	 * @param parserStr
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected BinParser getParser(final Structure structure, final String parserStr) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		BinParser parser;
		if(parserStr.matches("\\w+")){  //直接的字串
			parser = parserHandle.getParser(parserStr);
		}else if(parserStr.matches(METHOD_REGEX)){  //外部静态函数
			Object obj = invokeStaticMethod(structure, parserStr);
			if(obj instanceof BinParser){
				parser = (BinParser) obj;
			}else{
				throw new IllegalStateException("Can't parse to type: "+obj);
			}
		}else{
			throw new IllegalArgumentException("Can't parse propety type: "+parserStr);
		}
		
		return parser;
	}
	
	/**
	 * 执行跟Structure相关的一个静态Java函数脚本<br>
	 *    格式为：classpath.class.method($arg1,$arg2...)
	 * @param structure
	 * @param methodStr
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	protected Object invokeStaticMethod(final Structure structure, final String methodStr) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Matcher matcher = METHOD_PATTERN.matcher(methodStr.subSequence(0, methodStr.length()));
		matcher.find();
		Class clazz = Class.forName(matcher.group(1));
		List args = new ArrayList();
		List<Class> parameterTypesList = new ArrayList<Class>();
		if(!matcher.group(3).equals("")){
			for(String arg : matcher.group(3).split(",")){
				Object argValue = getArgValue(structure.getId(),arg);
				args.add(argValue);
				if(argValue==null){
					throw new IllegalArgumentException("Can't get parameter value! parameter name: "+structure.getId()+'.'+arg);
				}
				parameterTypesList.add(argValue.getClass());
			}
		}
		
		Class[] parameterTypes = new Class[parameterTypesList.size()];
		for(int i=0; i<parameterTypes.length; i++){
			parameterTypes[i] = parameterTypesList.get(i);
		}
		Method method = clazz.getMethod(matcher.group(2), parameterTypes);
		
		Object obj = method.invoke(null, args.toArray());
		
		return obj;
	}
	
	/**
	 * 取参数值，若是本structure内部的参数，就不需要写structure的id了
	 * @param structureId
	 * @param strLen
	 * @return
	 */
	abstract Object getArgValue(final String structureId, String argStr);
}
