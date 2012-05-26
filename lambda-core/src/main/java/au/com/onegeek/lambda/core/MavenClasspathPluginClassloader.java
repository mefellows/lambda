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
package au.com.onegeek.lambda.core;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.onegeek.lambda.api.Config;

/**
 * No need to create a separate plugin classloader - all classes are available on the MavenClasspath
 * 
 * @author mfellows
 * 
 */
public class MavenClasspathPluginClassloader implements PluginClassloader {

	/**
	 * Class logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MavenClasspathPluginClassloader.class);

	@Override
	public ClassLoader getClassloader() {
		return this.getClass().getClassLoader();
	}
}
