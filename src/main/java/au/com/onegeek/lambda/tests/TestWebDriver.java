package au.com.onegeek.lambda.tests;

import static org.testng.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import au.com.onegeek.lambda.framework.selenium.analytics.Analytics;
import au.com.onegeek.lambda.framework.selenium.analytics.SiteCatalystAnalyticsImpl;

import com.thoughtworks.selenium.Selenium;

@ContextConfiguration({ "classpath:META-INF/spring/test-context.xml" })
public class TestWebDriver extends AbstractTestNGSpringContextTests {
	private static final Logger logger = LoggerFactory.getLogger(TestWebDriver.class);
	
	/**
	 * The current browser driver.
	 */
	//@Autowired
	protected WebDriver driver;
	
	@Autowired
	protected List<WebDriver> drivers;
	
	/**
	 * The Analytics implementation specific to the site being tested.
	 */
	//@Autowired 
	private Analytics analytics;
	
	//@Autowired
	private SeleniumAssertions assertions;
	
	/**
	 * The Legacy Selenium Server for backwards-compatibility with tests.
	 * 
	 * @deprecated Classes should not implement the BrowserTest 
	 *             class and instead implement the new WebDriver class.
	 */
//	protected SeleniumServer server;
	
	/**
	 * Selenium object.
	 *  
	 */
	protected Selenium selenium;

	@DataProvider(name="testDataProvider")
	public Object[][] testDataProvider() {
	       Object[][] retObjArr={{"homepage","homepage", 123},
	    		   {"homepage","homepage", 123}};
	       return(retObjArr);		
	}
	@DataProvider(name="testXlsSpreadsheetProvider")
	public Object[][] testXlsSpreadsheetProvider() {
		
		// Support multiple formats? i.e. xls, ods, xlsx?		
		try {
			return SpreadsheetTestCase.getTableArray("/Users/mfellows/Desktop/test.xlsx", "Sheet1", "testWebDriver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@DataProvider(name="testXlsxSpreadsheetProvider")
	public Object[][] testXlsxSpreadsheetProvider() {
		
		// Support multiple formats? i.e. xls, ods, xlsx?		
		try {
			return SpreadsheetTestCase.getTableArray("/Users/mfellows/Desktop/test.xlsx", "Sheet1", "testWebDriver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	//@Test(dataProvider="aeuaoe")
	@Test
	public void runSpreadsheetTests() throws IOException {
		// TODO Auto-generated method stub
        InputStream excelInputStream = new FileInputStream("/Users/mfellows/development/browser-tests/src/main/java/com/melbourneit/web/test/tests.xlsx");
        
        Workbook workbook = new XSSFWorkbook(excelInputStream);
        logger.debug("workbook" + workbook.toString());

        
        // Parse Data sheets into Data object (Map?)
        // Should variables start with $ or something?
        //Sheet sheet = workbook.getSheet("data");
        List <HashMap<String, String>> dataMap = new LinkedList<HashMap<String, String>>();
        
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        	// TODO: variable variable replacement, store in a separate map temporarily
        	//       Make variables embeddable\replaceable within a cell i.e. foobar-${variable}-something
			// and replace later
        	// HashMap<String, String> variableReplacements = new HashMap<String, String>();
        	
        	Sheet sheet = workbook.getSheetAt(i);
        	if (sheet.getSheetName().startsWith("data")) {
        		// parse key\value pairso
        		HashMap<String, String> map = new HashMap<String, String>();
        		dataMap.add(map);
        		boolean done = false;
        		
        		Row row = sheet.getRow(sheet.getFirstRowNum());
        		while (!done) {
        			// TODO: parse numerics correctly (i.e. don't add decimal points if not needed)
        			String key = (String) TestXslxData.objectFrom(workbook, row.getCell(0));
        			String value = (String) TestXslxData.objectFrom(workbook, row.getCell(1));
        			logger.debug("Adding variable to map: " + key + ":" + value);
        			map.put(key, value);	
        			
        			row = sheet.getRow(row.getRowNum()+1);
        					
        			if (row == null || (row.getRowNum() == sheet.getLastRowNum() + 1)) {
        				done = true;
        			}
        		}
        	}
        }
        
        // TODO: Make test case execution a loop, and within each loop run each set of data
        // For now, just run against the first data set...
        Map<String, String> map = dataMap.get(0);
        
        // Parse Test sheets  
        // into TEST CASES (of type TestNG with private variables or MIT specific class?)
        //
        Sheet sheet = workbook.getSheet("Tests");        
        int maxRows = sheet.getPhysicalNumberOfRows();
        int i = 0;
        int currentRow = sheet.getFirstRowNum();
        logger.debug("Nr rows in sheet: " + maxRows);
        
        // Get First row, containing the test name and the data to be injected
        while (i < maxRows) {
        	logger.debug("i: " + i);
        	logger.debug("currentRow: " + currentRow);
        	Row row = sheet.getRow(currentRow);
        	
        	// Check for empty row
        	if (row != null && row.getPhysicalNumberOfCells() != 0) {
        		i++;

        		String command = null;
        		Object[] args = new Object[3];
        		int j = 0;
        		
		        // Get Cells
        		Iterator<Cell> iterator = row.cellIterator();
		        while(iterator.hasNext()) {
		        	Cell cell = iterator.next();
		        	logger.debug("Cell: " + TestXslxData.objectFrom(workbook, cell));
			        String cellValue = TestXslxData.objectFrom(workbook, cell).toString();
		        	
			        // variable substitution
			        // TODO: make this better, it doesn't really extract out a variable name very well - regex would be much better...
			        //       in fact, ${variable} is probably best as it aligns with JMeter...
			        //String regex = "(\\$\\{[a-zA-Z]*\\})";
			        String regex = "(\\$[a-zA-Z]*)";
			        Pattern p = Pattern.compile(regex);
			        Matcher m = p.matcher(cellValue);

			        if (m.groupCount() > 0) {
				        while (m.find()) {
			        		String match = m.group();
			        		logger.debug("variable reference found: " + match.substring(1) + ", replacing with current value from map: " + map.get(match.substring(1)));			        		
			        	    // If using the ${variable} style vars...
			        		//cellValue.replace(match, map.get(match.substring(2, match.length()-1)));
			        		
			        		// $variable style vars
			        	    cellValue = cellValue.replace(match, map.get(match.substring(1)));
			        	}
			        } 
			        
			        if (cellValue.startsWith("test")) {
		        		logger.debug("Test case found: " + cellValue);
		        		break;
		        		// TODO: create test case...
		        		
		        	} else {
		        		if (command == null) {
		        			logger.debug("Command found: " + cellValue); 
		        			command = cellValue;
		        		} else {
		        			logger.debug("Command argument found: " + cellValue);
		        			args[j] = cellValue;
		        			j++;
		        		}
		        	}
		        }
		        
		        if (command != null) {
		        	Object obj = selenium;
		        	
			        // Create Test Case w\
			        Method method = null;
			        try {
			        	// Determine argument types
			        	Class[] argTypes = new Class[j];
			        	int k = 0;
			        	for (k = 0; k < j; k++) {
			        		argTypes[k] = String[].class;
			        		argTypes[k] = args[k].getClass();
			        		logger.debug("Argument class: " + argTypes[k]);
			        	}
	        			
			        	// Cut down args to correct size
			        	args = Arrays.copyOf(args, k);
			        	
	        			if (command.startsWith("assert")) {
	        				obj = this.assertions;
	        				method = this.assertions.getClass().getMethod(command, argTypes);
	        			} else {
	        				method = selenium.getClass().getMethod(command, argTypes);
	        			}
			        } catch (SecurityException e) {
			        	fail("Method '" + command + "' security exception" + e.getMessage());
			        } catch (NoSuchMethodException e) {
			        	fail("Method '" + command + "' not found");
			        }
			        
			        String result = "not set";
			        try {
			        	result = (String) method.invoke(obj, args);
			        } catch (IllegalArgumentException e) {
			        	fail("Method '" + command + "' illegal argument exception: " + e.getMessage());
			        } catch (IllegalAccessException e) {
			        	fail("Method '" + command + "' illegal acess exception: " + e.getMessage());
			        } catch (InvocationTargetException e) {
			        	fail("Method '" + command + "' InvocationTarget exception: " + e.getMessage());
			        } catch (AssertionError e) {
			        	logger.error("Assertion fail: " + e.getMessage());
			        }
			        
			        logger.debug("output from reflected method: " + result);
		        }
		        
        	}
		    currentRow++;
        }
        		
	}
	
//	@Test(dataProvider="testXlsxSpreadsheetProvider")
//	@Test(dataProvider="testDataProvider")
	public void testWebDriver(String channel, String pageName, int foobar) {
		logger.debug("running tests...");
		logger.debug(this.analytics.toString());
		
		// Retail tests
		driver.get("http://ote.retail.melbourneit.com.au");
		
		analytics.assertPageName(channel + ":" + pageName);
		
	    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	    // Now you can do whatever you need to do with it, for example copy somewhere
	    try {
			FileUtils.copyFile(scrFile, new File(new File(".").getAbsolutePath() + "/screenshot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		analytics.assertVariable("channel", channel);
		analytics.assertServerNameNotNull();
		
//		selenium.getBodyText()
		
		Method method = null;
		try {
		  method = selenium.getClass().getMethod("getBodyText");
		} catch (SecurityException e) {
		  // ...
		} catch (NoSuchMethodException e) {
		  // ...
		}

		String result = "not set";
		try {
		   result = (String) method.invoke(selenium, null);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		
		logger.debug("output from reflected method: " + result);
	}
	
	@BeforeClass
	@Parameters({"selenium.browser", "environment.url", "environment.analytics" })
	public void startSelenium(String browser, String url, String analytics) throws InterruptedException {
		
		// Create the browser driver
		try {
			logger.debug("Creating " + browser + " Driver.");
			this.driver = TestWebDriver.getDriver(browser);
			driver.get(url);
		} catch (Exception e) {
			logger.debug("Could not create driver because of: "
					+ e.getMessage() + "\n Exiting now...");
			this.shutdown();
			System.exit(1);
		}

		// Start the Selenium Server
		try {
			// Setup the OLD selenium driver
//			server = new SeleniumServer();
//			server.start();
			this.selenium = new WebDriverBackedSelenium(this.driver, url);
			this.selenium.open(url);
		} catch (Exception e) {
			logger.debug("Could not start selenium or the server because: " + e.getMessage());
			this.shutdown();
			System.exit(1);
		}
		
		// Wire in the analytics implementation
		logger.debug("startSelenium: analytics value: " + analytics);
		try {
			this.analytics = TestWebDriver.getAnalytics(driver, analytics);
		} catch (Exception e) {
			logger.debug("Could not create analytics provider because of: "
					+ e.getMessage() + "\n Exiting now...");
			this.shutdown();
			System.exit(1);			
		}
		
		this.assertions = new SeleniumAssertions(selenium);
	}
	
	@AfterClass
	public void shutdown() {
		logger.debug("teardown()");
		
		// Close the browser
		if (this.driver != null) {
			this.driver.close();
			this.driver.quit();
		}
		
		// Shut down server
		if (this.selenium != null) {
			selenium.stop();
		}
//		if (this.server != null) {
//			this.server.stop();
//		}
	}
	
	/**
	 * Get the Analytics provider.
	 * 
	 * Factory method. 
	 * 
	 * @param driver
	 * @param provider
	 * @return
	 * @throws Exception 
	 */
	public static Analytics getAnalytics(WebDriver driver, String provider) throws Exception {		
		if (provider.equalsIgnoreCase("sitecatalyst")) {
			SiteCatalystAnalyticsImpl scAnalytics = new SiteCatalystAnalyticsImpl();
			scAnalytics.setDriver(driver);
			return scAnalytics;
		}
		throw new Exception("Analytics provider could not be found. Please set 'environment.analytics' in your config file, ensure a provider (*Impl.java) exists and try again.");
	}

	/**
	 * Creates a OS \ architecture independent WebDriver based on the browser
	 * requested. TODO: This should be moved into a Factory of sorts so that it
	 * can be refactored and tidied up.
	 * 
	 * @param browser
	 *            The name of the browser to create. Currently supports Firefox,
	 *            Chrome, IE, HTMLUnit and null. Default is HTMLunit if none
	 *            supplied. Parameter is case insensitive.
	 * @return The WebDriver to run the tests.
	 * @throws Exception
	 */
	public static WebDriver getDriver(String browser) throws Exception {
		WebDriver driver = null;

		// HTMLUnit is default browser
		if (browser == null || browser.equalsIgnoreCase("htmlunit")
				|| browser.trim().equalsIgnoreCase("")) {
			driver = new HtmlUnitDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			// Create a new instance of the Firefox driver
			// Notice that the remainder of the code relies on the interface,
			// not the implementation.
			Properties props = System.getProperties();
			Enumeration e = props.propertyNames();

			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				logger.debug(key + " -- " + props.getProperty(key));
			}

			String os = System.getProperty("os.name");
			String arch = System.getProperty("sun.arch.data.model");
			String currentDir = new File(".").getAbsolutePath();
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();

			// Check OS
			if (os.indexOf("Mac OS X") > -1) {
				capabilities
						.setCapability("chrome.binary",
								"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");

				// TODO: Make this class a factory, and make this method OS
				// aware
				// - generate the driver depending on the test case (i.e. loop)
				// - detect OS and use correct driver
				System.setProperty("webdriver.chrome.driver", currentDir
						+ "/lib/chromedriver-macosx");
			} else if (arch.equalsIgnoreCase("32") && os.indexOf("Linux") > -1) {

			} else if (arch.equalsIgnoreCase("64") && os.indexOf("Linux") > -1) {

			} else if (os.indexOf("Linux") > -1) {

			} else {
				throw new Exception(
						"Cannot find WebDriver for os, therefore cannot run these tests: "
								+ os);
			}

			driver = new ChromeDriver(capabilities);
		}

		return driver;
	}
}
