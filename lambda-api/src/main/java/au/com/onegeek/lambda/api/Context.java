/*
 * #%L
 * Lambda Core API
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
package au.com.onegeek.lambda.api;

import java.util.List;

/**
 * Lambda Context.
 * 
 * Provides framework environment\contextual parameters available to all plugins.
 * 
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public interface Context {

	public Config getConfig();
	
	public List<Plugin> getPlugins();
	
	public List<AssertionProvider> getAssertionProviders();
	
	public List<DataProvider> getDataProviders();
	
	public List<TestProvider> getTestProviders();

	public List<OutputConnector> getIOutputConnectors();
}
