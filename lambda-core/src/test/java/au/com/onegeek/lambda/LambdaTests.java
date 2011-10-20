package au.com.onegeek.lambda;

import org.testng.annotations.Test;

import au.com.onegeek.lambda.core.Lambda;
import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;

public class LambdaTests {

	@Test
	public void testLambda() throws UnableToParseTestsException, UnableToParseDataException {
		Lambda lambda = Lambda.getInstance();
		
		lambda.setBrowser("firefox");
		lambda.setHostname("http://retail.stage.mit");
		lambda.setTestSuiteFilename("/Users/mfellows/Desktop/retail.xlsx");
		lambda.run();
	}
}
