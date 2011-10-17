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