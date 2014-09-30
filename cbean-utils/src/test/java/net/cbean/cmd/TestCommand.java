/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 13:35:31
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  TestCommand.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: TestCommand.java,v $  $Revision: 1.0 $  $Date: 13:35:31 $
 * $Log: TestCommand.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.PrintStream;

/**
 * @author Simon.Wu <br>
 * <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a><p>
 * 
 */
public class TestCommand extends BaseCommand implements CmdDescriptor {

	/* (non-Javadoc)
	 * @see net.cbean.cmd.BaseCommand#execute(java.io.BufferedReader, java.io.PrintStream)
	 */
	@Override
	public int execute(BufferedReader in, PrintStream out) {
		System.out.println("run test, args:" + getArgLine());
		String arg = getInput("next arg: ");
		String arg1 = getInput("3rd arg: ");
		
		out.println(arg);
		out.println(arg1);
//		this.session.putAttribute(Command.NEXT_CMD, "test1");
		return 0;
	}

	public String description() {
		return "Test Command for cmd";
	}

}
