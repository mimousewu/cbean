/**
 * @author WuTao
 *
 * 2006-9-1 11:16:40
 */
package net.cbean.binary;

/**
 * 用来处理二进制与对象之间的转化关系
 */
public interface BinParser<V> {
	/**
	 * 解开二进制
	 * @param data
	 * @return
	 */
	public V decode(byte[] data);
	
	/**
	 * 转化成二进制
	 * @param value
	 * @return
	 */
	public byte[] encode(V value);
	
	/**
	 * 转化成定长二进制
	 * @param value
	 * @param length 二进制长度
	 * @return
	 */
	public byte[] encode(V value,int length);
}
