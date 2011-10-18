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