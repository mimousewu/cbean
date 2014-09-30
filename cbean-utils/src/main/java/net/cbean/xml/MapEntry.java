/****************************************
 * ∞Ê»®À˘”–£∫Cbean.net
 * Copyright (c)  by TD-Tech Inc.
 * All right reserved.
 * Create Date: 18:21:53
 * Create Author: Wu Tao
 * File Name:  MapEntry.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: MapEntry.java,v $  $Revision: 1.4 $  $Date: 2006/06/09 02:50:05 $
 * $Log: MapEntry.java,v $
 */
package net.cbean.xml;

import java.util.List;
import java.util.Map;

/*
 * MapEntry
 */
public class MapEntry<K,V> {
	private K key;
	private V value;
	
	public MapEntry(){}
	public MapEntry(K k,V v){
		this.key = k;
		this.value = v;
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	
	public List getListValue(){
		if(value instanceof List){
			return (List) value;
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setListValue(List value){
		this.value = (V) value;
	}
	
	public Map getMapValue() {
		if(value instanceof Map){
			return (Map) value;
		}else{
			return null;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MapEntry){
			MapEntry entry = (MapEntry) obj;
			if(key.equals(entry.key) && value.equals(entry.getValue()))
				return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return key.hashCode();
	}
}
