package net.cbean.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;

import net.cbean.cmd.BaseCommand;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class RunAntCommand extends BaseCommand {

	@Override
	public int execute(BufferedReader in, PrintStream out) throws Exception {
		File buildFile = new File(getAntFile());
		Project p = new Project();

		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		try {
			p.fireBuildStarted();
			p.init();
			ProjectHelper helper = ProjectHelper.getProjectHelper();
			helper.parse(p, buildFile);

			String targetName = getTargetName(p.getDefaultTarget());

			p.executeTarget(targetName);
			p.fireBuildFinished(null);
		} catch (BuildException e) {
			p.fireBuildFinished(e);
			return 1;
		}

		return 0;
	}

	protected String getTargetName(String defaultTarget) {
		String targetName = getHistoryInput("What target do you want to run (Default target please enter)? ");
		if (targetName == null || "".equals(targetName)) {
			targetName = defaultTarget;
		}
		return targetName;
	}

	protected String getAntFile() {
		String antFile = getHistoryInput("Please specify the build file (Default is build.xml): ");
		if (antFile == null || "".equals(antFile)) {
			antFile = "build.xml";
		}
		return antFile;
	}

}
