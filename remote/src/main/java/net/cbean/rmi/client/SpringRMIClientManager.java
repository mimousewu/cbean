package net.cbean.rmi.client;

import java.util.HashMap;
import java.util.Map;

import net.cbean.rmi.RemoteClientManager;
import net.cbean.rmi.annotation.RMIService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * RMI stub management class
 * 
 * @author wutao
 * 
 * @org.apache.xbean.XBean element="client" rootElement="true"
 */
public class SpringRMIClientManager implements RemoteClientManager {

	private static final Log log = LogFactory
			.getLog(SpringRMIClientManager.class);

	private Map<String, Object> stubs = new HashMap<String, Object>();

	private String rmiHost = "localhost";

	private int rmiPort = 1099;

	public <T> T getRemoteService(Class<T> t) {
		return getRemoteService(t, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T getRemoteService(Class<T> t, String serviceName) {
		if (serviceName == null || "".equals(serviceName)) {
			serviceName = genServiceName(t);
		}

		if (!stubs.containsKey(serviceName)) {
			stubs.put(serviceName, newInstance(t, serviceName));
		}
		return (T) stubs.get(serviceName);
	}

	private <T> String genServiceName(Class<T> t) {
		RMIService rs = t.getAnnotation(RMIService.class);
		if (rs != null) {
			String serviceName = rs.value();
			if (serviceName == null) {
				serviceName = t.getSimpleName();
			}
			return serviceName;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	<T> T newInstance(Class<T> t, String serviceName) {
		RmiProxyFactoryBean rfb = new RmiProxyFactoryBean();
		rfb.setServiceUrl("rmi://" + rmiHost + ":" + rmiPort + "/"
				+ serviceName);
		rfb.setServiceInterface(t);
		rfb.prepare();
		rfb.afterPropertiesSet();
		rfb.setRefreshStubOnConnectFailure(true);
		T remoteService = (T) rfb.getObject();

		log.info("Prepared RMI client proxy: " + serviceName);
		return remoteService;
	}

	public void resetConnection() {
		stubs.clear();
	}
	
	/**
	 * @org.apache.xbean.Property alias="port" nestedType="java.lang.Integer"
	 *    description="The RMI port refer to the port which offering RMI services. default value is '1099'"
	 */
	public void setRmiPort(int rmiPort) {
		this.rmiPort = rmiPort;
	}
	
	/**
	 * @org.apache.xbean.Property alias="hostname"
	 *    description="The RMI hostname refer to the host name which offering RMI services. default value is 'localhost'"
	 */
	public void setRmiHost(String rmiHost) {
		this.rmiHost = rmiHost;
	}
}
