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
