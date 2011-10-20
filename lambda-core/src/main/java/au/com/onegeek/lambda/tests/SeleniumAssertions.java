package au.com.onegeek.lambda.tests;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.thoughtworks.selenium.Selenium;

public class SeleniumAssertions extends Assert {
	private static final Logger logger = LoggerFactory.getLogger(SeleniumAssertions.class);

	private Selenium selenium;

	public SeleniumAssertions(Selenium selenium) {
		this.selenium = selenium;
	}
	
	public void assertTextPresent(String pattern) {
		logger.debug("assertTextPresent: " + pattern);
		assertTrue(selenium.isTextPresent(pattern));
	}
	public void assertTitle(String pattern) {
		logger.debug("assertTitle: " + pattern);
		assertTrue(selenium.getTitle().equals(pattern));
	}
	
	public void sleep(int seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000);
	}
	
	public void sleep(String wait) throws InterruptedException, ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        Number number = format.parse(wait);
		this.sleep(number.intValue());
	}
	
	public void clickAndWait(String id) throws ParseException {
		this.clickAndWait(id, "30");
	}
	
	public void clickAndWait(String id, String seconds) throws ParseException {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		Number number = format.parse(seconds);
		selenium.click(id);
		selenium.waitForPageToLoad(String.valueOf(number.intValue() * 1000));
	}
	
	public void assertElementPresent(String locator) {
		assertTrue(selenium.isElementPresent(locator));
	}
}