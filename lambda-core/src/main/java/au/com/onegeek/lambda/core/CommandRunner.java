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
package au.com.onegeek.lambda.core;

import static org.testng.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.TestCommand;

/**
 * 
 * Command Runner supports pluggable keyword-driven commands to extend the
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
@Configurable
//@Component
public class CommandRunner {
	private static final Logger logger = LoggerFactory.getLogger(CommandRunner.class);
	
	// TODO: Use annotations instead of interfaces -> any existing class may be used :)

	@Autowired
	private Lambda lambda;

//	private CommandRunner() {
		private void init() {		
			logger.info("Lambda is null?" + (lambda == null));
	}

	
	/**
	 * Find the assertion class implementor and invoke the call using reflection.
	 * 
	 * @param testCommand
	 */
	public void runCommand(TestCommand testCommand) {
		String keyword = testCommand.getCommand();
		logger.debug("Looking for method with name: " + testCommand.getCommand());
		        
        // Create Test Case w\
		Object object = null;
        Method method = null;
        
    	// Determine argument types
    	Class[] argTypes = new Class[testCommand.getParameters().length];    	
    	int k = 0;
    	for (k = 0; k < testCommand.getParameters().length; k++) {

    		// Check if there is a number hidden in the args list
    		// Currently, although most of the Lambda implementation supports Object params
    		// The conversion into a class (JavassistTestBuilderImpl) turns them back into 
    		// Strings. 
    		
    		// TODO: set locale?
           /* NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            Number number;
            try {
				number = format.parse((String) testCommand.getParameters()[k]);
				testCommand.getParameters()[k] = number;
				
				// Looks like it ALWAYS comes out of here as long
				// Apache commons to the rescue? Surely there is a library that does this better
				// TODO: fix this
				if (number.getClass().getSimpleName().equalsIgnoreCase("long")) {
					argTypes[k] = long.class;					
				}
				else if (number.getClass().getSimpleName().equalsIgnoreCase("double")) {
					argTypes[k] = double.class;					
				}
				else if (number.getClass().getSimpleName().equalsIgnoreCase("float")) {
					argTypes[k] = float.class;					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}*/
    		
//    		if (argTypes[k] == null) {
    			argTypes[k] = testCommand.getParameters()[k].getClass();
//    		}
    		logger.debug("Argument value: " + testCommand.getParameters()[k]);
    		logger.debug("Argument class: " + argTypes[k]);
    	}

    	logger.info("looking for providers...");
    	
        // Find the implementing class of the method
    	for(Object provider : this.lambda.getAssertionProviders()) {
	    	try {
	    		logger.info("getting method....");
	    		method = provider.getClass().getMethod(keyword, argTypes);
	    		logger.info("got one!");
	    		object = provider;
	        } catch (SecurityException e) {
	        	logger.debug("Not allowed to call method " + keyword + " from Provider <" + provider.getClass().getName() + "> ");
	        } catch (NoSuchMethodException e) {
	        	for (Object object2 : argTypes) {	        		
	        		logger.debug("Method: " + keyword + ": Arg type: " + object2);	        		
	        	}
	        	logger.debug("Method: " + keyword + " not found in Provider <" + provider.getClass().getName() + "> ");
	        }
    	}
    	
    	logger.info("Checknig if we found a provider");
    	
    	if (object == null) {
    		logger.error("cannot find provider of method " + keyword);
    		fail("Cannot find a provider of method: '" + keyword + "'");
    	} else {
    		logger.debug("Found source object for method, object: <" + object.toString() + ">");
    	}
	    	
    	// Invoke method
        Object result = "not set";
        try {
        	logger.info("Invoking method '" + keyword + "' on object");
        	result = method.invoke(object, testCommand.getParameters());
        } catch (IllegalArgumentException e) {
        	fail("Method '" + keyword + "' illegal argument exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
        	fail("Method '" + keyword + "' illegal acess exception: " + e.getMessage());
        } catch (InvocationTargetException e) {
        	// This is usually a failed Assertion from an AssertionProvider
        	logger.debug("InvocationTargetException, usually a failed assertion: ");
        	if (logger.isErrorEnabled()) {
        		e.printStackTrace();
        	}
        	fail("Method '" + keyword + "' InvocationTarget exception: " + e.getMessage());
        	
        	// TODO: remove\handle this better
        	e.printStackTrace();
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
//	public static CommandRunner getInstance() {
//		if (_instance != null) {
//			return _instance;
//		}
//		
//		_instance = new CommandRunner();
//		return _instance;
//	}
}