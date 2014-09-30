package net.cbean.rmi;

/**
 * @author wutao
 * 
 */
public interface RemoteClientManager {
	/**
	 * Get Remote Service by type
	 * 
	 * @param <T>
	 * @param t
	 *            result class type
	 * @return
	 */
	public <T> T getRemoteService(Class<T> t);

	/**
	 * Get Remote Service by type and service name
	 * 
	 * @param <T>
	 * @param t
	 *            result class type
	 * @param serviceName
	 * @return
	 */
	public <T> T getRemoteService(Class<T> t, String serviceName);

	/**
	 * Sometimes RMI connection could interrupt by some reasons. This method
	 * will clear the stubs in client manager.
	 */
	public void resetConnection();
}
