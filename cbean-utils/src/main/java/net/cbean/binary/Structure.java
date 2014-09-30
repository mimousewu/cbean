/**
 * @author WuTao
 *
 * 2006-7-28 11:11:55
 */
package net.cbean.binary;

import java.util.ArrayList;
import java.util.List;

/**
 * BinFactory配置文件中Structure的映射类，表示一个结构体，内部有多个Property组成
 */
public class Structure {
	/**
	 * structure的唯一标识，同一个配置文件中不能重复
	 */
	private String id;
	/**
	 * 对应的java类，会使用反射获得一个实例，所以不能是接口或者abstract类
	 */
	private String clazz;
	/**
	 * collection / map<br>
	 * 如果class是实现Collection接口的，则这个属性的值是collection，<br>
	 * 如果class是实现Map接口的，则这个属性值是map。
	 */
	private String collection;
	private List<Property> properties = new ArrayList<Property>();
	/**
	 * @return Returns the clazz.
	 */
	public String getClazz() {
		return clazz;
	}
	/**
	 * @param clazz The clazz to set.
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the properties.
	 */
	public List<Property> getProperties() {
		return properties;
	}
	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}
	/**
	 * @return Returns the collection.
	 */
	public String getCollection() {
		return collection;
	}
	/**
	 * @param collection The collection to set.
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
}
