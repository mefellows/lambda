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
/**
 * 
 */
package au.com.onegeek.lambda.api;



/**
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public interface OutputConnector extends Plugin {

	/**
	 * Publish the Test Results to an endpoint system. 
	 * 
	 * @param testng The TestNG object used to harness the test system.
	 * @param tests  The set of au.com.onegeek.lambda.Test classes that have been run. 
	 * @return
	 */
	public boolean publish(org.testng.TestNG testng, Test[] tests);
}
