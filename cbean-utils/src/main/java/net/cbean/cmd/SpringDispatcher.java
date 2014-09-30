package net.cbean.cmd;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@Repository
public class SpringDispatcher extends Dispatcher implements BeanFactoryAware {
	private static final Log log = LogFactory.getLog(SpringDispatcher.class);

	BeanFactory beanFactory;

	public SpringDispatcher() {
	}

	public SpringDispatcher(BeanFactory beanFactory) {
		this(null, beanFactory);
	}

	public SpringDispatcher(String classpathConfig, BeanFactory beanFactory) {
		setBeanFactory(beanFactory);
		if (classpathConfig != null) {
			super.init(classpathConfig);
		}
		init(beanFactory);
	}

	private void init(BeanFactory beanFactory) {
		if (beanFactory instanceof ApplicationContext) {
			Map<String, Command> commands = ((ApplicationContext) beanFactory)
					.getBeansOfType(Command.class);
			for (Map.Entry<String, Command> entry : commands.entrySet()) {
				super.setCommand(entry.getKey(), entry.getValue(), true);
			}
		}
	}

	@Override
	protected Command retrieveCommand(String className)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Object obj = null;
		if (this.beanFactory != null) {
			if (this.beanFactory.containsBean(className)) {
				obj = this.beanFactory.getBean(className);
			} else {
				try {
					obj = this.beanFactory.getBean(Class.forName(className));
				} catch (Exception e) {
					log.warn("Can not get bean from context by className "
							+ className, e);
				}
			}
		}
		if (obj instanceof Command) {
			return (Command) obj;
		} else {
			return super.retrieveCommand(className);
		}
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
