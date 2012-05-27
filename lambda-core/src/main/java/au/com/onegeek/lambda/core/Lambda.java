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
package au.com.onegeek.lambda.core;

// 
// test -h http://ote.retail.melbourneit.com.au -d /Users/mfellows/development/lambda/lambda-core/src/main/resources/tests.xlsx -f /Users/mfellows/development/lambda/lambda-core/src/main/resources/tests.xlsx -b chrome
//test -f /Users/mfellows/development/lambda/lambda-assembly/target/lambda-0.0.1-SNAPSHOT-clamshell-assembly/examples/retail.xlsx -b chrome -e http://ote.retail.melbourneit.com.au

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.testng.TestNG;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.Config;
import au.com.onegeek.lambda.api.Context;
import au.com.onegeek.lambda.api.DataProvider;
import au.com.onegeek.lambda.api.OutputConnector;
import au.com.onegeek.lambda.api.Plugin;
import au.com.onegeek.lambda.api.Test;
import au.com.onegeek.lambda.api.TestProvider;
import au.com.onegeek.lambda.api.exception.UnableToParseDataException;
import au.com.onegeek.lambda.api.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.core.exception.ProviderNotFoundException;
import au.com.onegeek.lambda.core.provider.WebDriverBackedSeleniumProvider;

/**
 * Front end for running the Lamba browser testing framework.
 * 
 * TODO: Across entire project check for optimal use of logging statements (i.e.
 * logger.debug("this is a {}", string); as described here:
 * http://logback.qos.ch/manual/architecture.html )
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 * 
 */
//@Component
@Configurable
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
	protected List<OutputConnector> connectorRegistry;

	/**
	 * A registry of parsers for reading input test data.
	 */
	protected List<DataProvider> dataProviders;
	
	/**
	 * A registry of parsers for reading input test cases.
	 */
	protected List<TestProvider> testProviders;

	/**
	 * A registry of Assertion Providers for use within test cases.
	 */
	protected List<AssertionProvider> assertionProviders;

	private String testSuiteFilename;

	private String dataSetFilename;

	private String browser;

	private String hostname;

	// TODO: Springify me ?
	// @Autowired
	private WebDriver driver;

	// TODO: Springify me ?
	// @Autowired
	private WebDriverBackedSeleniumProvider selenium;

	private static Lambda instance;

//	@Autowired
	//private DataProvider dataProvider;

//	@Autowired
	//private TestProvider testProvider;

	/**
	 * List of plugins given to Lambda.
	 */
	private List<Plugin> plugins;

	/**
	 * Classloader for finding Plugins etc.
	 */
	private ClassLoader classLoader;

	@Autowired
	private PluginClassloader pluginClassloader;
	
	/**
	 * Configuration identity.
	 * 
	 * TODO: Make this a mandatory input to Lambda. Switch between Spring \
	 * TestNG configuration v CLI v GUI by creating a uniform interface.
	 * 
	 */
	@Autowired
	private Config configuration;
	
	/**
	 * Framework context.
	 */
	@Autowired
	private Context context;

	/**
	 * Gets the last instance of ClassLoader created, otherwise it creates one.
	 * Internally, it setups a class loader for the path specified in property
	 * cli.dir.plugins (or defaults to directory ./plugins) Any jar files found
	 * at that location will be loaded in that class loader.
	 * 
	 * @return
	 */
	private ClassLoader getClassLoader() {
		return this.pluginClassloader.getClassloader();
	}

	/**
	 * Returns a list of the loaded Plugin instances.
	 * 
	 * @return List <Plugin>
	 */
	public List<Plugin> getPlugins() {
		logger.info("get plugins ");
		if (plugins != null) {
			return plugins;
		}
		ClassLoader cl = getClassLoader();
		plugins = PluginLoader.loadPlugins(cl);
		return plugins;
	}

	/**
	 * Retrieves a list of Class instances using the provided Type.
	 * 
	 * @param <T>
	 *            The generic type used to filter the plugins by type
	 * @param type
	 *            the Class to used as filter
	 * @return List of components of type <T>
	 */
	public <T> List<T> getPluginsByType(Class<T> type) {
		List<T> result = new ArrayList<T>();
		for (Plugin p : getPlugins()) {
			if (type.isAssignableFrom(p.getClass())) {
				result.add((T) p);
			}
		}
		return result;
	}

	protected void importPlugins() {
		logger.info("Loading Plugins");

		// Find Parsers
		this.classLoader = this.getClassLoader();
		this.plugins = PluginLoader.loadPlugins(this.classLoader);

		if (logger.isDebugEnabled()) {
			for (Plugin p : this.plugins) {
				logger.debug("Found Plugin with name: : " + p.getClass().getName());
			}
		}
		
		// Find assertion modules
		this.assertionProviders = this.getPluginsByType(AssertionProvider.class);
		
		Assert.notNull(this.selenium, "Selenium must not be null");
		this.assertionProviders.add(this.selenium);
		this.dataProviders = this.getPluginsByType(DataProvider.class);
		this.testProviders = this.getPluginsByType(TestProvider.class);
		
		// Find Reporting Connectors

		// Initialise connections with the connectors

		
		// Update context with Selenium
		context.setDriver(driver);
		
		// Initialize plugins
		for (Plugin p: this.plugins) {
			p.start(context);
		}
	}

	protected void importTests() throws Exception {
		// Import Tests

		// TODO: Abstract this out of the class and make it extensible. For
		// example, a Java input
		// type needs to create it's own classloader e.g. built from the
		// <lambda-home>/tests dir
		InputStream testSuiteInputStream = null;
		String extension = "";

		// TODO: lookup input type and match parser against parser registry
		if (this.tests == null) {
			logger.info("Creating Tests");
			try {
				extension = testSuiteFilename.substring(this.testSuiteFilename.lastIndexOf('.')+1);
				logger.debug("Extension: " + extension);
				
				testSuiteInputStream = new FileInputStream(
						this.testSuiteFilename);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: Handle exceptions
			}
			
			TestProvider testProvider = null;
			for (TestProvider p: this.testProviders) {
				for (String ext: p.providesTestExtensions()) {
					if (ext.equalsIgnoreCase(extension)) {
						testProvider = p;
					}
				}
			}
			
			if (testProvider == null) {
				throw new ProviderNotFoundException("A test provider for extension [." + extension + "] could not be found. Please install a supported plugin or check the test filename");
			}
			this.tests = testProvider.parseTests(testSuiteInputStream);
		}
	}
	
	public void importData() throws Exception {
		// TODO: Make the input stream the input, not hard-coded to file...
		// TODO: Abstract this out of the class and make it extensible. For
		// example, a Java input
		// type needs to create it's own classloader e.g. built from the
		// <lambda-home>/tests dir
		InputStream dataSetInputStream = null;
		String extension = "";


		// TODO: lookup input type and match parser against parser registry
		if (this.dataSet == null) {
			logger.info("Creating Dataset");
			if (this.dataSetFilename != null) {
				try {
					extension = dataSetFilename.substring(this.dataSetFilename.lastIndexOf('.')+1);
					dataSetInputStream = new FileInputStream(
							this.dataSetFilename);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: Handle exceptions
				}
			}
			DataProvider dataProvider = null;
			for (DataProvider p: this.dataProviders) {
				for (String ext: p.providesDataExtensions()) {
					if (ext.equalsIgnoreCase(extension)) {
						dataProvider = p;
					}
				}
			}
			if (dataProvider == null) {
				throw new ProviderNotFoundException("A data provider for extension [." + extension + "] could not be found. Please install a supported plugin or check the dataset filename");
			}
			
			this.dataSet = dataProvider.parseDataSet(dataSetInputStream);
		}		
	}
	
	public void init() {
//		this.importPlugins();
	}

	
	// TODO: Refactor this so that all the plugins and stuff happen in an init method. Pass around the Config object to remove the state from THIS application.
	public void run() throws UnableToParseTestsException,
			UnableToParseDataException, ProviderNotFoundException {
		// Read config
		logger.debug("Checking context...");
		
		logger.debug(context == null || (context.getConfig() == null) ? "Context is null" : "context is not null");
		
		// Import General Configuration

		// Import Plugins: Parsers, Reporting Engines, Connectors
		// - This should enable filtering on input files etc.
		logger.info("Running Lambda");

		// Set paths to reporting

		// Startup Selenium etc.

		// Create the browser driver
		try {
			logger.debug("Creating " + browser + " Driver.");
			this.driver = BrowserFactory.getDriver(this.browser);
			driver.get(this.hostname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Could not create driver for browser '" + browser
					+ "' because of: " + e.getMessage() + "\n Exiting now...");
			System.exit(1);
		}
		
		// Start the Selenium Server
		try {
			this.selenium = new WebDriverBackedSeleniumProvider(this.driver, this.hostname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Could not start selenium or the server because: "
					+ e.getMessage());
		}
		
		// Import Plugins (Including the Selenium Plugin)
		this.importPlugins();

		// Parse the tests
		try {
			// Parse the data map
			this.importData();
			this.importTests();
		
		} catch (Exception e) {
			this.shutdown();
			
			throw new ProviderNotFoundException(e.getMessage());
		}

		
		logger.info("Running non Testng Tests");
		for (Class<Test> clazz: tests) {
			Test test = null;
			try {
				test = clazz.newInstance();
			} catch (InstantiationException e) {
				logger.info("Could not instantiate the Test class: " + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				logger.info("Could not instantiate the Test class: " + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Method m: clazz.getMethods()) {
				org.testng.annotations.Test testAnnotation = m.getAnnotation(org.testng.annotations.Test.class);
				if (testAnnotation != null) {
					logger.info("Runing test with name: " + m.getName());
					// Crap - how do we do variables? TestNG has @DataProviders, can we leverage this?
					String dataProviderMethodName = testAnnotation.dataProvider();
					if (dataProviderMethodName != null) {
						Method dataProvidermethod = null;
						try {
							dataProvidermethod = clazz.getDeclaredMethod(dataProviderMethodName);
							Object[][] data = (Object[][]) dataProvidermethod.invoke(test);
							for (Object[] array: data) {
								m.invoke(test, array);
							}
						} catch (SecurityException e) {
							logger.info("Could not instantiate the DataProvider method SE: " + e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							logger.info("Could not instantiate the DataProvider method NSME: " + e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							logger.info("Could not instantiate the Test class IAE: " + e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							logger.info("Could not instantiate the Test class IAE: " + e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							logger.info("Could not instantiate the Test class ITE: " + e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		// Create TestNG objects
		/*
		logger.info("Starting TestNG Instance");
		this.testng = new TestNG();
		testng.setVerbose(10);
		TestListenerAdapter tla = new TestListenerAdapter();
		
		testng.setTestClasses(this.tests);
		testng.addListener(tla);

		// Run the TestNG suite
		logger.info("Running Tests");
		testng.run();
*/

		this.shutdown();

		// Timer has already been stopped issue.. Consider running this in own
		// Thread?

		// Produce Reports

		//
	}
	
	private void shutdown() {
		// Close down browsers etc.

		try {
			// Is this order correct?
			selenium.stop();
			driver.quit();
			selenium = null;
			driver = null;
		} catch (Exception e) {
			logger.debug("Browser shutdown wasn't completely successful. This seems to happen with the Chrome driver so don't be too disheartened.");
		}
		
		// Close down stateful objects 
		this.dataSet = null;//new LinkedList<Map<String,Object>>();
		this.tests = null;
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

	/**
	 * Set's the Test Website hostname (including protocol) for this test suite.
	 * 
	 * @param hostname
	 *            The name of the host to connect to i.e. http://www.google.com
	 * @throws IOException
	 *             If the hostname is malformed or host doesn't exist, an
	 *             IOException is thrown.
	 */
	public void setHostname(String hostname) throws IOException {
		try {
			URL url = new URL(hostname);
			URLConnection conn = url.openConnection();
//			conn.connect();
			//conn.getInputStream();
		} catch (MalformedURLException e) {
			throw new MalformedURLException("Test URL Provided is invalid: "
					+ e.getMessage());
		} catch (IOException e) {
			throw new IOException("Test URL Provided does not exist: "
					+ e.getMessage());
		}

		this.hostname = hostname;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriverBackedSeleniumProvider getSelenium() {
		return selenium;
	}

	public void setSelenium(WebDriverBackedSeleniumProvider selenium) {
		this.selenium = selenium;
	}

	public List<AssertionProvider> getAssertionProviders() {
		return assertionProviders;
	}

	public void setAssertionProviders(List<AssertionProvider> assertionProviders) {
		this.assertionProviders = assertionProviders;
	}
}
