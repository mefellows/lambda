package au.com.onegeek.lambda.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Supplier;

public class MITWebDriver extends WebDriverBackedSelenium {

	public MITWebDriver(Supplier<WebDriver> maker, String baseUrl) {
		super(maker, baseUrl);
	}

	/**
	 * The current browser driver.
	 */
	@Autowired
	protected WebDriver driver;


}
