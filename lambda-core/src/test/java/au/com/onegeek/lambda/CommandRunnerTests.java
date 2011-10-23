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
package au.com.onegeek.lambda;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import au.com.onegeek.lambda.core.CommandRunner;
import au.com.onegeek.lambda.core.TestCommand;

public class CommandRunnerTests {

	
	CommandRunner runner;
	
	@BeforeClass
	private void setup() {
		this.runner = CommandRunner.getInstance();		
	}	
	
	@Test
	public void testCommandRunner() {		
		TestCommand command = new TestCommand("getBodyText");		
		runner.runCommand(command);

		command = new TestCommand("isTextPresent", "I am here to");		
		runner.runCommand(command);
	}
	
	@Test(expectedExceptions={java.lang.AssertionError.class})
	public void testMethodNotExist() {
		TestCommand command = new TestCommand("I do not exist", "I am here to");		
		runner.runCommand(command);
	}
}
