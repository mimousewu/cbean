/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 12:25:40
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  OutputInputTest.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: OutputInputTest.java,v $  $Revision: 1.0 $  $Date: 12:25:40 $
 * $Log: OutputInputTest.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;


/**
 * @author Simon.Wu <br>
 * <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a><p>
 * 
 */
public class OutputInputTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CommandRunner crunner = new CommandRunner(new Dispatcher("conf/cmd.properties"));
		crunner.setStaticArgs(args);
		crunner.run(System.in, System.out);
	}

}
