/*
 * #%L
 * Lambda CLI
 * %%
 * Copyright (C) 2011 null
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package au.com.onegeek.lambda.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import cli.clamshell.api.Command;
import cli.clamshell.api.Configurator;
import cli.clamshell.api.Context;
import cli.clamshell.api.IOConsole;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.internal.Lists;

/**
 * TODO: Import config from ~/.lambdaconfig - Default options (browser, test dir
 * etc.)
 * 
 * @author mfellows
 * 
 */
public class LogLevelCmd implements Command {
	private static final String ACTION_NAME = "log";
	private LogLevelDescriptor descriptor;

	private class LambdaParams {
		@Parameter
		public List<String> parameters = Lists.newArrayList();

		@Parameter(names = { "--level", "-l" }, required = true, description = "Set the Log Level. One of trace, debug, info, warn, error, crit (case-insensitive).")
		public String logLevel;
	}

	private class LogLevelDescriptor implements Command.Descriptor {
		private JCommander commander;
		LambdaParams parameters;

		public void setCommandArgs(String[] args) {
			commander = new JCommander((parameters = new LambdaParams()), args);
		}

		public String getName() {
			return ACTION_NAME;
		}

		public String getDescription() {
			return "Modifies\\Views log settings";
		}

		public String getUsage() {
			StringBuilder result = new StringBuilder();
			result.append(Configurator.VALUE_LINE_SEP)
					.append("\tlambda [options]")
					.append(Configurator.VALUE_LINE_SEP);

			for (Map.Entry<String, String> entry : getArgsDescription()
					.entrySet()) {
				result.append(String.format("%n\t%1$-20s %2$s %3$s",
						entry.getKey(), " ", entry.getValue()));
			}

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

		if (args != null) {
			try {
				descriptor.setCommandArgs(args);
			} catch (RuntimeException ex) {
				c.writeOutput(String.format("%nUnable execute command: %s%n%n",
						ex.getMessage()));
				return null;
			}

			if (descriptor != null) {
				// Extract log level
				if (descriptor.parameters.logLevel != null) {
					final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
					Level level = null;

					if (descriptor.parameters.logLevel
							.equalsIgnoreCase("trace")) {
						level = Level.TRACE;
					} else if (descriptor.parameters.logLevel
							.equalsIgnoreCase("debug")) {
						level = Level.DEBUG;
					} else if (descriptor.parameters.logLevel
							.equalsIgnoreCase("info")) {
						level = Level.INFO;
					} else if (descriptor.parameters.logLevel
							.equalsIgnoreCase("warn")) {
						level = Level.WARN;
					} else if (descriptor.parameters.logLevel
							.equalsIgnoreCase("error")) {
						level = Level.ERROR;
					} else if (descriptor.parameters.logLevel
							.equalsIgnoreCase("off")) {
						level = Level.OFF;
					}

					
					for (ch.qos.logback.classic.Logger log : lc.getLoggerList()) {
						log.setLevel(level);
					}
				}
			}
		}

		return null;
	}

	@Override
	public void plug(Context plug) {
		descriptor = new LogLevelDescriptor();
	}

	@Override
	public Command.Descriptor getDescriptor() {
		return this.descriptor;
	}
}
