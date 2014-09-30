/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 12:53:55
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  Dispatcher.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: Dispatcher.java,v $  $Revision: 1.0 $  $Date: 12:53:55 $
 * $Log: Dispatcher.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.cbean.io.ClasspathUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 */
public class Dispatcher {
	private static final Log log = LogFactory.getLog(Dispatcher.class);

	public static final String INIT_CMD = "INIT_CMD";

	private Map<String, Command> commands = new HashMap<String, Command>();

	private String initCmd;

	public Dispatcher() {
	}

	public Dispatcher(String classpathConfig) {
		init(classpathConfig);
	}

	protected void init(String classpathConfig) {
		int idx = classpathConfig.lastIndexOf('/');
		String pkg = "";
		if (idx > 0) {
			pkg = classpathConfig.substring(0, idx);
			classpathConfig = classpathConfig.substring(idx + 1);
		}
		try {
			for (InputStream in : ClasspathUtil.findPackageFiles(pkg,
					classpathConfig)) {
				Properties p = new Properties();
				p.load(in);
				loadCommandConfig(p);
			}
		} catch (IOException e) {
			log.warn("Can not load command from current classpath!", e);
		}
	}

	public Command getCommand(String cmd) {
		return commands.get(cmd);
	}

	public void loadCommandConfig(String cmdConfFile) throws IOException {
		Properties p = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(
				cmdConfFile);
		p.load(in);
		loadCommandConfig(p);
	}

	private void loadCommandConfig(Map<?, ?> commandConfs) {
		try {
			for (Map.Entry<?, ?> entry : commandConfs.entrySet()) {
				String key = entry.getKey().toString();
				String value = entry.getValue().toString();
				if (INIT_CMD.equals(key)) {
					initCmd = value;
					continue;
				}

				if (commands.containsKey(key)
						&& !commands.get(key).equals(retrieveCommand(value))) {
					throw new IllegalArgumentException(
							"Dumplicate configurations of command " + key);
				} else {
					commands.put(key, retrieveCommand(value));
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"can't init command configurations.", e);
		}
	}

	protected Command retrieveCommand(String className)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return (Command) Class.forName(className).newInstance();
	}

	public Map<String, Command> getCommands() {
		return commands;
	}

	protected boolean setCommand(String name, Command command,
			boolean ignoreExistCommand) {
		if (ignoreExistCommand && commands.containsValue(command)) {
			return false;
		}

		commands.put(name, command);
		return true;
	}

	public Command getInitCommand() {
		if (initCmd != null) {
			return getCommand(initCmd);
		} else {
			return null;
		}
	}

}
