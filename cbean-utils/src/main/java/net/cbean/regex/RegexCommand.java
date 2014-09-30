package net.cbean.regex;

import java.io.BufferedReader;
import java.io.PrintStream;

import net.cbean.cmd.BaseCommand;

public class RegexCommand extends BaseCommand {

	@Override
	public int execute(BufferedReader in, PrintStream out) throws Exception {
		Runtime.getRuntime().exec("java -cp lib/cbeanUtils.jar net.cbean.regex.AppRegexDemo");
		return 0;
	}

}
