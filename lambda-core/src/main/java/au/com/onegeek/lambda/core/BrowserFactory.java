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



import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.api.exception.BrowserNotFoundException;

import com.opera.core.systems.OperaDriver;

public class BrowserFactory {
	/**
	 * Class logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
	
	/**
	 * Factory method that creates a OS \ architecture independent WebDriver based on the browser
	 * requested.
	 * 
	 * @param browser 	The name of the browser to create. Currently supports Firefox,
	 *            		Chrome, IE, HTMLUnit and null. Default is HTMLunit if none
	 *            		supplied. Parameter is case insensitive..
	 * @return The WebDriver to run the tests.
	 * @throws Exception
	 */
	public static WebDriver getDriver(String browser) throws Exception {
		WebDriver driver = null;

		// HTMLUnit is default browser
		if (browser == null || browser.equalsIgnoreCase("htmlunit") || browser.trim().equalsIgnoreCase("")) {
			logger.debug("Creating new HTMLUnit driver");
			driver = new HtmlUnitDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			logger.debug("Creating new Firefox driver");
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("opera")) {
			logger.debug("Creating new Opera driver");
			driver = new OperaDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			
			logger.debug("Creating new Chrome driver");
			// Chrome has it's own web driver implementation, need to specify the property 'webdriver.chrome.driver'
			Properties props = System.getProperties();
						
			if (System.getProperty("webdriver.chrome.driver") == null) {			
				// Create a new instance of the Chrome driver
				// Notice that the remainder of the code relies on the interface,
				// not the implementation.
				Enumeration e = props.propertyNames();
	
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					logger.debug("key {} -- value{}", key, props.getProperty(key));
				}
	
				String os = System.getProperty("os.name");
				String arch = System.getProperty("sun.arch.data.model");
				String currentDir = new File(".").getAbsolutePath();
	
				// Check OS
				if (os.indexOf("Mac OS X") > -1) {
					//capabilities.setCapability("chrome.binary", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
					System.setProperty("webdriver.chrome.driver", currentDir + "/lib/chromedriver-macosx");
				} else if (arch.equalsIgnoreCase("32") && os.indexOf("Linux") > -1) {
					System.setProperty("webdriver.chrome.driver", currentDir + "/lib/chromedriver-linux32");
				} else if (arch.equalsIgnoreCase("64") && os.indexOf("Linux") > -1) {
					System.setProperty("webdriver.chrome.driver", currentDir + "/lib/chromedriver-linux64");
				} else if (os.indexOf("Linux") > -1) {
					System.setProperty("webdriver.chrome.driver", currentDir + "/lib/chromedriver-linux32");
				} else if (os.indexOf("Windows") > -1) {
					System.setProperty("webdriver.chrome.driver", currentDir + "/lib/chromedriver.exe");
				} else {
					throw new Exception("Cannot find WebDriver for os, therefore cannot run these tests: " + os);
				}
			}

			
			logger.debug("Chrome driver binary: " + System.getProperty("webdriver.chrome.driver"));
//			driver = new ChromeDriver(capabilities);
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie") || browser.equalsIgnoreCase("iexplore")) {
			logger.debug("Creating new Internet Explorer driver");
			driver = new InternetExplorerDriver(); 
		} else {
			throw new BrowserNotFoundException("Browser " + browser + " not supported.");
		}

		return driver;
	}
}
