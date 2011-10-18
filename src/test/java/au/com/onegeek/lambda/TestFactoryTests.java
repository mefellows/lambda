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

import au.com.onegeek.lambda.core.JavassistTestBuilderImpl;
import au.com.onegeek.lambda.core.TestCommand;

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
		f.appendTestToLastMethod(new TestCommand("assertTitle", "$foobar"));
		f.appendTestToLastMethod(new TestCommand("assertTitle", "$foobar5"));
		f.addDataProvider();
		
		// For invoking the method directly
//		Object obj = f.getObjectInstance();	
//		Method m = obj.getClass().getMethod("testTheGoogle", java.lang.String.class, java.lang.String.class);		
//		m.invoke(obj, "Google", "Google");
		
		
		// Running TestNG inside of TestNG
		logger.info("Setting up TestNG");
		TestNG testng = new TestNG();
		TestListenerAdapter tla = new TestListenerAdapter();
		testng.setTestClasses(new Class[]{f.getCreatedClass()});
		testng.addListener(tla);
				
		// Run the TestNG suite
		logger.info("Running Tests");
		testng.run();		
	}
}