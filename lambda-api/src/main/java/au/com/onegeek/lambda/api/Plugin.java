/*
 * #%L
 * Lambda Core API
 * %%
 * Copyright (C) 2011 OneGeek
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

/**
 * Lambda Plugin Interface. All Lambda extension points hinge on this class
 * for import into the framework.
 * 
 * Plugins go through the following lifecycle:
 * 
 * - 
 *  
 * @author Matt Fellows <matt.fellows@onegeek.com.au>
 *
 */
public interface Plugin {


	public void start(Context context);
}