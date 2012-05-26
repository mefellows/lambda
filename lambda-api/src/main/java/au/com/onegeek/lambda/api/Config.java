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

public interface Config {
	
    public static String VALUE_DIR_ROOT = ".";    
    public static final String KEY_PROP_FILE = "lambda.config.file"; // property name for property file.
    public static final String KEY_LAMBDA_HOME = "lambda.home";
    
    public static final String VALUE_DIR_CONF =  (
        System.getProperty(KEY_LAMBDA_HOME) != null ? System.getProperty(KEY_LAMBDA_HOME) + "/conf" : VALUE_DIR_ROOT + "/conf"
    );
    
    public static final String VALUE_DIR_PLUGINS = (
        System.getProperty(KEY_LAMBDA_HOME) != null ? System.getProperty(KEY_LAMBDA_HOME) + "/plugins" : VALUE_DIR_ROOT + "/plugins"
    );
    
    public static final String VALUE_DIR_LIB = (
        System.getProperty(KEY_LAMBDA_HOME) != null ? System.getProperty(KEY_LAMBDA_HOME) + "/lib" : VALUE_DIR_ROOT + "/lib"
    );
    
    /**
     * Reads a property from the loaded cli.properties file.
     * @param key the name of the property
     * @return an object representing property value
     */
    public Object getProperty(String key);
}