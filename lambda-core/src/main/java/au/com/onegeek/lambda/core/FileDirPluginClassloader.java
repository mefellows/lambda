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
 * @author mfellows
 * 
 */
public class FileDirPluginClassloader implements PluginClassloader {

	/**
	 * Class logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FileDirPluginClassloader.class);
	
	/**
	 * The plugin classloader.
	 */
	private ClassLoader classLoader;
	
	/**
	 * The configuration object 
	 */
	@Autowired
	private Config configuration;
	
	@Override
	public ClassLoader getClassloader() {
		if (classLoader != null) {
			return classLoader; // return if already loaded.
		}

		File pluginsDir = new File(this.configuration.VALUE_DIR_PLUGINS);
		logger.debug("Plugin dir: " + pluginsDir);
		if (pluginsDir.exists() && pluginsDir.isDirectory()) {
			try {
				classLoader = PluginLoader.createClassLoaderForPath(
						new File[] { pluginsDir }, Thread.currentThread()
								.getContextClassLoader());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		} else {
			 throw new RuntimeException("Unable to find directory [plugins]. Lambda will stop.");
		}

		return classLoader;
	}

}
