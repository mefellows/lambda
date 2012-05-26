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
package au.com.onegeek.lambda.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import au.com.onegeek.lambda.api.AssertionProvider;
import au.com.onegeek.lambda.api.Config;
import au.com.onegeek.lambda.api.Context;
import au.com.onegeek.lambda.api.DataProvider;
import au.com.onegeek.lambda.api.OutputConnector;
import au.com.onegeek.lambda.api.Plugin;
import au.com.onegeek.lambda.api.TestProvider;

@Component
public class LambdaContext implements Context {
	private static final Logger logger = LoggerFactory.getLogger(LambdaContext.class);
	
	public LambdaContext() {
		logger.debug("LambdaContext starting...");
	}
	
	@Override
	public Config getConfig() {
		logger.debug("get Config() ");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Plugin> getPlugins() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AssertionProvider> getAssertionProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataProvider> getDataProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TestProvider> getTestProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OutputConnector> getIOutputConnectors() {
		// TODO Auto-generated method stub
		return null;
	}

}