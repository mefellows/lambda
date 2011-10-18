package au.com.onegeek.lambda.core;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import au.com.onegeek.lambda.core.exception.UnableToParseDataException;

public interface IDataParser {

	/**
	 * Fetch the test data sets from an input source.
	 * 
	 * @param stream The input containing test data - may be a file, console input etc.
	 * @return The data sets, containing a list of key-value pairs.
	 * @throws UnableToParseDataException
	 */
	public List <Map<String, Object>> parseDataSet(InputStream stream) throws UnableToParseDataException;
}
