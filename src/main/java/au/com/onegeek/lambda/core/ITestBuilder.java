package au.com.onegeek.lambda.core;

import java.util.List;
import java.util.Map;

import au.com.onegeek.lambda.core.exception.CannotCreateTestClassException;
import au.com.onegeek.lambda.core.exception.CannotCreateTestMethodException;
import au.com.onegeek.lambda.core.exception.CannotModifyTestMethodException;

/**
 * Interface for TestBuilder implementations.
 * 
 * Facilitates the following algorithm:
 * 
 * 	// Test class creation algorithm
 *	//
 *	
 *	// 1. Create Class
 *	
 *	// 2. Create Method	
 *	// Loop commands in method
 *	// 2a - For each test command, append command to previously created method
 *	// 2c - Add any variables found to _ordered list_ of variables (for data provider)
 *	// 2d - Update method signature to include that param
 *	
 *	
 *	(Hint:
 *		method.insertParameter(this._classPool.get("String")); // Inserts at start of method args
 *		LinkedList list = new LinkedList();					   // Can insert variable names here easily
 *		list.addFirst("variableFoo");
 *	)
 *	
 *	
 *	// 3. Complete Method
 *	// 3a - Create data provider for method based on number variables in current map
 *	// 3b - Annotate with a named data provider (if it needs one)
 *	// 
 * 
 * Note: Implementation of the Builder design pattern
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public interface ITestBuilder {

	public ITestBuilder makeTest(String testClassName, List<Map<String, Object>> dataSet) throws CannotCreateTestClassException;
	
	public boolean addTestMethod(String testMethodName) throws CannotCreateTestMethodException;
	
	public boolean appendTestToLastMethod(TestCommand command) throws CannotModifyTestMethodException;
	
	public boolean appendTestToMethodByName(String methodName, TestCommand command) throws CannotModifyTestMethodException;
	
	public Class<Test> getCreatedClass() throws CannotCreateTestClassException;
	
	public Test getClassInstance() throws InstantiationException, IllegalAccessException;
}