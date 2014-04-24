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

/**
 * Test Class generator using Javassist to create the classes
 * on the fly.
 * 
 * This class is responsible for creating objects of type <code>Test</code>.
 * 
 * Note: This class is NOT thread safe and assumes only 1 concurrent caller.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 */
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.api.Test;
import au.com.onegeek.lambda.api.TestCommand;
import au.com.onegeek.lambda.api.exception.CannotCreateDataProviderException;
import au.com.onegeek.lambda.api.exception.CannotCreateTestClassException;
import au.com.onegeek.lambda.api.exception.CannotModifyTestMethodException;
import au.com.onegeek.lambda.api.exception.VariableNotFoundException;

public class JavassistTestBuilderImpl {
	private static final Logger logger = LoggerFactory.getLogger(JavassistTestBuilderImpl.class);
	
	/**
	 * Current Test Class in the making.
	 */
	private CtClass ctClass;
	
	/**
	 * Class Pool.
	 */
	private ClassPool classPool;
	
	/**
	 * Pointer to the current method.
	 */
	private CtMethod ctMethod;

	/**
	 * Test data set.
	 */
	private List<Map<String, Object>> dataSet;
	
	/**
	 * Ordered list of variables used in the current method
	 * providing easy lookup.
	 * 
	 * Used to create <code>@DataProvider</code> annotations for test methods.
	 */
	private LinkedHashMap<String, Object> methodVariableToParameterMap = new LinkedHashMap<String, Object>();

	/**
	 * The ordered set of types for the test methods. Must match up with the DataProvider Object[][].
	 */
	private List<Class> signatureTypes = new LinkedList<Class>();
	
	/**
	 * In Progress statements to add to test method. To be added after signature is created.
	 */
	private List<String> commandsToAdd = new LinkedList<String>();
	
	/**
	 * The compiled class object. The compilation can only happen once.
	 * 
	 * TODO: Ensure the compilation can only happen once OR make it so it can be re-generated
	 */
	private Class<Test> clazz;
	
	/**
	 * Singleton instance.
	 */
	private static JavassistTestBuilderImpl instance;
	
	/**
	 * Private constructor for Singleton implementation.
	 */
	private JavassistTestBuilderImpl() {
		
	}
	
	/**
	 * Get an instance of the Builder.
	 * 
	 * @return
	 */
	public static JavassistTestBuilderImpl getInstance() {
		if (instance == null) {
			instance = new JavassistTestBuilderImpl();
		}
		return instance;
	}
	
	/**
	 * Create a stub Test class with a test method attached.
	 * 
	 * @param testClassName Name of class to create - this could be the name of the tab in the spreadsheet, for example.
	 * @return
	 * @throws CannotCompileException
	 * @throws NotFoundException 
	 * @throws CannotCreateTestClassException 
	 */
	public void makeTestClass(String testClassName, List<Map<String, Object>> dataSet) throws CannotCreateTestClassException {
		this.dataSet = dataSet;		
		this.classPool = ClassPool.getDefault();		
		classPool.importPackage("org.testng");
		classPool.importPackage("org.testng.annotations");
		classPool.importPackage("au.com.onegeek.lambda.core");
		classPool.importPackage("au.com.onegeek.lambda.api");
		classPool.importPackage("au.com.onegeek.lambda.exception");
		classPool.importPackage("org.slf4j");
		
		// Create the class container to add methods to
		this.ctClass = classPool.makeClass(testClassName);
		this.ctClass.stopPruning(true);

		try {
			this.ctClass.setSuperclass(this.classPool.get("au.com.onegeek.lambda.core.Test"));			
		} catch (CannotCompileException e) {
			throw new CannotCreateTestClassException("Cannot create Test class '" + testClassName + "' as class au.com.onegeek.lambda.core.Test' as it could not be compiled.");
		} catch (NotFoundException e) {
			throw new CannotCreateTestClassException("Cannot create Test class '" + testClassName + "' as class 'au.com.onegeek.lambda.core.Test' could not be found.");
		}
		
		// Inject in field params
		CtField f;
		try {
			f = CtField.make("private static final Logger logger = LoggerFactory.getLogger(" + testClassName + ".class);", this.ctClass);
		} catch (CannotCompileException e) {
			throw new CannotCreateTestClassException("Cannot create logger for Test class '" + testClassName + "' due to nested compile exception. Embedded exception: " + e.getMessage());
		}
		try {
			this.ctClass.addField(f);
		} catch (CannotCompileException e) {
			throw new CannotCreateTestClassException("Cannot create logger for Test class '" + testClassName + "' due to nested compile exception. Embedded exception: " + e.getMessage());
		}
	}
	
	private void completeMethod() throws CannotModifyTestMethodException, CannotCompileException, NotFoundException {
		logger.debug("completing any in flight methods...");
		if (this.ctMethod != null) {
			logger.debug("completing in flight method: " + this.ctMethod.getName());
			
			// Update method signature
			for (Class type: signatureTypes) {
				// Signature MUST be the same across ALL tests due to limitations in the
				// library (needs to call a static method on THIS class to retrieve data)
				try {
					// Dynamically find type, import into classpool and append the param into the method signature
					logger.debug("Adding parameter of type " + type + " to method signature: " + this.ctMethod.getName());
					this.classPool.importPackage(type.getPackage().getName());
					this.ctMethod.addParameter(this.classPool.get(type.getName()));
				} catch (NotFoundException e) {
					throw new CannotModifyTestMethodException("Cannot modify the method signature because the parameter type could not be found. Embedded exception is: " + e.getMessage());
				} catch (CannotCompileException e) {
					throw new CannotModifyTestMethodException("Cannot modify the method signature because the method could not be compiled. Embedded exception is: " + e.getMessage());
				}
			}
			
			// Bring other method signatures up to speed with any new params
			// Note: This can't be done at the end of all methods as the method bodies
			//       may refer to signature variables which don't yet exist 
			for (CtMethod m: this.ctClass.getMethods()) {
				if (m.getName().startsWith("test") && !m.getName().contains("DataProvider")) {
					// Add any additional parameters to signature if missing
					int count = m.getParameterTypes().length;
					logger.debug("Method '" + m.getName() + "' has " + count + " parameters");
					logger.debug("Signature map has " + this.signatureTypes.size() + " types. Need to add additional " + (this.signatureTypes.size() - count) + " params");
					
					
					for (int i = count; i < this.signatureTypes.size(); i++) {
							Class type = this.signatureTypes.get(i);
							logger.debug("Adding parameter of type " + type + " to previous method signature to ensure consistent method signature: " + this.ctMethod.getName());
							this.classPool.importPackage(type.getPackage().getName());
							m.addParameter(this.classPool.get(type.getName()));
					}
				}
			}			
			
			// Add commands to Test
			for (String commandString: this.commandsToAdd) {
				logger.debug("Adding command to method with body: {}", commandString);
				this.ctMethod.insertAfter(commandString);
			}
			
			// Reset temp vars - dataSet, ctMethod ...
			this.ctMethod = null;
			//this.methodVariableToParameterMap = new LinkedHashMap<String, Object>();			
			// Reset commands for next method
			this.commandsToAdd = new LinkedList<String>();
		}
	}
	
	/**
	 * Create a new method on the Test Class with the given name.
	 * 
	 * TODO: Use CannotAddTestMethodException instead of others
	 * 
	 * @param testMethodName The name of the method
	 * @return
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 * @throws VariableNotFoundException 
	 * @throws CannotModifyTestMethodException 
	 */
	public void addTest(String testMethodName) throws CannotCompileException, NotFoundException, VariableNotFoundException, CannotModifyTestMethodException {
		// Close off previous Method
		this.completeMethod();
		
		// Create an @Test annotation on the methods to fire
		AnnotationsAttribute attr = new AnnotationsAttribute(this.ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation("org.testng.annotations.Test", this.ctClass.getClassFile().getConstPool());
		annot.addMemberValue("dataProvider", new StringMemberValue(testMethodName + "DataProvider",  this.ctClass.getClassFile().getConstPool()));
		attr.addAnnotation(annot);

		CtMethod testMethod = CtNewMethod.make(
				"public void " + testMethodName + " () { " +
						"logger.debug(\"about to show args\");" +
						"for (int i=0; i<$args.length; i++) {" +
						"logger.debug(\"Method param value: \" + $args[i]);}" + 
						"logger.debug(\"Running dynamic TestCase: " + testMethodName + "\");" +
						"}",
				this.ctClass);
		
		testMethod.getMethodInfo().addAttribute(attr); 
		this.ctClass.getClassFile().addAttribute(attr);
		this.ctClass.addMethod(testMethod);	
		this.ctMethod = testMethod;
	}

	/**
	 * Appends the code to execute a <code>TestCommand</code> into the last method added to the class. 
	 *
	 * @param command The command to execute as part of the test case containing unparsed variables
	 * @throws CannotModifyTestMethodException
	 * @throws CannotCompileException
	 * @throws VariableNotFoundException
	 */
	public void appendTestToLastMethod(TestCommand command) throws CannotModifyTestMethodException, CannotCompileException, VariableNotFoundException {
		// Extract variables, update method signature, Escape vars in Strings
		this.handleVariablesInTestCommand(command);		

		// Add command to Test class
		String commandString = this.implodeCommandString(command);

		// Add tests to method en-mass
		logger.debug("Adding command body: '" + commandString + "' to list of commands to append to test method body");
		this.commandsToAdd.add(commandString);
	}
	
	/**
	 * Find variables referenced from within a command in order to dynamically update the
	 * test method's signature, as well as escape the variable for use within the <code>implodeCommandString</code>
	 * so that it references a local variable defined as a parameter in the method.
	 * 
	 * @param command The <code>TestCommand</code> to assess.
	 * @throws VariableNotFoundException
	 * @throws CannotModifyTestMethodException
	 */
	protected void handleVariablesInTestCommand(TestCommand command) throws VariableNotFoundException, CannotModifyTestMethodException {
		
		if (command.getParameters() == null) {
			return;
		}
		Object[] params = new Object[command.getParameters().length];
		
		int i = 0;
		for (Object obj : command.getParameters()) {
			params[i] = obj;
			
			// Only support String variable replacement
			if (obj instanceof String) {
				String param = (String) obj;
				
				// Find all vars in string
		        String regex = "(\\$[a-zA-Z0-9]*)";
		        Pattern p = Pattern.compile(regex);
		        Matcher m = p.matcher(param);

		        if (m.groupCount() > 0) {
			        while (m.find()) {
		        		String match = m.group();
		        		logger.debug("variable reference found from string '{}': {}, adding to map of vars to add?", param, match.substring(1));
		        			// Determine parameter type from a provided data set		        			
		        			@SuppressWarnings("rawtypes")
							Class type = null;
		        			
		            		try {		            			
		            			type = this.dataSet.get(0).get(match.substring(1)).getClass();
		            		} catch (NullPointerException e) {
		            			throw new VariableNotFoundException();
		            		}		        			
		        			
		            		if (this.methodVariableToParameterMap.get(match.substring(1)) != null) {
		            			logger.debug("Variable'{}' exists in map, ignoring...", match);
		            		} else {		            			
		            			logger.debug("Variable'{}' DOES NOT exist in map, adding...", match);
		            			// Add variable to map
		            			this.methodVariableToParameterMap.put(match.substring(1), type);
		            			this.signatureTypes.add(type);
		            				        			
		            			// Previously updated method signature here, but have had to refactor as
		            			// method signatures can't differ across tests
		            		}
		        			
		        			// Substitute variables - can't use named parameters as Javassist doesn't support them in methods (you can understand why)
		        			// Substitute variables with 	[0...n]
		        			int position = 0;
		        			for (String key : this.methodVariableToParameterMap.keySet()) {
		        				if (key.equals(match)) {
		        					break;
		        				}
		        				position++;
		        			}
		        			
		        			// TODO: Why does the next line NOT work but the following does? Interesting...
		        			//param = param.replace(match, "\" + $args[" + position + "] + \"");
		        			param = param.replace(match, "\" + $" + position + " + \"");
		        		
		        	}
			        // Update param
			        params[i] = param;
		        }
			}
			i++;
		}
		command.setParameters(params);
	}

	/**
	 * Add a <code>DataProvider</code> to the last method so that it will 
	 * 
	 * Create a new field, and initialize it with the array of data from the variables listed in the variabes array
	 * Uses the {@link JavassistTestBuilderImpl#getData(Object, String[], Object[]) method of this class}
	 * 
	 * @throws CannotCompileException
	 * @throws CannotCreateDataProviderException
	 * @throws NotFoundException
	 */
	public void addDataProvider() throws CannotCompileException, CannotCreateDataProviderException, NotFoundException {
		
		String fieldName = "_" + this.ctMethod.getName() + "DataProvider";
		
		// Loop through the dataset, converting the variables into an Object[][]

		// Create an @DataProvider annotation on the method
		AnnotationsAttribute attr = new AnnotationsAttribute(this.ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation("org.testng.annotations.DataProvider", this.ctClass.getClassFile().getConstPool());
		annot.addMemberValue("name", new StringMemberValue(this.ctMethod.getName() + "DataProvider",  this.ctClass.getClassFile().getConstPool()));
		attr.addAnnotation(annot);
		
		// Create class field variable because [][] are not supported by Javassist
		String[] variables = new String[methodVariableToParameterMap.size()];
		methodVariableToParameterMap.keySet().toArray(variables);
		
		logger.debug("Configured to initialise array from class: " + this.getClass().getName() + ", method getData()");
		
		Initializer init = CtField.Initializer.byCallWithParams(this.classPool.get(this.getClass().getName()), "getData", variables);
		CtField field = CtField.make("private Object[][] " + fieldName + ";", ctClass);
	
		this.ctClass.addField(field, init);
		
		
		String methodString = "public Object[][] " + this.ctMethod.getName() + "DataProvider () { ";
		if (logger.isDebugEnabled()) {
			// Debugging statements not useful to functionality of program but useful when, well, debugging :)
			methodString += "logger.debug(\"Running dynamic DataProvider: '" + this.ctMethod.getName() + "DataProvider'\");" +
							"JavassistTestBuilderImpl builder = JavassistTestBuilderImpl.getInstance();"+
							"int nrCols = builder.getMethodVariableToParameterMap().size();" +
							"int nrRows = builder.getDataSet().size();"	+
							"String dataRepresentation = \"return new Object[][]{\\\n\";" + 
							"for (int i = 0; i < nrRows; i++) {"+
							"	if (i != 0) {				  " +
							"		dataRepresentation += \",\\\n\";" +
							"	}" +
							"	dataRepresentation += \"\\\t{\";"+
							"	for (int j = 0; j < nrCols; j++) {"+
							"		if (j != 0) {"+
							"			dataRepresentation += \",\";"+					
							"		}"+
							"		dataRepresentation += \" + data[i][j] + \";" +					
							"	}" +
							"	dataRepresentation += \"}\";" +
							"}"+
							"dataRepresentation += \"\\\n};\";"+
							"logger.debug(\"Returning Object[][] that looks like...: \\\n\" + dataRepresentation);" +														
							"System.out.println(this._" + this.ctMethod.getName() + "DataProvider.toString());";
		}
		methodString += 	"return this._" + this.ctMethod.getName() + "DataProvider; }";
		logger.debug("Created DataProvider with following method body: " + methodString);
		CtMethod testMethod = CtNewMethod.make(methodString, this.ctClass);
		testMethod.getMethodInfo().addAttribute(attr);
		this.ctClass.getClassFile().addAttribute(attr);
		this.ctClass.addMethod(testMethod);
	}
		
	/**
	 * Generate the <code>DataProvider</code>'s dataset for it's provider method.
	 * 
	 * This method will be invoked when the Test object is created, so it's
	 * necessary that both the dataSet it's referring to is consistent and
	 * the variables names don't change.
	 * 
	 * NOTE: This has the side-effect that only 1 data provider can exist! So
	 * all methods in the test must have the same signature.
	 * 
	 * @param obj  The current Javassist compile-in-progress Test class.
	 * @param keys The set of variable names in order to put into the data set.
	 * @param args The args passed to the constructor of the object.
	 * @return     The data set for the Data Provider's method return.
	 * @throws CannotCreateDataProviderException 
	 */
	public static Object[][] getData(Object obj, String[] keys, Object[] args) throws CannotCreateDataProviderException {
		
		JavassistTestBuilderImpl builder = JavassistTestBuilderImpl.getInstance();
		
		// Loop through the dataset, converting the variables into an Object[][]
		int nrCols = builder.getMethodVariableToParameterMap().size();
		int nrRows = builder.getDataSet().size();
		
		Object[][] data = new Object[nrRows][nrCols];
			
		int row = 0;
		int col = 0;		
		logger.debug("Data set rows: " +  nrRows + ", cols: " + nrCols);
		for (row = 0; row < nrRows; row++) {
			Map<String, Object> dataSet = builder.getDataSet().get(row);			
		logger.debug("Data set iteration: " +  row);
			
			col = 0;		
			for (Object key : builder.getMethodVariableToParameterMap().keySet()) {
				try {
					logger.debug("fetching variable from map: " + key);
					data[row][col] = dataSet.get(key);
					logger.debug("fetched value for variable from map: " + dataSet.get(key));
					col++;
				} catch(NullPointerException e) {
					throw new CannotCreateDataProviderException("Cannot find a value for key '" + key + "' in dataset number: " + row);
				}
			}
		}
		
 	 	// Create the @DataProvider method and give it a name that matches the DataProvider created in addTest <methodName>DataProvider
		if (logger.isDebugEnabled()) {
			String dataRepresentation = "return new Object[][]{\n";
			for (int i = 0; i < nrRows; i++) {
				if (i != 0) {
					dataRepresentation += ",\n";
				}
				dataRepresentation += "\t{";
				for (int j = 0; j < nrCols; j++) {
					if (j != 0) {
						dataRepresentation += ",";					
					}
					if (data[i][j] == null) {
						throw new CannotCreateDataProviderException("Null value found during Object[][] creation. Dump of dataRepresentation so far \n:" + dataRepresentation);
					}
					dataRepresentation += "\"" + data[i][j] + "\"";					
				}
				dataRepresentation += "}";
			}
			dataRepresentation += "\n};";
			
			logger.debug("DataProvider object[][] created, and it looks like...: \n" + dataRepresentation);
		
			for (String key : keys) {
				logger.debug("Key provided: " + key);
			}
			for (Object arg : args) {
				logger.debug("arg provided: " + arg);
			}
		}
		return data;
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
		
		// Check type of parameter, Strings could also be longs, ints, decimals etc.
		
		if (command.getParameters().length > 0) {
			returnString += "executeCommand(\"" + command.getCommand() + "\", new Object[]{";
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
			
			returnString += "\"" + param.toString() + "\"";
			start = false;
		}

		return returnString + "});\n";
	}
	
	public CtClass getCtClass() {
		return this.ctClass;
	}
	
	/**
	 * Get the generated Class file for the new class.
	 * 
	 * TODO: Consider allowing freezing\defrosting so that it can still
	 *       be modified  at runtime.
	 * 
	 * @return
	 * @throws CannotCreateTestClassException
	 * @throws CannotModifyTestMethodException 
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public Class<Test> getCreatedClass() throws CannotCreateTestClassException, CannotModifyTestMethodException, CannotCompileException, NotFoundException {	
		this.completeMethod();		
		
		if (this.clazz != null) {
			return this.clazz;
		}
		try {
			this.clazz = this.ctClass.toClass();
			return clazz;
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new CannotCreateTestClassException("Cannot compile test class using Jassist. Embedded exception: " + e.getMessage());
		} finally {
			// Allow class re-creation later on (i.e. if tests are run again)
			ctClass.defrost();
		}
	}
	
	/**
	 * Returns an instantiated Test class object. 
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws CannotCompileException 
	 * @throws NotFoundException 
	 */
	public Test getObjectInstance() throws InstantiationException, IllegalAccessException, CannotCompileException, NotFoundException {
		Test obj;
		try {
			obj = (Test) this.getCreatedClass().newInstance();
		} catch (CannotModifyTestMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
			throw new InstantiationException("Unable to create test class object. Embedded exception: " + e.getMessage());
		} catch (CannotCreateTestClassException e) {
			throw new InstantiationException("Unable to create test class object. Embedded exception: " + e.getMessage());
		}
		return obj;		
	}

	public ClassPool getClassPool() {
		return classPool;
	}

	public CtMethod getCtMethod() {
		return ctMethod;
	}

	public List<Map<String, Object>> getDataSet() {
		return dataSet;
	}

	public LinkedHashMap<String, Object> getMethodVariableToParameterMap() {
		return methodVariableToParameterMap;
	}

	public void setCtClass(CtClass ctClass) {
		this.ctClass = ctClass;
	}
}