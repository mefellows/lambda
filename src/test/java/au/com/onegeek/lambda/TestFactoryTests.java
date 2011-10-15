package au.com.onegeek.lambda;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.testng.annotations.Test;

import au.com.onegeek.lambda.core.CommandRunner;
import au.com.onegeek.lambda.core.TestCommand;
import au.com.onegeek.lambda.core.TestBuilder;
import au.com.onegeek.lambda.core.exception.VariableNotFoundException;

public class TestFactoryTests {
	@Test
	public void testClassCreation() throws CannotCompileException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, NotFoundException, VariableNotFoundException {

		Map dataSet = new HashMap();
		dataSet.put("foobar", "I am here to");
		TestBuilder f = new TestBuilder("au.com.onegeek.lambda.tests.runtimegenerated.eatmyTests", dataSet);
		CommandRunner runner = CommandRunner.getInstance();

		TestCommand command = new TestCommand("getBodyText");
		TestCommand command2 = new TestCommand("isTextPresent", "$foobar");

		List<TestCommand> commands = new ArrayList<TestCommand>();
		commands.add(command);
		commands.add(command2);

		
		f.addTest("testTheGoogle", commands);
		Object obj = f.getClassInstance();
		
		System.out.println("f's superclass: " + obj.getClass().getSuperclass());
		for(Method m : obj.getClass().getMethods()) {
			System.out.println("f method: " + m.getName() + ". toString: " + m.toGenericString());			
		}
		
		Method m = obj.getClass().getMethod("testTheGoogle");
		
		m.invoke(obj);
	}
}