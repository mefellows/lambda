package au.com.onegeek.lambda.framework.selenium.analytics;

import static org.testng.Assert.fail;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * The Google Analytics implementation class.
 * 
 * 
 * 
 * @author Matt Fellows <matt.fellows@melbourneit.com.au>
 */
public class GoogleAnalyticsmpl implements Analytics {

	private static final Logger logger = LoggerFactory.getLogger(GoogleAnalyticsmpl.class);

	/**
	 * WebDriver object used to perform the tests.
	 */
	WebDriver driver;

	/**
	 * The JS executor responsible for all JS assertions.
	 */
	protected JavascriptExecutor js;

	public void setDriver(WebDriver driver) {
		this.driver = driver;
		this.init();
	}
	
	public void init() {
		this.js = (JavascriptExecutor) driver;		
		logger.debug("driver" + driver);
		logger.debug("js" + js);
		
		Assert.notNull(this.driver);
		Assert.notNull(this.js);
	}
	
	@Override
	public void assertPageName(String name) {
		// TODO Implement method		
		fail("Method not implemented");
	}

	@Override
	public void assertVariable(String varName, String varValue) {
		// TODO Implement method		
		fail("Method not implemented");	
	}

	@Override
	public void assertServerNameNotNull() {
		// TODO Implement method		
		fail("Method not implemented");		
	}

	@Override
	public void assertServerName(String server) {
		// TODO Implement method		
		fail("Method not implemented");
	}

	@Override
	public void assertEvent(String eventName, String eventValue) {
		// TODO Implement method
		fail("Method not implemented");
	}

	@Override
	public void assertAccount(String account) {
		// TODO Implement method
		fail("Method not implemented");
	}

	@Override
	public void assertOrderProduct(String product) {
		// TODO Implement method
		fail("Method not implemented");
	}

	@Override
	public void assertOrderProductQty(String product, int qty) {
		// TODO Implement method
		fail("Method not implemented");
	}

	@Override
	public void assertOrderValue(float value) {
		// TODO Implement method
		fail("Method not implemented");
	}
	
	@Override
	public void assertOrderProductQty(String product, int qty, float total) {
		// TODO Implement method		
		fail("Method not implemented");
	}	
}