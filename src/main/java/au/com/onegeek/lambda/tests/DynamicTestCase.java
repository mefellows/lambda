package au.com.onegeek.lambda.tests;

import org.testng.annotations.Test;

import au.com.onegeek.lambda.framework.selenium.analytics.Analytics;

import com.thoughtworks.selenium.Selenium;

@Test
public class DynamicTestCase {

	private Selenium selenium;
	private Analytics analytics;
	
	@Test
	public void executeTestCase() {
		System.out.println("Output from SOURCE object");
	}
}