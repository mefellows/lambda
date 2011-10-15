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
}
