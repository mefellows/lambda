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
/**
 * 
 */
package au.com.onegeek.lambda.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Abstract Class.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public abstract class Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	// @TODO: can this be springyfied?
	protected CommandRunner runner;
	
	public Test() {
		runner = CommandRunner.getInstance();
	}
	
	/**
	 * Execute an arbitrary command from a Test Assertion Implementation. 
	 * 
	 * @param keyword The name of the keyword (method) to call.
	 * @param params The set of parameters to pass in.
	 */
	protected void executeCommand(String keyword, Object...params) {
		TestCommand command = new TestCommand(keyword, params);
		runner.runCommand(command);
	}
	
	/**
	 * Execute a command with no arguments.
	 * 
	 * Convenience function to workaround Javassist's lack of varargs complilation support. 
	 * 
	 * @param keyword
	 */
	protected void executeCommand(String keyword) {
		TestCommand command = new TestCommand(keyword);
		runner.runCommand(command);
	}
}
