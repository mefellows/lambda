/**
 * Abstract BrowserTest class.
 * 
 * Java 6 
 * 
 * @version    CVS: $Id: BrowserTest.java,v 1.1 2010/09/16 02:03:26 mfellows Exp $
 * @deprecated As of 1.1, now replaced by the newer
 * @author     Matt Fellows <matt.fellows@melbourneit.com.au>
 * @copyright  2008-2010 Melbourne IT Ltd. (ABN 21 073 716 793)
 * @license    Proprietary
 * @category   Java
 * @link       http://www.melbourneit.com.au/
 */

/**
 * Abstract BrowserTest class.
 * 
 * Sets up BrowserTest environment, starts\stops Selenium server driver
 * and generates a bunch of methods to re-use.
 * 
 * NOTE: This is the OLD class and is now deprecated in favour of the newer WebDriver
 * API.
 * 
 * @version    CVS: $Id: BrowserTest.java,v 1.1 2010/09/16 02:03:26 mfellows Exp $
 * @deprecated
 * @author     Matt Fellows <matt.fellows@melbourneit.com.au>
 * @copyright  2008-2010 Melbourne IT Ltd. (ABN 21 073 716 793)
 * @license    Proprietary
 * @category   Java
 * @link       http://www.melbourneit.com.au/
 */

package au.com.onegeek.lambda.tests;

import org.testng.annotations.Test;

@Test(groups = "init")
public class BrowserTest extends TestWebDriver {
			
}