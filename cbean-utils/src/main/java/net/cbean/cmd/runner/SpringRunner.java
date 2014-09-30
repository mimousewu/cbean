package net.cbean.cmd.runner;

import net.cbean.cmd.CommandRunner;
import net.cbean.cmd.SpringDispatcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is an runner that commands have ability to use spring beans by use default
 * configuration files cmd.properties and applicationContext.xml in classpath
 * 
 * @author Tao.Wu
 *
 */
public class SpringRunner {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SpringDispatcher config = new SpringDispatcher("cmd.properties", context);
		CommandRunner crunner = new CommandRunner(config);
		crunner.setStaticArgs(args);
		crunner.run(System.in, System.out);
	}

}
