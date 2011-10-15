package au.com.onegeek.lambda.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
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
}