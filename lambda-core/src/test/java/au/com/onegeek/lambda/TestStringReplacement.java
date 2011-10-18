package au.com.onegeek.lambda;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.util.SystemOutLogger;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtField.Initializer;
import javassist.NotFoundException;

import au.com.onegeek.lambda.core.exception.VariableNotFoundException;

public class TestStringReplacement {

	/**
	 * @param args
	 * @throws NotFoundException 
	 * @throws CannotCompileException 
	 * @throws IllegalAccessException, Exception 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, Exception {
		ClassPool _classPool = ClassPool.getDefault();		
		_classPool.importPackage("org.testng");
		_classPool.importPackage("org.testng.annotations");
		_classPool.importPackage("au.com.onegeek.lambda.core");
		_classPool.importPackage("org.slf4j");
		CtClass ctClass = _classPool.makeClass("TestClassTest");
		
		int[] sizes = new int[]{2, 5};
		LinkedHashMap<String, Object> methodVariableToParameterMap = new LinkedHashMap<String, Object>();
		methodVariableToParameterMap.put("foobar","cheese");
		methodVariableToParameterMap.put("foobar2","cheese");
		
		//Initializer init = CtField.Initializer.byNewArray(CtClass.intType, sizes);
//		Initializer init = CtField.Initializer.byNewArray(CtClass.intType, sizes);
		String[] ar = new String[methodVariableToParameterMap.size()];
		for (String key : methodVariableToParameterMap.keySet().toArray(ar)) {
			System.out.println("key: " + key);
		}
		//String[] keys = (String[]) methodVariableToParameterMap.keySet().toArray();
		
		//Initializer init = CtField.Initializer.byNewWithParams(objectType, stringParams)
		//CtField field = new CtField(CtClass.intType, "_" + this._ctMethod.getName() + "DataProvider", _ctClass);
		//CtField field = CtField.make("private int[][] tetDataProvider = new int[2][5];", ctClass);
		Initializer init = CtField.Initializer.byCallWithParams(_classPool.get("au.com.onegeek.lambda.core.StaticDataProvider"), "getData", ar);
		CtField field = CtField.make("private int[][] tetDataProvider;", ctClass);
	
		ctClass.addField(field, init);
		
		CtMethod testMethod = CtNewMethod.make(
				"public Object[][] testDataProvider () { " + 
//							"int[] foo = {1,1,1,1,1};" +
//							"int[] foo2 = {2,2,1,1,1};" +
//							"this.tetDataProvider[0] = foo;" +
//							"this.tetDataProvider[1] = foo2;" +
							"for (int i =0; i < 2; i++){ for (int j=0; j<5;j++) {System.out.println(this.tetDataProvider[i][j]);} }" +
							"return null;" +
							//"return this._" + this._ctMethod.getName() + "DataProvider" +						
						"}",

				ctClass);
		ctClass.addMethod(testMethod);
		
		Object obj = ctClass.toClass().newInstance();
		Method m = obj.getClass().getMethod("testDataProvider");
		m.invoke(obj);
		
		/*
		System.out.println("this is a variable " + TestStringReplacement.replaceVar("embedded in a string $foobar that happens to be ") + " inside another string");
		System.out.println("this is a variable " + TestStringReplacement.replaceVar("$foobar") + " inside another string");
		System.out.println(TestStringReplacement.replaceVar("$foobar"));
		
		Object[][] data = new Object[5][10];
		
		String dataRepresentation = "new Object[][]{\n";
		for (int i = 0; i < 5; i++) {
			if (i != 0) {
				dataRepresentation += ",\n";
			}
			dataRepresentation += "\t{";
			for (int j = 0; j < 10; j++) {
				if (j != 0) {
					dataRepresentation += ",";					
				}
				data[i][j] = i*j;
				dataRepresentation += data[i][j];					
			}
			dataRepresentation += "}";
		}
		dataRepresentation += "\n}";
		System.out.println(dataRepresentation);
		*/
	}
	
	public static void fooBar(String arg1, Object arg2) {
		
	}

	public static String replaceVar(String value) {
        String regex = "(\\$[a-zA-Z]*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);

        if (m.groupCount() > 0) {
	        while (m.find()) {
        		String match = m.group();
        		
        		// $variable style vars
        		try {
        			value = value.replace(match, "\" + " + match.substring(1) + " + \"");        			
        		} catch (NullPointerException e) {
        			
        		}
        	}
        }
        return value;
	}
}