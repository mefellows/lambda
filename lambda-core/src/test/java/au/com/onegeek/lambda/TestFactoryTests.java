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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

import au.com.onegeek.lambda.api.TestCommand;
import au.com.onegeek.lambda.core.JavassistTestBuilderImpl;

public class TestFactoryTests {
	private static final Logger logger = LoggerFactory.getLogger(TestFactoryTests.class);
	@Test
	public void testClassCreation() throws Exception {
		List<Map<String, Object>> dataSet = new LinkedList<Map<String, Object>>();
		
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("fooba1r", "Google");
		map1.put("foobar2", "Google");
		map1.put("foobar3", "Google");
		map1.put("foobarr", "Google");
		map1.put("foobar", "Google");
		map1.put("foobar5", "Google");
		dataSet.add(map1);
		
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("fooba1r", "Google");
		map2.put("foobar2", "Google");
		map2.put("foobar3", "Google");
		map2.put("foobarr", "Google");
		map2.put("foobar", "Google");
		map2.put("foobar5", "Google");
		dataSet.add(map2);
		 
		JavassistTestBuilderImpl f = JavassistTestBuilderImpl.getInstance();
		
		f.makeTestClass("au.com.onegeek.lambda.tests.runtimegenerated.eatmyTests", dataSet);

		f.addTest("testTheGoogle");
		f.appendTestToLastMethod(new TestCommand("getBodyText"));
		f.appendTestToLastMethod(new TestCommand("wait", new Double(10.00)));
		f.appendTestToLastMethod(new TestCommand("assertTitle", "$foobar"));
		f.appendTestToLastMethod(new TestCommand("assertTitle", "$foobar5"));
		f.addDataProvider();
		
		// For invoking the method directly
//		Object obj = f.getObjectInstance();	
//		Method m = obj.getClass().getMethod("testTheGoogle", java.lang.String.class, java.lang.String.class);		
//		m.invoke(obj, "Google", "Google");
		
		
		// Running TestNG inside of TestNG
//		logger.debug("Setting up TestNG");
//		TestNG testng = new TestNG();
//		testng.setVerbose(10);
//		TestListenerAdapter tla = new TestListenerAdapter();
//		testng.setTestClasses(new Class[]{f.getCreatedClass()});
//		testng.addListener(tla);
//				
//		// Run the TestNG suite
//		logger.debug("Running Tests");
//		testng.run();
	}
}
