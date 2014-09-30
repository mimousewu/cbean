package net.cbean.binary;

import java.util.Map;

/**
 * ÄÚ²¿EntryÀà
 * @author WuTao
 *
 * 2006-8-2 14:48:53
 */
public class MapItem{
	private Object key;
	private Object value;
	
	public MapItem(){}
	
	public MapItem(Map.Entry<Object, Object> entry){
		this(entry.getKey(),entry.getValue());
	}
	
	public MapItem(Object key,Object value){
		this.key = key;
		this.value = value;
	}

	/**
	 * @return Returns the key.
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @param key The key to set.
	 */
	public void setKey(Object key) {
		this.key = key;
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
}