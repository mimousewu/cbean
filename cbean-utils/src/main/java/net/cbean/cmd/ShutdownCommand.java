package net.cbean.cmd;

import java.io.BufferedReader;
import java.io.PrintStream;

public class ShutdownCommand implements Command, CmdDescriptor{

	public int execute(BufferedReader in, PrintStream out, CmdSession session) {
		out.println("Shuting down...");
		System.out.println("Shuting down...");
		System.exit(0);
		return 0;
	}

	public String description() {
		return "Shutdown command server, Console will exit right now.";
	}

}
