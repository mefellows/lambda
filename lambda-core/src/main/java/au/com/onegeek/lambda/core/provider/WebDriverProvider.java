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
package au.com.onegeek.lambda.core.provider;

import static org.testng.Assert.*;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.Context;

/**
 * WebDriver Assertion Provider. Provides an interface into the WebDriver
 * specification.
 * 
 * Implements the <code>IAssertionProvider</code> Interface meaning it 
 * can be used for test case keyword-driven tests.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public class WebDriverProvider implements AssertionProvider {
	private static final Logger logger = LoggerFactory.getLogger(WebDriverProvider.class);
	
	private WebDriver driver;

	@Override
	public void start(Context context) {
		// TODO Auto-generated method stub
		
	}
}