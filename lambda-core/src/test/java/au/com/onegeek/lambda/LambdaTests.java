/*
 * #%L
 * Lambda Core
 * %%
 * Copyright (C) 2011 null
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
package au.com.onegeek.lambda;

import org.testng.annotations.Test;

import au.com.onegeek.lambda.core.Lambda;
import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;

public class LambdaTests {

	@Test
	public void testLambda() throws UnableToParseTestsException, UnableToParseDataException {
		Lambda lambda = Lambda.getInstance();
		
		lambda.setBrowser("firefox");
		lambda.setHostname("http://retail.stage.mit");
		lambda.setTestSuiteFilename("/Users/mfellows/Desktop/retail.xlsx");
		lambda.run();
	}
}
