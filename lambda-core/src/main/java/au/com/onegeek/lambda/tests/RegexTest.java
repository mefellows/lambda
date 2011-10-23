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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

	public static void main(String[] args) {
        // TODO: make this better, it doesn't really extract out a variable name very well - regex would be much better...
        //       in fact, ${variable} is probably best as it aligns with JMeter...
        String regex = "(\\$[a-zA-Z]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher("id=$searchBox");

        if (m.groupCount() > 0) {
	        while(m.find()) {
	        	System.out.println("sub-match found: " + m.group());
	        }
        }
	}
}
