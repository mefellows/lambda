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
package au.com.onegeek.lambda.api;

import java.util.List;
import java.util.Map;

import au.com.onegeek.lambda.api.exception.CannotCreateTestClassException;
import au.com.onegeek.lambda.api.exception.CannotCreateTestMethodException;
import au.com.onegeek.lambda.api.exception.CannotModifyTestMethodException;

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
public interface TestBuilder {

	public TestBuilder makeTestClass(String testClassName, List<Map<String, Object>> dataSet) throws CannotCreateTestClassException;
	
	public boolean addTestMethod(String testMethodName) throws CannotCreateTestMethodException;
	
	public boolean addTestMethod(String testMethodName, List<TestCommand> commands) throws CannotCreateTestMethodException;
	
	public boolean appendTestToLastMethod(TestCommand command) throws CannotModifyTestMethodException;
	
	public boolean appendTestToMethodByName(String methodName, TestCommand command) throws CannotModifyTestMethodException;

	public boolean createTestMethodDataProvider();
	
	public boolean createTestMethodDataProviderByName(String methodName);
	
	public Class<Test> getCreatedClass() throws CannotCreateTestClassException;
	
	public Test getClassInstance() throws InstantiationException, IllegalAccessException;
}
