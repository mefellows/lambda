/*
 * #%L
 * Lambda Core
 * %%
 * Copyright (C) 2011 - 2012 OneGeek
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
/**
 * Copyright Substantiate 2012.
 */
package au.com.onegeek.lambda;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import au.com.onegeek.lambda.core.BrowserFactory;
import au.com.onegeek.lambda.core.provider.WebDriverBackedSeleniumProvider;

/**
 * @author mfellows
 *
 */
public class BrowserFactoryTest {
	private static final Logger logger = LoggerFactory.getLogger(BrowserFactoryTest.class);
	
	@Test
	public void testBrowserFactory() throws Exception {
		
		String browser = "chrome";
		String hostname = "http://ote.retail.melbourneit.com.au";
		WebDriverBackedSeleniumProvider selenium = null;
		WebDriver driver = null;
//		System.setProperty("webdriver.chrome.driver", "/Users/mfellows/development/lambda/lambda-assembly/lib/chromedriver-macosx");
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
//		driver = new FirefoxDriver();
//		driver = new ChromeDriver();
		
		driver = BrowserFactory.getDriver(browser);
		driver.get(hostname);
		selenium = new WebDriverBackedSeleniumProvider(driver, hostname);
//		try {
//			logger.debug("Creating " + browser + " Driver.");
//			driver = BrowserFactory.getDriver(browser);
//			driver.get(hostname);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.debug("Could not create driver for browser '" + browser
//					+ "' because of: " + e.getMessage() + "\n Exiting now...");
//			System.exit(1);
//		}
//		
//		// Start the Selenium Server
//		try {
//			selenium = new WebDriverBackedSeleniumProvider(driver, hostname);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.debug("Could not start selenium or the server because: "
//					+ e.getMessage());
//		}
		
		
		//selenium.open("http://ote.retail.melbourneit.com.au");
		//logger.debug(selenium.getHtmlSource());
//		WebElement nameid = driver.findElement(By.id("nameid"));
//		nameid.sendKeys("hello.com.au");
//		nameid.submit();
		selenium.open(hostname);
		selenium.type("nameid", "foobar");
		
//		selenium.type("nameid", "foobar");
//		selenium.assertTitle("Google2");
//		Thread.sleep(10000);
//		selenium.stop();
		driver.quit();
		selenium = null;
		driver = null;	
	}
}
