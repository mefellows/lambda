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
package au.com.onegeek.lambda.tests;

import java.security.SecureClassLoader;

/** Simple-minded loader for constructed classes. */
public class DirectClassLoader extends SecureClassLoader
{
    protected Class load(String name, byte[] data) {
        return super.defineClass(name, data, 0, data.length);
    }
}
