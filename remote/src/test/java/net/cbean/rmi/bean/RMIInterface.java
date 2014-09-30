package net.cbean.rmi.bean;

import net.cbean.rmi.annotation.RMIService;

@RMIService("mock")
public interface RMIInterface {
	String name();
}
