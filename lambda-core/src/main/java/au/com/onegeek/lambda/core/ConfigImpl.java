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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import au.com.onegeek.lambda.api.Config;
import au.com.onegeek.lambda.api.Context;

@Component
public class ConfigImpl implements Config {
	private static final Logger logger = LoggerFactory.getLogger(ConfigImpl.class);
	
	@Override
	public Object getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public ConfigImpl() {
		// TODO Auto-generated method stub
		logger.debug("Starting ConfigImpl plugin...");
	}		
}