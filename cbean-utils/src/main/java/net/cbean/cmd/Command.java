/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 12:20:45
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  Command.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: Command.java,v $  $Revision: 1.0 $  $Date: 12:20:45 $
 * $Log: Command.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 */
public interface Command {
	/**
	 * Specify the next command name, but please be careful don't make cycle
	 * invokes.
	 */
	static final String NEXT_CMD = "NEXT_CMD";

	public int execute(BufferedReader in, PrintStream out, CmdSession session);
}
