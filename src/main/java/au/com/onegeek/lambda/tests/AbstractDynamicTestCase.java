package au.com.onegeek.lambda.tests;

import au.com.onegeek.lambda.framework.selenium.analytics.Analytics;

import com.thoughtworks.selenium.Selenium;

public abstract class AbstractDynamicTestCase {

	private Selenium selenium;
	private Analytics analytics;
	
	public abstract void executeTestCase();
}
