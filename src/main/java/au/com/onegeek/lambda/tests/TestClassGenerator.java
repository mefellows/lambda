package au.com.onegeek.lambda.tests;

import java.util.ArrayList;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class TestClassGenerator {

	/** Parameter types for call with no parameters. */
	private static final CtClass[] NO_ARGS = {};

	/** Parameter types for call with single int value. */
	private static final CtClass[] INT_ARGS = { CtClass.intType };

	/**
	 * @param args
	 */
	public Class makeClass() throws Exception {
		ArrayList<Class> testClasses = new ArrayList<Class>();

		ClassPool pool = ClassPool.getDefault();
		pool.importPackage("org.testng");
		pool.importPackage("org.testng.annotations");

		// Name of class to create - this is the name of the tab
		String testClassName = "RetailTestSuite";

		// Name of the method to create - this is the test case in the
		// spreadsheet
		String testMethodName = "testHomePage";

		// Create the class container to add methods to
		CtClass evalClass = pool.makeClass(testClassName);

		// @TODO: Should this class implement an interface of sorts for use
		// later on?

		// @TODO: Inject in class members: Selenium, assertions

		// Create an @Test annotation on the methods to fire
		AnnotationsAttribute attr = new AnnotationsAttribute(evalClass
				.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation("org.testng.annotations.Test",
				evalClass.getClassFile().getConstPool());
		attr.addAnnotation(annot);

		// Create the test method
		CtMethod testMethod = CtNewMethod.make("public void " + testMethodName
				+ " () { System.out.println(\"Running DYNAMIC "
				+ testMethodName + " method\"); Assert.assertTrue(true); }",
				evalClass);
		testMethod.getMethodInfo().addAttribute(attr);
		evalClass.getClassFile().addAttribute(attr);
		evalClass.addMethod(testMethod);

		Class clazz = evalClass.toClass();		
		
		return clazz;
	}
}