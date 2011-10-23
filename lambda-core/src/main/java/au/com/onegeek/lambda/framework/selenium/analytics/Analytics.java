/*
 * #%L
 * Lambda Core
 * %%
 * Copyright (C) 2011 null
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
package au.com.onegeek.lambda.framework.selenium.analytics;

/**
 * Analytics interface.
 * 
 * A generic interface for testing website Analytics packages that is
 * provider agnostic.
 * 
 * @author Matt Fellows <matt.fellows@melbourneit.com.au>
 *
 */
public interface Analytics {
	/**
	 * Check the recorded page name. 
	 * 
	 * @param name The name of the page to confirm.
	 */
	public void assertPageName(String name);
	
	/**
	 * Check the server name recorded. 
	 *  
	 * @param server The server's name to confirm.
	 */
	public void assertServerName(String server);
	
	/**
	 * Check that the server name is simply set.
	 */
	public void assertServerNameNotNull();
	
	/**
	 * Check that an analytics variable has been set.
	 *  
	 * @param varName  The name of the variable to check.
	 * @param varValue The value of the variable to confirm.
	 */
	public void assertVariable(String varName, String varValue);
	
	/**
	 * Check that an analytics event has been fire.
	 *  
	 * @param varName  The name of the event to check.
	 * @param varValue The value of the event to confirm.
	 */	
	public void assertEvent(String eventName, String eventValue);
	
	/**
	 * Check that the correct analytics account is being used. 
	 * 
	 * @param account The name of the account.
	 */
	public void assertAccount(String account);
	
	/**
	 * Assert that a product exists as a line-item in an order.
	 * 
	 * @param product The name\id of the product in the basket.
	 */
	public void assertOrderProduct(String product);
	
	/**
	 * Assert that a product exists as a line-item in an order with a specified quantity.
	 * 
	 * @param product The name\id of the product in the basket.
	 * @param qty     The quantity of items.
	 */
	public void assertOrderProductQty(String product, int qty);
	
	/**
	 * Assert that a product exists as a line-item in an order with a specified quantity and total.
	 * 
	 * @param product The name\id of the product in the basket.
	 * @param qty     The quantity of items.
	 * @param total   The total cost of the line item.
	 */
	public void assertOrderProductQty(String product, int qty, float total);
	
	/**
	 * Assert the total value of the current order.
	 * 
	 * @param value The value of the order.
	 */
	public void assertOrderValue(float value);// throws org.testng.TestException;
}
