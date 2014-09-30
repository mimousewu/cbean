package net.cbean.rmi.server;

import java.lang.reflect.Type;
import java.rmi.RemoteException;

import net.cbean.rmi.RemoteServerManager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 * @org.apache.xbean.XBean element="injector" rootElement="true"
 *                         description="This bean managers scan all RMI server beans"
 * There is something wrong implementing org.springframework.beans.factory.config.BeanPostProcessor
 */
public class RMServiceInjector implements 
		ApplicationContextAware {

	private RemoteServerManager rsm;

	/**
	 * @deprecated
	 */
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {

		Class<?> clazz = bean.getClass();
		Type[] genericInterfaces = clazz.getGenericInterfaces();
		if (genericInterfaces != null && genericInterfaces.length > 0) {
			registerService(beanName, clazz);
		}

		return bean;
	}

	/**
	 * @deprecated
	 */
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

		return bean;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		rsm = applicationContext.getBean(RemoteServerManager.class);

		for (String beanName : applicationContext.getBeanDefinitionNames()) {
			Object bean = applicationContext.getBean(beanName);

			Class<?> clazz = bean.getClass();
			Type[] genericInterfaces = clazz.getGenericInterfaces();
			if (genericInterfaces == null || genericInterfaces.length == 0)
				continue;
			registerService(beanName, clazz);
		}
	}

	private void registerService(String beanName, Class<?> clazz) {
		try {
			rsm.initType(clazz);
		} catch (RemoteException e) {
			throw new ApplicationContextException("Failed to start RMI: "
					+ beanName, e);
		}
	}

}
