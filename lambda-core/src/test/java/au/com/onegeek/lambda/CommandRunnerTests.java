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