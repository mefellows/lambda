package au.com.onegeek.lambda.connector;

import org.testng.TestNG;

import au.com.onegeek.lambda.core.IOutputConnector;
import au.com.onegeek.lambda.core.Test;

public class QcConnector implements IOutputConnector {

	@Override
	public boolean publish(TestNG testng, Test[] tests) {
		// TODO Auto-generated method stub
		return false;
	}
}