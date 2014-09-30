/**
 * @author WuTao
 *
 * 2006-9-1 11:27:20
 */
package net.cbean.binary;


/**
 * 用来获得BinParser解析器
 */
public interface ParserHandle {
	/**
	 * 根据类型名称获得属性解析器
	 * @param parserName
	 * @return
	 */
	public BinParser getParser(String parserName);
	
	/**
	 * 获得带类型信息的属性解析器
	 * @param <T>
	 * @param parserName
	 * @param t
	 * @return
	 */
	public <T> BinParser<T> getParser(String parserName,Class<T> t);
	
	/**
	 * 增加一种新的Parser
	 * @param key
	 * @param parser
	 */
	public void setParser(String key, BinParser parser);
}
