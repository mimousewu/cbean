package net.cbean.rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

public interface RemoteServerManager {

	int start(String serviceName) throws RemoteException;
	
	int start(String serviceName, String beanName) throws RemoteException;
	
	int destory(String serviceName) throws RemoteException;
	
	Collection<String> getServiceNames();
	
	void init() throws RemoteException, ClassNotFoundException, IOException;
	
	void initType(Class<?> clazz) throws RemoteException ;
	
	int getRmiPort();
}
