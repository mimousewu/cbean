/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 12:18:12
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  CmdSession.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: CmdSession.java,v $  $Revision: 1.0 $  $Date: 12:18:12 $
 * $Log: CmdSession.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 *         This object use to save some variables during the command is
 *         executing. Each new command line will create one more cmdSession
 *         object.
 */
public class CmdSession {
	public static final String ARG = "ARGUMENT_LINE";

	public static final String STATIC_ARG = "STATIC_ARGUMENT_LINE";

	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	private List<String> cmdChain = new ArrayList<String>();

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name, Class<T> t) {
		Object value = getAttribute(name);
		if (value == null)
			return null;
		else
			return (T) value;
	}

	public void putAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
	public void logCommand(String cmd){
		this.cmdChain.add(cmd);
	}
	
	public List<String> cmdHistory(){
		return this.cmdChain;
	}
}
