/**
 * @author WuTao
 *
 * 2006-7-28 11:40:49
 */
package net.cbean.binary;

/**
 * BinFactory配置文件中Property的映射类，表示Structure中的一个字段
 */
public class Property {
	/**
	 * 属性名称，对应JavaBean的Property。<br>
	 *   若此Property的structure为collection，name值为value的属性会调用Collection.add()<br>
	 *   若此Property的structrue为map，name值为key和value的属性为会调用map.put(key,value)
	 */
	private String name;
	/**
	 * 属性类型，对应ParserManager中支持的类型，或者可以对应structure id,<br>
	 * 表示这个属性存储的另一个结构体
	 *  静态方法 - 也可以是外部静态Java方法的返回值，但是此静态java方法的返回值类型一定要是BinParser<br>
	 */
	private String type;
	/**
	 * 属性的长度，支持的类型如下：<br>
	 *   数值 - 十进制整数<br>
	 *   属性 - 这个属性以上的已经解析出来的属性值，并且可以是+-公式，<br>
	 *   静态方法 - 也可以是外部静态Java方法的返回值，但是此静态java方法的返回值类型一定要是Integer<br>
	 *   \@end - 表示从当前位置到byte[]结束的长度<br>
	 *   不填 - 表示这个属性是一个structure，并且这个structure是可以自指定长度的
	 */
	private String length;
	/**
	 * 标识此属性不作为JavaBean的Property
	 */
	private boolean ignore = false;
	/**
	 * 对象转化成二进制时的默认值，支持的类型如下：<br>
	 *   \@size - 表示本结构体内自此属性以下的二进制长度<br>
	 *   属性 - 这个属性以上的已经解析出来的属性二进制值<br>
	 *   ignore - 这个属性值将不在二进制中写出<br>
	 *   二进制值 - 十六进制数值字串，可以用空格分开<br>
	 *   静态方法 - 与length相同
	 */
	private String value;
	
	/**
	 * @return Returns the ignore.
	 */
	public boolean isIgnore() {
		return ignore;
	}
	/**
	 * @param ignore The ignore to set.
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	/**
	 * @return Returns the length.
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length The length to set.
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
