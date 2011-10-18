package au.com.onegeek.lambda.core;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.parser.Excel2SeleniumParser;

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
	
	private String testSuiteFilename;
	
	private String dataSetFilename;
	
	/**
	 * Configuration identity.
	 * 
	 * TODO: Make this a mandatory input to Lamba. Switch between
	 * Spring \ TestNG configuration v CLI v GUI by creating a uniform interface.
	 * 
	 */
	//private Configuration configuration;

	public static void main(String[] args) throws UnableToParseTestsException, UnableToParseDataException {
		Lambda lambda = new Lambda();			
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
	
	public void runWithParams(String filename) throws UnableToParseTestsException, UnableToParseDataException {
		this.testSuiteFilename = filename;
		this.run();
	}
	public void runWithParams(String filename, String dataSetFilename) throws UnableToParseTestsException, UnableToParseDataException {
		this.testSuiteFilename = filename;
		this.dataSetFilename = dataSetFilename;
		this.run();
	}
	
	public void run() throws UnableToParseTestsException, UnableToParseDataException {
		
		this.importPlugins();
		this.importTests();
		
		System.out.println("java.class.path" + System.getProperty("java.class.path"));
		
		
		// Import General Configuration
		
		
		// Import Plugins: Parsers, Reporting Engines, Connectors
		//   - This should enable filtering on input files etc.
		logger.info("Running Lambda");
		
		// Import TEST Configuration
		
		// Import Tests
		
		// TODO: make the input stream the input, not hard-coded to file...
		InputStream testSuiteInputStream = null;
		InputStream dataSetInputStream = null;
		
		// TODO: lookup input type and match parser against parser registry
		logger.info("Creating Dataset");
		try {
			if (this.testSuiteFilename == null) {
				this.testSuiteFilename = "/Users/mfellows/development/lambda/src/main/java/au/com/onegeek/lambda/tests/tests-aes.xlsx";
			}
			testSuiteInputStream = new FileInputStream(this.testSuiteFilename);
		} catch(Exception e) {
			e.printStackTrace();
			// TODO: Handle exceptions
		}		
		Excel2SeleniumParser dataParser = new Excel2SeleniumParser();
		//IDataParser = ...		
		this.dataSet = dataParser.parseDataSet(testSuiteInputStream);
		
		
		// TODO: lookup input type and match parser against parser registry
		logger.info("Creating Tests");
		if (this.dataSetFilename != null) {
			try {
				dataSetInputStream = new FileInputStream(this.dataSetFilename);
			} catch(Exception e) {
				e.printStackTrace();
				// TODO: Handle exceptions
			}
		} else {
			// Use the same filename as for the test cases
			dataSetInputStream = testSuiteInputStream;
		}
		ITestParser parser = dataParser;
		this.tests = parser.parseTests(dataSetInputStream);
		
		logger.debug(String.valueOf(this.tests.length));
		for(Class<Test> t : this.tests) {
			System.out.println(t);
		}
		
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