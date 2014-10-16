package net.cbean.cmd.runner;

import net.cbean.cmd.CommandRunner;
import net.cbean.cmd.SpringDispatcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is an runner that commands have ability to use spring beans by use
 * default configuration files cmd.properties and applicationContext.xml in
 * classpath
 * 
 * @author Tao.Wu
 *
 */
public class SpringRunner {

	protected static final String CONTEXT_KEY = "cmdContext";
	protected static final String CMD_CONFIG = "cmd.properties";

	public static void main(String[] args) {
		String applicationContext = "cmdApplicationContext.xml";
		if (System.getProperty(CONTEXT_KEY) != null) {
			applicationContext = System.getProperty(CONTEXT_KEY);
		}
		run(args, applicationContext);
	}

	protected static void run(String[] args, String applicationContext) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				applicationContext);
		SpringDispatcher config = new SpringDispatcher(CMD_CONFIG,
				context);
		CommandRunner crunner = new CommandRunner(config);
		crunner.setStaticArgs(args);
		crunner.run(System.in, System.out);
	}

}
