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
package au.com.onegeek.lambda;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import au.com.onegeek.lambda.api.exception.UnableToParseDataException;
import au.com.onegeek.lambda.api.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.core.Lambda;
import au.com.onegeek.lambda.core.exception.ProviderNotFoundException;

@ContextConfiguration({"classpath:META-INF/spring/test-context.xml"})
public class LambdaTests extends AbstractTestNGSpringContextTests {

	@Autowired
	Lambda lambda;
	
	@Test
	public void testLambda() throws UnableToParseTestsException, UnableToParseDataException, IOException, ProviderNotFoundException {
		//Lambda lambda = Lambda.getInstance();
		//Lambda lambda = new Lambda();
		
		
		lambda.setBrowser("firefox");
		lambda.setHostname("http://ote.retail.melbourneit.com.au");
		lambda.setTestSuiteFilename("/Users/mfellows/development/lambda/lambda-core/src/main/resources/tests.xlsx");
		lambda.setDataSetFilename("/Users/mfellows/development/lambda/lambda-core/src/main/resources/tests.xlsx");
		lambda.run();
	}
}