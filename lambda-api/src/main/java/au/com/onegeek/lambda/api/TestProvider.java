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

/**
 * Test Parser Interface.
 * 
 * Any Input type (i.e. xlsx, xls, xml...) needs to match a parser. 
 * The matching process goes by:
 * 		1. name i.e. in the case of .xlsx file, the parser should be named XlsxParser
 * 		2. Registry lookup matching the 'extension' property of beans. 
 * 
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 */
import java.io.InputStream;

import au.com.onegeek.lambda.api.exception.UnableToParseTestsException;

public interface TestProvider extends Plugin {

	/**
	 * Parse the Tests given an input stream.
	 * 
	 * @param stream The input containing test cases - may be a file, console input etc.
	 * @return Array of TestNG annotated classes containing Test methods.
	 */
	public Class<Test>[] parseTests(InputStream stream) throws UnableToParseTestsException;
	
	/**
	 * Return the file extensions the test provider provides.
	 * 
	 * @return
	 */
	public String[] providesTestExtensions();	
}