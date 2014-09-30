package net.cbean.cmd;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringDispatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext-mail.xml");
		SpringDispatcher config = new SpringDispatcher("conf/springcmd.properties", context);
		CommandRunner crunner = new CommandRunner(config);
		crunner.setStaticArgs(args);
		crunner.run(System.in, System.out);
	}
}
