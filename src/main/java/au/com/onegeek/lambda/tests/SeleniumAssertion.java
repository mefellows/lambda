package au.com.onegeek.lambda.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.thoughtworks.selenium.Selenium;

public class SeleniumAssertion extends Assert {

	@Autowired
	private Selenium selenium;
	
	public void assertTextPresent(String pattern) {
		assertTrue(selenium.isTextPresent(pattern));
	}
}
