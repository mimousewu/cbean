/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 7:25:17
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  SecurityTest.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: SecurityTest.java,v $  $Revision: 1.0 $  $Date: 7:25:17 $
 * $Log: SecurityTest.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.security;

/**
 * @author Simon.Wu <br>
 * <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a><p>
 * 
 */
public class SecurityTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SecurityManager s = new SecurityManager();
		System.setSecurityManager(s);
	}

}
