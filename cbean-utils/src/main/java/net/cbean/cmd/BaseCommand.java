/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 13:07:11
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  BaseCommand.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: BaseCommand.java,v $  $Revision: 1.0 $  $Date: 13:07:11 $
 * $Log: BaseCommand.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 *         This class will be extended by command class. It include some utility
 *         tools. Such as access session, resolve input output, store input to
 *         file.
 */
public abstract class BaseCommand implements Command {
	private static final Log log = LogFactory.getLog(BaseCommand.class);

	protected BufferedReader in;
	protected PrintStream out;
	protected CmdSession session;
	private Properties historyVariables;

	private int usedArgIndex;

	protected boolean forceInput;

	private String confDir = ".";

	private String getFileName() {
		return confDir + "/" + getClass().getSimpleName() + ".properties";
	}

	boolean needSave = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.cmd.Command#execute(java.io.BufferedReader,
	 * net.cbean.cmd.CmdSession)
	 */
	public int execute(BufferedReader in, PrintStream out, CmdSession session) {
		this.in = in;
		this.out = out;
		this.session = session;

		int re = 0;
		try {
			re = execute(in, out);
		} catch (Throwable e) {
			out.println("\nError occured! Please check your input and run again.");
			e.printStackTrace(out);
		}
		if (needSave) {
			storeProperties();
		}
		usedArgIndex = 0;
		return re;
	}

	/**
	 * 
	 * @param in
	 * @param out
	 * @return
	 */
	public abstract int execute(BufferedReader in, PrintStream out)
			throws Exception;

	protected String[] getArgLine() {
		return session.getAttribute(CmdSession.ARG, String[].class);
	}

	protected String getHistoryInput(String preString, String defaultValue) {
		String value = getHistoryInput(preString);
		if (value == null || "".equals(value)) {
			historyVariables.put(preString, defaultValue);
			value = defaultValue;
		}
		return value;
	}

	protected String getHistoryInput(String preString) {
		if (historyVariables == null) {
			loadProperties();
		}

		String value = historyVariables.getProperty(preString);
		Object obj = this.session.getAttribute(Dispatcher.INIT_CMD);
		boolean isInit = (obj == null) ? false : (Boolean) obj;

		if (value == null) {
			value = getInput(preString);
			needSave = true;
		} else if (!isInit || this.forceInput) {
			String outputString = (!"".equals(value)) ? preString + "[ "
					+ value + " ]? " : preString;
			String newValue = getInput(outputString);
			if (!"".equals(newValue)) {
				value = newValue;
			}
			needSave = true;
		}
		historyVariables.put(preString, value);
		return value;
	}

	private void storeProperties() {
		try {
			File dir = new File(confDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			historyVariables.store(
					new FileOutputStream(new File(getFileName())),
					"history variables");
		} catch (FileNotFoundException e) {
			// do not need log
		} catch (IOException e) {
			log.error("can't store history variables for "
					+ getClass().getSimpleName(), e);
		}
	}

	private void loadProperties() {
		historyVariables = new Properties();
		try {
			historyVariables.load(new FileInputStream(new File(getFileName())));
		} catch (FileNotFoundException e) {
			// do not need log
		} catch (IOException e) {
			log.error("can't load history variables for "
					+ getClass().getSimpleName(), e);
		}
	}

	protected String getInput(String preString) {
		try {
			if (!forceInput && getArgLine() != null
					&& usedArgIndex < getArgLine().length) {
				return getArgLine()[usedArgIndex++];
			} else {
				if (null == preString || "".equals(preString)) {
					out.print("Anonymous arg: ");
				} else {
					out.print(preString);
				}
				return in.readLine();
			}
		} catch (IOException e) {
			throw new IllegalStateException(
					"can't get input from InputStream. ", e);
		}
	}

	/**
	 * This method is use to set next command name. the command will be execute
	 * after current command done immediately.
	 * 
	 * @param nextCmd
	 */
	protected void nextCmd(String nextCmd) {
		this.session.putAttribute(Command.NEXT_CMD, nextCmd);
	}

	protected void setConfDir(String confDir) {
		this.confDir = confDir;
	}

	protected String getConfDir() {
		return confDir;
	}

}
