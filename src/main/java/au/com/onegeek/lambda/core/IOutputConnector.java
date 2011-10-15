/**
 * 
 */
package au.com.onegeek.lambda.core;


/**
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public interface IOutputConnector {

	/**
	 * Publish the Test Results to an endpoint system. 
	 * 
	 * @param testng The TestNG object used to harness the test system.
	 * @param tests  The set of au.com.onegeek.lambda.Test classes that have been run. 
	 * @return
	 */
	public boolean publish(org.testng.TestNG testng, Test[] tests);
}