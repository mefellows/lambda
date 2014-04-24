/*
 * #%L
 * Lambda XLSX Parser
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
/**
 * Copyright Substantiate 2012.
 */
package au.com.onegeek.lambda.extension.provider;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.Context;

/**
 * @author mfellows
 *
 */
public class MattAssertionProvider implements AssertionProvider {
	private static final Logger logger = LoggerFactory.getLogger(MattAssertionProvider.class);
	
	WebDriver driver;
	
	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.Plugin#start(au.com.onegeek.lambda.api.Context)
	 */
	@Override
	public void start(Context context) {
		// TODO Auto-generated method stub
		logger.debug("starting Matt's Assertion Provider...");
		this.driver = context.getDriver();
	}
	
	public void matt(String message) {
		logger.debug("Called Matt method!");
		String source = this.driver.getPageSource();
		
		Assert.isTrue(source.contains(message), "'" + message + "' was not found to be awesome in the DOM Source");
	}
}