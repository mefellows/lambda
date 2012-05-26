/*
 * #%L
 * Lambda Core
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
package au.com.onegeek.lambda.core;

/**
 * The TestCommand class contains the information for running a particular keyword-driven
 * command from a test case. It contains the keyword (operation) and the ordered parameters to run those commands.
 * 
 * @author mfellows
 *
 */
public class TestCommand {

	protected String command;
	
	protected Object[] parameters;

	public TestCommand(String command, Object...parameters) {
		this.command = command;
		this.parameters = parameters;
	}
	
	public TestCommand(String command) {
		this.command = command;
		this.parameters = new Object[0];
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(Object param) {
		Object[] newParams = new Object[this.parameters.length + 1];
		
		int i = 0;
		for (Object obj : this.parameters) {
			newParams[i] = obj;
			i++;
		}
		
		newParams[i] = param;
		this.parameters = newParams;
	}
}
