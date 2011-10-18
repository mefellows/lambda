package au.com.onegeek.lambda.cli;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.onegeek.lambda.core.Lambda;
import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;
import cli.clamshell.api.Command;
import cli.clamshell.api.Configurator;
import cli.clamshell.api.Context;
import cli.clamshell.api.IOConsole;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.internal.Lists;

/**
 * TODO: Import config from ~/.lambdaconfig
 *       - Default options (browser, test dir etc.)
 * @author mfellows
 *
 */
public class LambdaCmd implements Command {
	private static final String ACTION_NAME = "lambda";
	private static final String APPLICATION_NAME = "Clambda Shell (Surface Tension)";
	private LambdaDescriptor descriptor;

	private class LambdaParams {
		@Parameter
		public List<String> parameters = Lists.newArrayList();

		@Parameter(names = { "-f", "--filename" }, arity = 1, required = true, description = "Specifies the location of input file and dataset if -dataset not set.")
		public String file;
		
		@Parameter(names = { "-d", "--dataset" }, arity = 1, required = false, description = "Specifies the location of dataset.")
		public String dataSet;

		@Parameter(names = { "-e", "--environment" }, arity = 1, required = true, description = "Specifies the hostname for which to run the tests against.")
		public List<String> environment;

		@Parameter(names = {"-b", "--browser" }, required = false, description = "Specifies the browsers for which to run the tests against. This parameter can be used multiple times. e.g. -b firefox -b ie7 -b chrome. Defaults to 'firefox'")
		public List<String> browsers;
	}

	private class LambdaDescriptor implements Command.Descriptor {
		private JCommander commander;
		LambdaParams parameters;

		public void setCommandArgs(String[] args) {
			commander = new JCommander((parameters = new LambdaParams()), args);
		}

		public String getName() {
			return ACTION_NAME;
		}

		public String getDescription() {
			return "Runs the Lambda Browser Testing framework";
		}

		public String getUsage() {
			StringBuilder result = new StringBuilder();
			result.append(Configurator.VALUE_LINE_SEP)
					.append("\tlambda [options]")
					.append(Configurator.VALUE_LINE_SEP);

			for (Map.Entry<String, String> entry : getArgsDescription().entrySet()) {
				result.append(String.format("%n\t%1$-20s %2$s %3$s", entry.getKey(), " ", entry.getValue()));
			}
			
			result.append(String.format("\n\n\t@ Syntax: " +					
					"\n\n\tClambda Shell (surface tension) supports the @ syntax to import the commandline args." +
					"\n\t\tExample: 'mfellows > lambda @/Users/mfellows/Desktop/retail.txt' " +
					"\n\t\tIf you're really lazy, create a ~/.config file and put your defaults there so you can do this: 'mfellows > lambda @retail.txt"));

			return result.toString();
		}

		public Map<String, String> getArgsDescription() {
			if (commander == null) {
				commander = new JCommander(new LambdaParams());
			}
			Map<String, String> result = new HashMap<String, String>();
			List<ParameterDescription> params = commander.getParameters();
			for (ParameterDescription param : params) {
				result.put(param.getNames(), param.getDescription());
			}

			return result;
		}
	}

	@Override
	public Object execute(Context ctx) {

		String[] args = (String[]) ctx.getValue(Context.KEY_COMMAND_LINE_ARGS);		
		IOConsole c = ctx.getIoConsole();
		
		for (String arg : args) {
			c.writeOutput("Key provided: " + arg + "\n");
		}
		
		if (args != null) {
			try {
				descriptor.setCommandArgs(args);
			} catch (RuntimeException ex) {
				c.writeOutput(String.format("%nUnable execute command: %s%n%n",
						ex.getMessage()));
				return null;
			}

			// decipher args

			// >sysinfo -props
			//if (descriptor != null && descriptor.parameters.browsers != null && descriptor.parameters.browsers.length != 0) {
			if (descriptor != null && descriptor.parameters.browsers != null) {
				for (String browser : descriptor.parameters.browsers) {
					c.writeOutput(String.format("%nBrowser specified: " + browser));					
				}
				
			}

		}

		return null;
/*
		IOConsole console = ctx.getIoConsole();
		// console.writeOutput(ctx.getValue("foo"));
		console.writeOutput(String.format("%n%s%n%n", new Date().toString()));

		String filename = "";

		Lambda lambda = new Lambda();
		try {
			lambda.runWithParams(filename);
		} catch (UnableToParseTestsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnableToParseDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		*/
	}

	@Override
	public void plug(Context plug) {
		descriptor = new LambdaDescriptor();
	}

	@Override
	public Command.Descriptor getDescriptor() {
		return this.descriptor;
	}
}