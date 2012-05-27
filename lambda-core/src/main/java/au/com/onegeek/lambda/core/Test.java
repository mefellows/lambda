/*
 * #%L
 * Lambda Core
 * %%
 * Copyright (C) 2011 OneGeek
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import au.com.onegeek.lambda.api.TestCommand;

/**
 * Test Abstract Class.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
@Configurable()
public abstract class Test implements au.com.onegeek.lambda.api.Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	// @TODO: can this be springyfied?
	@Autowired
	protected CommandRunner runner;
	
//	public Test() throws InterruptedException {
//		runner = CommandRunner.getInstance();
		
//		logger.info("********* CommandRunner: About to load spring context");
//		Thread.sleep(1000);
//		ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/lambda-context.xml");
//		this.runner = context.getBean(CommandRunner.class);
//		Lambda lambda = context.getBean(Lambda.class);
//		lambda.init();
//		logger.info("********* CommandRunner: About to check if runner is null ");
//		Thread.sleep(1000);
		
//		Assert.notNull(this.runner, "Runner has not been Autowired");
//		
//		logger.info("runner is null?" + (runner == null));
//	}
	
	/**
	 * Execute an arbitrary command from a Test Assertion Implementation. 
	 * 
	 * @param keyword The name of the keyword (method) to call.
	 * @param params The set of parameters to pass in.
	 */
	public void executeCommand(String keyword, Object...params) {
		logger.info("Running command: " + keyword);
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
	public void executeCommand(String keyword) {
		TestCommand command = new TestCommand(keyword);
		logger.info("Running command: " + keyword);
		runner.runCommand(command);
	}
}