package net.cbean.cmd.runner;

import net.cbean.cmd.CommandRunner;
import net.cbean.cmd.Dispatcher;

/**
 * This Runner is a basic runner which using cmd.properties at classpath
 * 
 * @author Tao.Wu
 * 
 */
public class SimpleRunner {

	public static void main(String[] args) {
		CommandRunner crunner = new CommandRunner(new Dispatcher("cmd.properties"));
		crunner.setStaticArgs(args);
		crunner.run(System.in, System.out);
	}

}
