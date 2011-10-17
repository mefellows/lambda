package au.com.onegeek.lambda.core;

import static org.testng.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.tests.SeleniumAssertions;
import au.com.onegeek.lambda.tests.TestWebDriver;

/**
 * 
 * Command Runner supports pluggable keyword-driven command to extend the
 * assertion library outside of Lambda.
 * 
 * This class is an implementation of both the Mediator and Singleton design patterns.
 * 
 * @TODO: Need to work out how to dynamically add classes to registry. Also, should registry
 * 		  store methods to register or should we use reflection dynamically to determine if method exists?
 * 
 * @author mfellows
 *
 */
public class CommandRunner {
	private static final Logger logger = LoggerFactory.getLogger(CommandRunner.class);
	
	// Use annotations instead of interfaces -> any existing class may be used :)
	
	/**
	 * Singleton instance.
	 */
	private static CommandRunner _instance;
		
	/**
	 * List of classes providing assertion capabilities.
	 */
	protected List<Object> assertionProviders;

	private WebDriver driver;

	private WebDriverBackedSelenium selenium;
	
	
	private CommandRunner() {
		// Create the browser driver
		try {
			logger.debug("Creating firefox Driver.");
			this.driver = TestWebDriver.getDriver("firefox");
			driver.get("http://www.google.com.au");
		} catch (Exception e) {
			logger.debug("Could not create driver because of: "
					+ e.getMessage() + "\n Exiting now...");
			System.exit(1);
		}
		
		// Start the Selenium Server
		try {
			this.selenium = new WebDriverBackedSelenium(this.driver, "http://www.google.com.au");
			this.selenium.open("http://www.google.com.au/");
		} catch (Exception e) {
			logger.debug("Could not start selenium or the server because: " + e.getMessage());
			System.exit(1);
		}
		
		assertionProviders = new ArrayList<Object>();
		assertionProviders.add(selenium);
		assertionProviders.add(new SeleniumAssertions(selenium));		
	}
	
	/**
	 * Find the assertion class implementor and invoke the call using reflection.
	 * 
	 * @param testCommand
	 */
	public void runCommand(TestCommand testCommand) {
		String keyword = testCommand.getCommand();
		        
        // Create Test Case w\
		Object object = null;
        Method method = null;
        
    	// Determine argument types
    	Class[] argTypes = new Class[testCommand.getParameters().length];    	
    	int k = 0;
    	for (k = 0; k < testCommand.getParameters().length; k++) {
    		argTypes[k] = String[].class;
    		argTypes[k] = testCommand.getParameters()[k].getClass();
    		logger.debug("Argument class: " + argTypes[k]);
    	}

        // Find the implementing class of the method
    	for(Object provider : assertionProviders) {
	    	try {
	    		method = provider.getClass().getMethod(keyword, argTypes);
	    		object = provider;
	        } catch (SecurityException e) {
	        	logger.debug("Not allowed to call method " + keyword + " from Provider <" + provider.getClass().getName() + "> ");
	        } catch (NoSuchMethodException e) {
	        	logger.debug("Method: " + keyword + " not found in Provider <" + provider.getClass().getName() + "> ");
	        }
    	}
    	
    	if (object == null) {
    		fail("Cannot find a provider of method: '" + keyword + "'");
    	} else {
    		logger.debug("Found source object for method, object: <" + object.toString() + ">");
    	}
	    	
    	// Invoke method
        Object result = "not set";
        try {
        	result = method.invoke(object, testCommand.getParameters());
        } catch (IllegalArgumentException e) {
        	fail("Method '" + keyword + "' illegal argument exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
        	fail("Method '" + keyword + "' illegal acess exception: " + e.getMessage());
        } catch (InvocationTargetException e) {
        	// This is usually a failed Assertion from an AssertionProvider
        	e.printStackTrace();
        	//fail("Method '" + keyword + "' InvocationTarget exception: " + e.getMessage());        	
        } catch (AssertionError e) {
        	logger.error("Assertion fail: " + e.getMessage());
        }
	    
    	logger.debug("output from reflected method: " + result);
	}
	
	/**
	 * Get a CommandRunner instance.
	 * 
	 * @return
	 */
	public static CommandRunner getInstance() {
		if (_instance != null) {
			return _instance;
		}
		
		_instance = new CommandRunner();
		return _instance;
	}
}
