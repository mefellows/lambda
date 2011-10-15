package au.com.onegeek.lambda.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;

import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.parser.Excel2SeleniumParser;
import au.com.onegeek.lambda.tests.TestWebDriver;

/**
 * This needs a better name - what if I want to create another custom input format?
 * 
 * Anyway, it will do for now...
 * 
 * @author mfellows
 *
 */
public class Lambda {
	/**
	 * Class logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Lambda.class);
	
	/**
	 * Tests to be provided to the TestNG class.
	 */
	//protected Class<IDynamicTest>[] tests;
	protected Class<Test>[] tests;
	
	/**
	 * Data variable sets to test against.
	 */
	protected List <HashMap<String, String>> dataSet = new LinkedList<HashMap<String, String>>(); 
	
	/**
	 * The TestNG class to bootstrap and run the actual tests.
	 */
	protected TestNG testng;
	
	/**
	 * A registry of output connectors for publishing after Test Case execution.  
	 */
	protected Map<String, IOutputConnector> connectorRegistry;
	
	/**
	 * A registry of parsers for reading input test cases.
	 */
	protected Map<String, ITestParser> parserRegistry;
	
	public static void main(String[] args) throws UnableToParseTestsException {
		Lambda lambda = new Lambda();
		
		lambda.importPlugins();
		lambda.importTests();
		
		lambda.run();
	}
	
	protected void importPlugins() {
		// Find Parsers
		
		// Find assertion modules 
		
		// Find Reporting Connectors
		
			// Initialise connections with the connectors
		
		
	}
	
	protected void importTests() {
		// 
	}
	
	@DataProvider(name="dynamicDataProvider")
	protected Object[][] getData() {
		return null;
	}
	
	public void run() throws UnableToParseTestsException {
		
		// Import Plugins: Parsers, Reporting Engines, Connectors
		//   - This should enable filtering on input files etc.
		logger.info("Running Lambda");
		
		// Import Tests
		
		InputStream excelInputStream = null;
		try {
			excelInputStream = new FileInputStream("/Users/mfellows/development/browser-tests/src/main/java/com/melbourneit/web/test/tests.xlsx");
		} catch(Exception e) {
			// TODO: Handle exceptions
		}
		
		// TODO: lookup input type and match parser against parser registry
		ITestParser parser = new Excel2SeleniumParser();
		tests = parser.parseTests(excelInputStream);
		logger.info("Creating Tests");		
		
		// Set paths to reporting
		
		// Create TestNG objects
		logger.info("Creating TestNG Instance");
		this.testng = new TestNG();
		TestListenerAdapter tla = new TestListenerAdapter();
		testng.setTestClasses(tests);
		testng.addListener(tla);
				
		// Run the TestNG suite
		logger.info("Running Tests");
		testng.run();
		
		
		// Produce Reports
		
		// 
	}
	
	
	
} 