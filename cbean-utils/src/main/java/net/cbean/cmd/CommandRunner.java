/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 13:10:33
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  CommandRunner.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: CommandRunner.java,v $  $Revision: 1.0 $  $Date: 13:10:33 $
 * $Log: CommandRunner.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 *         Set INIT_CMD in cmd.properties to run this command automatically Set
 *         NEXT_CMD in session to continue the process
 */
public class CommandRunner {
	public static final String USER_BREAK = "User Break";

	private Dispatcher dispatcher;
	private CmdSession session;
	private String[] staticArgs;

	public CommandRunner(Dispatcher config) {
		this.dispatcher = config;
	}

	public void setStaticArgs(String[] args) {
		this.staticArgs = args;
	}

	public void run(InputStream in, PrintStream out) {
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		run(bin, out);
	}

	public void run(BufferedReader bin, PrintStream out) {
		if (session == null) {
			session = new CmdSession();
			session.putAttribute(CmdSession.STATIC_ARG, this.staticArgs);
		}

		String cmd = null;
		if (dispatcher.getInitCommand() != null) {
			session.putAttribute(Dispatcher.INIT_CMD, true);
			session.putAttribute(CmdSession.ARG, this.staticArgs);
			dispatcher.getInitCommand().execute(bin, out, session);
		}

		while (true) {
			String nextCmd = session.getAttribute(Command.NEXT_CMD,
					String.class);
			session.putAttribute(Command.NEXT_CMD, null);
			if (nextCmd == null) {
				if (dispatcher.getInitCommand() != null) {
					return;
				}
				out.print("$ ");
				String fullCmd;
				try {
					fullCmd = bin.readLine().trim();
					int index = fullCmd.indexOf(' ');
					if (index > 0) {
						cmd = fullCmd.substring(0, index);
						session.putAttribute(CmdSession.ARG,
								trimArgLine(fullCmd.substring(index)));
					} else {
						cmd = fullCmd;
						session.putAttribute(CmdSession.ARG, null);
					}
				} catch (IllegalStateException e) {
					if (e.getMessage() == USER_BREAK) {
						out.print("^C\r\n");
						continue;
					}
				} catch (IOException e) {
					out.println(e.getMessage());
					continue;
				} catch (IllegalArgumentException e) {
					out.println(e.getMessage());
					continue;
				}
			} else {
				cmd = nextCmd;
			}

			Command command = dispatcher.getCommand(cmd);
			if (command != null) {
				command.execute(bin, out, session);
				this.session.logCommand(cmd);
			} else if ("exit".equals(cmd)) {
				break;
			} else if ("?".equals(cmd) || "help".equals(cmd)) {
				helpOutput(out);
			} else if (!"".equals(cmd)) {
				out.println(" invaild command line.");
			}

		}
	}

	/**
	 * Display help information
	 * 
	 * @param out
	 */
	private void helpOutput(PrintStream out) {
		out.println("Supported Commands:\r");
		for (Map.Entry<String, Command> entry : dispatcher.getCommands()
				.entrySet()) {
			Command comm = entry.getValue();
			out.print("\t" + entry.getKey() + "\t");
			if (entry.getKey().length() < 8)
				out.print("\t");
			if (comm instanceof CmdDescriptor) {
				out.println(((CmdDescriptor) comm).description() + "\r");
			} else {
				out.println(comm.getClass().getName() + "\r");
			}
		}
		out.println("\thelp/?\t\tshow this message.\r");
		out.println("\texit\t\tExit current console.\r\n");
	}

	/**
	 * Translate argument line to String array list.
	 * 
	 * @param orginalArgLine
	 * @return
	 */
	private String[] trimArgLine(String orginalArgLine) {
		List<String> args = new ArrayList<String>();

		while (orginalArgLine.matches(".*[^ ]+.*$")) {
			orginalArgLine = orginalArgLine.replaceFirst("[ ]+", "");
			int argIdx = -1;
			boolean comma = false;
			if (orginalArgLine.startsWith("\"")) {
				orginalArgLine = orginalArgLine.substring(1);
				argIdx = orginalArgLine.indexOf("\"");
				comma = true;
				if (argIdx < 0) {
					throw new IllegalArgumentException(
							"Argument error, the quotes(\") should be closed!");
				}
			} else {
				argIdx = orginalArgLine.indexOf(' ');
			}
			if (argIdx > 0) {
				args.add(orginalArgLine.substring(0, argIdx));
				if (comma)
					argIdx++;
				orginalArgLine = orginalArgLine.substring(argIdx);
			} else {
				args.add(orginalArgLine);
				orginalArgLine = "";
			}
		}

		return args.toArray(new String[] {});
	}

	public void setSession(CmdSession session) {
		this.session = session;
	}

	public CmdSession getSession() {
		return session;
	}

}
