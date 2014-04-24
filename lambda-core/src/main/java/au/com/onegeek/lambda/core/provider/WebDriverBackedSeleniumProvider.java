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

import static org.testng.Assert.assertTrue;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.Context;
import au.com.onegeek.lambda.api.Plugin;

/**
 * Selenium Assertion Provider.
 * 
 * Implements the <code>IAssertionProvider</code> Interface meaning it 
 * can be used for test case keyword-driven tests.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public class WebDriverBackedSeleniumProvider extends WebDriverBackedSelenium implements AssertionProvider, Plugin, BeanDefinitionRegistryPostProcessor {
	private static final Logger logger = LoggerFactory.getLogger(WebDriverBackedSeleniumProvider.class);
	
	public WebDriverBackedSeleniumProvider(WebDriver baseDriver, String baseUrl) {
		super(baseDriver, baseUrl);
		// TODO Auto-generated constructor stub
	}
		
	public void assertTextPresent(String pattern) {
		logger.debug("assertTextPresent: " + pattern);
		assertTrue(this.isTextPresent(pattern));
	}
	public void assertTitle(String pattern) {
		logger.debug("assertTitle: " + pattern);
		assertTrue(this.getTitle().equals(pattern));
	}
	
	public void sleep(int seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000);
	}
	
	public void sleep(String wait) throws InterruptedException, ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        Number number = format.parse(wait);
		this.sleep(number.intValue());
	}
	
	public void clickAndWait(String id) throws ParseException {
		this.clickAndWait(id, "30");
	}
	
	public void clickAndWait(String id, String seconds) throws ParseException {
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		Number number = format.parse(seconds);
		this.click(id);
		this.waitForPageToLoad(String.valueOf(number.intValue() * 1000));
	}
	
	public void assertElementPresent(String locator) {
		assertTrue(this.isElementPresent(locator));
	}

	@Override
	public void start(Context context) {
		logger.debug("Starting Plugin: " + this.getClass().getName());
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(
			BeanDefinitionRegistry registry) throws BeansException {
	    BeanDefinition definition = new RootBeanDefinition(
	    		WebDriverBackedSeleniumProvider.class);

	    logger.debug("registering SeleniumBackedWebDriver bean...");
	    registry.registerBeanDefinition("webDriverBackedSeleniumProvider", definition);		
	}	
}