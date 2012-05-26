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
/**
 * Copyright Substantiate 2012.
 */
package au.com.onegeek.lambda;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.api.Context;
import au.com.onegeek.lambda.api.DataProvider;
import au.com.onegeek.lambda.api.Plugin;
import au.com.onegeek.lambda.api.Test;
import au.com.onegeek.lambda.api.TestProvider;
import au.com.onegeek.lambda.api.exception.UnableToParseDataException;
import au.com.onegeek.lambda.api.exception.UnableToParseTestsException;

/**
 * @author mfellows
 *
 */
public class DummyProvider implements Plugin, TestProvider, DataProvider {
	private static final Logger logger = LoggerFactory.getLogger(DummyProvider.class);
	
	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.DataProvider#parseDataSet(java.io.InputStream)
	 */
	@Override
	public List<Map<String, Object>> parseDataSet(InputStream stream)
			throws UnableToParseDataException {
		return null;
	}

	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.DataProvider#providesDataExtensions()
	 */
	@Override
	public String[] providesDataExtensions() {
		return new String[]{"xlsx"};
	}

	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.TestProvider#parseTests(java.io.InputStream)
	 */
	@Override
	public Class<Test>[] parseTests(InputStream stream)
			throws UnableToParseTestsException {
		
		List<Class> tests = new ArrayList<Class>();
		
		Test t = new DummyTest();
		tests.add(t.getClass());
		
		return tests.toArray(new Class[tests.size()]);
	}

	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.TestProvider#providesTestExtensions()
	 */
	@Override
	public String[] providesTestExtensions() {
		return new String[]{"xlsx"};
	}

	/* (non-Javadoc)
	 * @see au.com.onegeek.lambda.api.Plugin#start(au.com.onegeek.lambda.api.Context)
	 */
	@Override
	public void start(Context context) {
		logger.info("starting DummyProvider");
	}

}
