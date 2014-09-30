package net.cbean.rmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.registry.LocateRegistry;

import net.cbean.rmi.bean.RMIInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RMIRemoteServiceManagerTest {
	ApplicationContext context;
	RemoteServerManager rsm;
	RemoteClientManager rcm;

	@Before
	public void setUp() throws Exception {
//		System.setProperty("java.rmi.server.hostname", "10.33.234.107");
//		LocateRegistry.createRegistry(1099);
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"RmiApplicationContext.xml");
		rsm = context.getBean(RemoteServerManager.class);
//		rsm.init();
		rcm = context.getBean(RemoteClientManager.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRemoteServiceClass() {
		 RMIInterface service = rcm.getRemoteService(RMIInterface.class);
		 assertNotNull(service);
		 assertEquals("Wu Tao", service.name());
	}

}
