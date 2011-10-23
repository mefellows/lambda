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
package au.com.onegeek.lambda.connector;

import org.testng.TestNG;

import au.com.onegeek.lambda.core.IOutputConnector;
import au.com.onegeek.lambda.core.Test;

public class QcConnector implements IOutputConnector {

	@Override
	public boolean publish(TestNG testng, Test[] tests) {
		// TODO Auto-generated method stub
		return false;
	}
}
