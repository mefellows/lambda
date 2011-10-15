package au.com.onegeek.lambda.core;

/**
 * Dynamic Class generator factory.
 * 
 * This class is responsible for creating objects of type <Test>
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 */
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.core.exception.CannotCreateTestClassException;
import au.com.onegeek.lambda.core.exception.CannotCreateTestMethodException;
import au.com.onegeek.lambda.core.exception.CannotModifyTestMethodException;
import au.com.onegeek.lambda.core.exception.VariableNotFoundException;

public class JassistTestBuilderImpl {// implements ITestBuilder {
	private static final Logger logger = LoggerFactory.getLogger(JassistTestBuilderImpl.class);
	
	/** Parameter types for call with no parameters. */
	private static final CtClass[] NO_ARGS = {};

	/** Parameter types for call with single int value. */
	private static final CtClass[] INT_ARGS = { CtClass.intType };
	
	private CtClass _ctClass;
	
	private ClassPool _classPool;

	/**
	 * Test data set.
	 */
	private List<Map<String, Object>> _dataSet;
	
	/**
	 * Create a stub Test class with a test method attached.
	 * 
	 * @param testClassName Name of class to create - this could be the name of the tab in the spreadsheet, for example.
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 */
	public JassistTestBuilderImpl (String testClassName, List<Map<String, Object>> dataSet) throws CannotCompileException, NotFoundException {
		this._dataSet = dataSet;
		
		this._classPool = ClassPool.getDefault();		
		_classPool.importPackage("org.testng");
		_classPool.importPackage("org.testng.annotations");
		_classPool.importPackage("au.com.onegeek.lambda.core");
		_classPool.importPackage("org.slf4j");
		
		// Create the class container to add methods to
		this._ctClass = _classPool.makeClass(testClassName);
		try {
			this._ctClass.setSuperclass(this._classPool.get("au.com.onegeek.lambda.core.Test"));			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Inject in field params
		CtField f = CtField.make("private static final Logger logger = LoggerFactory.getLogger(" + testClassName + ".class);", this._ctClass);
		this._ctClass.addField(f);
		
		// TODO: Should this class implement an interface of sorts or inherit from an Abstract class for use later on?
		// = IDynamicTest?
		
		// TODO: Inject in class members: Selenium, assertions
		//        Or, can Spring do this for us?
	}
	
	/**
	 * Takes a TestCommand and creates a method call out of it.
	 * 
	 * Note: Javassist does not support varargs hence the explicit Array notation.
	 * 
	 * @param command
	 * @return
	 * @throws VariableNotFoundException 
	 */
	private String implodeCommandString(TestCommand command) throws VariableNotFoundException {
		String returnString = "";
		
		if (command.getParameters().length > 0) {
			returnString += "executeCommand(\"" + command.getCommand() + "\", new String[]{";
		} else {
			// No arguments? Piece of cake.
			return "executeCommand(\"" + command.getCommand() + "\");\n";			
		}

		boolean start = true;
		for (Object param : command.getParameters()) {
			
			// Don't add ',' at the start of the array
			if (!start) {
				returnString += ",";
			}
			
			//returnString += "\"" + this.substituteVariable(param.toString(), this._dataSet) + "\"";
			
			// @TODO: VARIABLES GO HERE!!!
			
			returnString += "\"" + param.toString() + "\"";
			start = false;
		}

		return returnString + "});\n";
	}
	
	public CtClass getCtClass() {
		return this._ctClass;
	}
	
	public Class<Test> getCreatedClass() throws CannotCreateTestClassException {
		try {
			Class<Test> clazz = this._ctClass.toClass();
			return clazz;
		} catch (CannotCompileException e) {
			throw new CannotCreateTestClassException("Cannot compile test class using Jassist. Embedded exception: " + e.getMessage());
		}
	}
	
	public Test getClassInstance() throws InstantiationException, IllegalAccessException {
		Test obj;
		try {
			obj = (Test) this.getCreatedClass().newInstance();
		} catch (CannotCreateTestClassException e) {
			throw new InstantiationException("Unable to create test class object. Embedded exception: " + e.getMessage());
		}
		return obj;		
	}
	
	/**
	 * Create a new method on the Test Class with the given name.
	 * 
	 * @param testMethodName The name of the method
	 * @return
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 * @throws VariableNotFoundException 
	 */
	public CtMethod addTest(String testMethodName, List<TestCommand> commands) throws CannotCompileException, NotFoundException, VariableNotFoundException {
		// Create an @Test annotation on the methods to fire
		AnnotationsAttribute attr = new AnnotationsAttribute(this._ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation("org.testng.annotations.Test", this._ctClass.getClassFile().getConstPool());
		attr.addAnnotation(annot);

		logger.debug("Superclass: " + this._ctClass.getSuperclass());
		
		// Create the test method 
		String testBody = "";
		
		for (TestCommand command : commands) {
			testBody += this.implodeCommandString(command);
		}
		
		// Extract variable names in order to use in constructor?
		
		// Create local @DataProvider method?
		
		
		
		logger.debug("Test Method body generated: \n" + testBody);
		
		
		// @TODO: How to parameterize methods to take advantage of the @DataProvider annotations?
		
		CtMethod testMethod = CtNewMethod.make(
				"public void " + testMethodName + " () { " +
						"" +
						"logger.debug(\"Running dynamic TestCase: " + testMethodName + "\");" +						
						testBody +
						"}",
				this._ctClass);
		testMethod.getMethodInfo().addAttribute(attr); 
		this._ctClass.getClassFile().addAttribute(attr);
		this._ctClass.addMethod(testMethod);
	
		return testMethod;
	}
	
	
	public void addDataProviderMethod(String testCase, Map<?, ?> data) {
		
	}
	
	/**
	 * Substitute variable references in keyword calls with data from the current dataset.
	 * 
	 * @param value The string to check for a variable and replace.
	 * @param map   The set of variables to choose from.
	 * @return      The string with any variable's substituted with the source value.
	 * @throws VariableNotFoundException If variable referenced but none found, throws.
	 */
	private String substituteVariable(String value, Map<String, String> map) throws VariableNotFoundException {
        String regex = "(\\$[a-zA-Z]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);

        if (m.groupCount() > 0) {
	        while (m.find()) {
        		String match = m.group();
        		logger.debug("variable reference found from string '" + value + "': " + match.substring(1) + ", replacing with current value from map: " + map.get(match.substring(1)));			        		

        		// $variable style vars
        		try {
        			value = value.replace(match, map.get(match.substring(1)));
        		} catch (NullPointerException e) {
        			throw new VariableNotFoundException();
        		}
        	}
        }
		return value; 		
	}
}