package au.com.onegeek.lambda.core;

/**
 * Test Parser Interface.
 * 
 * Any Input type (i.e. xlsx, xls, xml...) needs to match a parser. 
 * The matching process goes by:
 * 		1. name i.e. in the case of .xlsx file, the parser should be named XlsxParser
 * 		2. Registry lookup matching the 'extension' property of beans. 
 * 
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 */
import java.io.InputStream;

import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;

public interface ITestParser {

	/**
	 * Parse the Tests given an input stream.
	 * 
	 * @param stream The input containing test cases - may be a file, console input etc.
	 * @return Array of TestNG annotated classes containing Test methods.
	 */
	public Class<Test>[] parseTests(InputStream stream) throws UnableToParseTestsException;
}