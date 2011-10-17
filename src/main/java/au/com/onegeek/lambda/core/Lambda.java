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

import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.parser.Excel2SeleniumParser;
import au.com.onegeek.lambda.tests.TestWebDriver;

/**
 * Front end for running the Lamba browser testing framework.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
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
	protected Class<Test>[] tests;
	
	/**
	 * Data variable sets to test against.
	 */
	protected List<Map<String, Object>> dataSet = new LinkedList<Map<String, Object>>(); 
	
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

	public static void main(String[] args) throws UnableToParseTestsException, UnableToParseDataException {
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
	
	public void run() throws UnableToParseTestsException, UnableToParseDataException {
		
		// Import General Configuration
		
		
		// Import Plugins: Parsers, Reporting Engines, Connectors
		//   - This should enable filtering on input files etc.
		logger.info("Running Lambda");
		
		// Import TEST Configuration
		
		// Import Tests
		
		InputStream excelInputStream = null;
		try {
			//excelInputStream = new FileInputStream("/Users/mfellows/development/browser-tests/src/main/java/com/melbourneit/web/test/tests.xlsx");
			excelInputStream = new FileInputStream("/Users/mfellows/development/browser-tests/src/main/java/com/melbourneit/web/test/tests.xlsx");
		} catch(Exception e) {
			e.printStackTrace();
			// TODO: Handle exceptions
		}
		
		// TODO: lookup input type and match parser against parser registry
		logger.info("Creating Dataset");
		Excel2SeleniumParser dataParser = new Excel2SeleniumParser();
		//IDataParser = ...		
		this.dataSet = dataParser.parseDataSet(excelInputStream);
		
		// TODO: lookup input type and match parser against parser registry
		logger.info("Creating Tests");		
		ITestParser parser = dataParser;
		this.tests = parser.parseTests(excelInputStream);
		
		logger.debug(String.valueOf(this.tests.length));
		
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

	public Class<Test>[] getTests() {
		return tests;
	}

	public void setTests(Class<Test>[] tests) {
		this.tests = tests;
	}

	public List<Map<String, Object>> getDataSet() {
		return dataSet;
	}

	public void setDataSet(List<Map<String, Object>> dataSet) {
		this.dataSet = dataSet;
	}
} 