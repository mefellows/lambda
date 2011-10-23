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
package au.com.onegeek.lambda.core;
//test -f /Users/mfellows/development/lambda/lambda-assembly/target/lambda-0.0.1-SNAPSHOT-clamshell-assembly/examples/retail.xlsx -b chrome -e http://ote.retail.melbourneit.com.au

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
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
 * TODO: Across entire project check for optimal use of logging statements (i.e. logger.debug("this is a {}", string); as described
 *       here: http://logback.qos.ch/manual/architecture.html )
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
	protected List<Map<String, Object>> dataSet; 
	
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
	
	private String browser;
	
	private String hostname;
	
	//@Autowired
	private WebDriver driver;

	//@Autowired
	private WebDriverBackedSelenium selenium;
	
	private static Lambda instance;
	
	/**
	 * Configuration identity.
	 * 
	 * TODO: Make this a mandatory input to Lamba. Switch between
	 * Spring \ TestNG configuration v CLI v GUI by creating a uniform interface.
	 * 
	 */
	//private Configuration configuration;

	private Lambda() {
		
	}
	
	public static Lambda getInstance() {
		if (instance == null) {
			instance = new Lambda();
		}
		return instance;
	}
	
	public static void main(String[] args) throws UnableToParseTestsException, UnableToParseDataException {
		Lambda lambda = Lambda.getInstance();		
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
		Excel2SeleniumParser dataParser = new Excel2SeleniumParser();
		if ( this.dataSet == null) {
			logger.info("Creating Dataset");
			try {
				if (this.testSuiteFilename == null) {
					this.testSuiteFilename = "/Users/mfellows/development/lambda/lambda-core/src/main/java/au/com/onegeek/lambda/tests/tests-aes.xlsx";
				}			
				testSuiteInputStream = new FileInputStream(this.testSuiteFilename);
			} catch(Exception e) {
				e.printStackTrace();
				// TODO: Handle exceptions
			}		
			//IDataParser = ...		
			this.dataSet = dataParser.parseDataSet(testSuiteInputStream);
		}
		
		
		// TODO: lookup input type and match parser against parser registry
		if (this.tests == null) {
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
		}
		// Set paths to reporting
		
		// Startup Selenium etc.
		
		// Create the browser driver
		try {
			logger.debug("Creating " + browser + " Driver.");
			this.driver = BrowserFactory.getDriver(this.browser);
			driver.get(this.hostname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Could not create driver for browser '" + browser + "' because of: " + e.getMessage() + "\n Exiting now...");
			System.exit(1);
		}
		
		// Start the Selenium Server
		try {
			this.selenium = new WebDriverBackedSelenium(this.driver, this.hostname);
			this.selenium.open("/");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Could not start selenium or the server because: " + e.getMessage());
			System.exit(1);
		}
		
		// Create TestNG objects
		logger.info("Starting TestNG Instance");
		this.testng = new TestNG();
		TestListenerAdapter tla = new TestListenerAdapter();
		testng.setTestClasses(this.tests);
		testng.addListener(tla);
				
		// Run the TestNG suite
		logger.info("Running Tests");
		testng.run();

		// Close down browsers etc.
		
		// Is this order correct?
	    selenium.stop();
	    driver.quit();
	    selenium = null;
	    driver = null;
	    
	    // Timer has already been stopped issue.. Consider running this in own Thread?
		
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

	public String getTestSuiteFilename() {
		return testSuiteFilename;
	}

	public void setTestSuiteFilename(String testSuiteFilename) {
		this.testSuiteFilename = testSuiteFilename;
	}

	public String getDataSetFilename() {
		return dataSetFilename;
	}

	public void setDataSetFilename(String dataSetFilename) {
		this.dataSetFilename = dataSetFilename;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriverBackedSelenium getSelenium() {
		return selenium;
	}

	public void setSelenium(WebDriverBackedSelenium selenium) {
		this.selenium = selenium;
	}
} 
