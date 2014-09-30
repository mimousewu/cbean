package net.cbean.rmi.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.cbean.rmi.ClassUtil;
import net.cbean.rmi.RemoteServerManager;
import net.cbean.rmi.annotation.RMIService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.stereotype.Service;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 * @org.apache.xbean.XBean element="server" rootElement="true"
 *                         description="This bean managers the RMI server beans"
 */
public class SpringRMIServiceManager implements RemoteServerManager,
		ApplicationContextAware {

	private static final Log log = LogFactory
			.getLog(SpringRMIServiceManager.class);

	private ApplicationContext context;
	private boolean registered = false;

	private int rmiPort = 1099;

	private String rmiHost = "localhost";

	private String scanPackage;

	private Map<String, RmiServiceExporter> exporterMap = new HashMap<String, RmiServiceExporter>();
	private Map<String, Entry> register = new HashMap<String, Entry>();

	public int destory(String serviceName) throws RemoteException {
		RmiServiceExporter exporter = exporterMap.get(serviceName);
		if (exporter != null) {
			exporter.destroy();
			exporterMap.remove(serviceName);
			register.remove(serviceName);
			exporter = null;
			return 0;
		} else {
			return 1;
		}
	}

	public void init() throws RemoteException, ClassNotFoundException,
			IOException {
		// LocateRegistry.createRegistry(rmiPort);

		for (Class<?> clazz : ClassUtil.findPackageClasses(scanPackage, true)) {
			Type[] genericInterfaces = clazz.getGenericInterfaces();
			if (genericInterfaces == null || genericInterfaces.length == 0)
				continue;

			initType(clazz);
		}
	}

	public void initType(Class<?> clazz) throws RemoteException {
		Type[] genericInterfaces = clazz.getGenericInterfaces();

		String serviceName = null;
		try {
			for (Type type : genericInterfaces) {
				Class<?> intf = (Class<?>) type;
				RMIService rmiService = intf.getAnnotation(RMIService.class);
				if (rmiService == null)
					continue;

				serviceName = rmiService.value();
				if (serviceName == null || "##default".equals(serviceName)) {
					serviceName = intf.getSimpleName();
				}
				String beanName = ((Service) clazz.getAnnotation(Service.class))
						.value();

				Object bean = null;
				if (beanName != null && !"".equals(beanName.trim())) {
					bean = context.getBean(beanName);
				}
				if (bean == null) {
					bean = context.getBean(clazz);
				}

				registerService(serviceName, bean, intf);
				break;
			}
		} catch (Exception e) {
			if (e instanceof RemoteException) {
				throw (RemoteException) e;
			} else {
				log.error("Error occur while register service" + serviceName, e);
			}
		}
	}

	public int start(String serviceName) throws RemoteException {
		return start(serviceName, register.get(serviceName).beanName);
	}

	public int start(String serviceName, String beanName)
			throws RemoteException {
		return start(serviceName, context.getBean(beanName));
	}

	private int start(String serviceName, Object beanName)
			throws RemoteException {
		if (this.context.getBean(beanName.getClass()) != null) {
			Class<?> intf = register.get(serviceName).intf;
			registerService(serviceName, beanName, intf);
			register.put(serviceName, new Entry(beanName, intf));
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * @param serviceName
	 * @param bean
	 * @param intf
	 * @throws RemoteException
	 */
	private void registerService(String serviceName, Object bean, Class<?> intf)
			throws RemoteException {
		createRegistry();

		RmiServiceExporter rse = new RmiServiceExporter();
		rse.setServiceName(serviceName);
		rse.setRegistryHost(rmiHost);
		rse.setRegistryPort(rmiPort);
		rse.setServiceInterface(intf);
		rse.setService(bean);
		rse.prepare();
		exporterMap.put(serviceName, rse);
		register.put(serviceName, new Entry(bean, intf));
		if (log.isInfoEnabled()) {
			Service serviceAnn = bean.getClass().getAnnotation(Service.class);
			String beanName = (serviceAnn == null || serviceAnn.value().equals(
					"")) ? bean.toString() : serviceAnn.value();
			log.info("Start RMI Service with name: " + serviceName
					+ ",\nURL: rmi://" + rmiHost + ":" + rmiPort + "/"
					+ serviceName + ",\nSpring bean Name: " + beanName
					+ ",\nInterface Class: " + intf.getName());
		}
	}

	/**
	 * @org.apache.xbean.Property alias="port" nestedType="java.lang.Integer"
	 *                            description=
	 *                            "The RMI port refer to the port which offering RMI services. default value is '1099'"
	 */
	public void setRmiPort(int rmiPort) {
		this.rmiPort = rmiPort;
	}

	public Collection<String> getServiceNames() {
		return exporterMap.keySet();
	}

	/**
	 * @org.apache.xbean.Property alias="base-package" description=
	 *                            "The package will be searched automatically and register the RMI service to RMIRegistry."
	 */
	public void setScanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
	}

	public Map<String, RmiServiceExporter> getExporterMap() {
		return exporterMap;
	}

	public int getRmiPort() {
		return rmiPort;
	}

	/**
	 * @org.apache.xbean.Property alias="hostname" description=
	 *                            "The RMI hostname refer to the host name which offering RMI services. default value is 'localhost'"
	 */
	public void setRmiHost(String rmiHost) {
		this.rmiHost = rmiHost;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	private void createRegistry() throws RemoteException {
		if (!registered) {
			LocateRegistry.createRegistry(rmiPort);
			registered = true;
		}
	}

	class Entry {
		private Object beanName;
		private Class<?> intf;

		public Entry(Object beanName, Class<?> intf) {
			this.beanName = beanName;
			this.intf = intf;
		}
	}

}
