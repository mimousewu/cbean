/******************************
 * 版权所有：Cbean.net
 * 创建日期: 2006-3-15 9:51:57
 * 创建作者：吴涛
 * 文件名称：XMLModel.java
 * 最后修改时间：
 * 修改记录：
 *****************************************/
package net.cbean.xml;

import java.util.Collection;
import java.util.Map;

/*
 * XMLModel this class can mapping collections
 */
public class XMLModel<E> {
	private E obj;
	private Collection<E> collection;
	private Map<Object,E> map;

	public E getObj() {
		return obj;
	}

	public void setObj(E obj) {
		this.obj = obj;
	}

	public Collection<E> getCollection() {
		return collection;
	}

	public void setCollection(Collection<E> collection) {
		this.collection = collection;
	}

	public Map<Object, E> getMap() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public void setMap(Object map) {
		this.map = (Map<Object, E>) map;
	}

}
