package au.com.onegeek.lambda.tests;

import java.security.SecureClassLoader;

/** Simple-minded loader for constructed classes. */
public class DirectClassLoader extends SecureClassLoader
{
    protected Class load(String name, byte[] data) {
        return super.defineClass(name, data, 0, data.length);
    }
}