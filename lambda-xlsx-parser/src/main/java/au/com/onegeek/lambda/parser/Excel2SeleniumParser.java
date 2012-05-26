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
package au.com.onegeek.lambda.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;

import au.com.onegeek.lambda.api.Context;
import au.com.onegeek.lambda.api.DataProvider;
import au.com.onegeek.lambda.api.Plugin;
import au.com.onegeek.lambda.api.Test;
import au.com.onegeek.lambda.api.TestCommand;
import au.com.onegeek.lambda.api.TestProvider;
import au.com.onegeek.lambda.api.exception.CannotCreateDataProviderException;
import au.com.onegeek.lambda.api.exception.CannotCreateTestClassException;
import au.com.onegeek.lambda.api.exception.CannotModifyTestMethodException;
import au.com.onegeek.lambda.api.exception.UnableToParseDataException;
import au.com.onegeek.lambda.api.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.api.exception.VariableNotFoundException;
import au.com.onegeek.lambda.core.JavassistTestBuilderImpl;

@Configurable
public class Excel2SeleniumParser implements TestProvider, DataProvider, Plugin {
	private static final Logger logger = LoggerFactory.getLogger(Excel2SeleniumParser.class);
	
	/**
	 * Data set.
	 */
	protected List<Map<String, Object>> dataMap;
	
	/**
	 * Tests parsed from Excel spreadsheet.
	 */
	protected List<Class<Test>> tests;
	
	private void parse(InputStream stream) throws CannotCompileException, NotFoundException, CannotCreateTestClassException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		logger.debug("Parsing...");
		
		if (this.dataMap != null && this.tests != null) {
			return;
		}
		
		this.dataMap = new ArrayList<Map<String, Object>>();
		this.tests = new ArrayList<Class<Test>>();		
        
        Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("workbook" + workbook.toString());
        
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {        	
        	Sheet sheet = workbook.getSheetAt(i);
        	if (sheet.getSheetName().startsWith("data")) {
        		// parse key\value pairs
        		HashMap<String, Object> map = new HashMap<String, Object>();
        		dataMap.add(map);
        		boolean done = false;
        		
        		Row row = sheet.getRow(sheet.getFirstRowNum());
        		
        		while (!done && row != null && row.getPhysicalNumberOfCells() > 0) {
        			// TODO: parse numerics correctly (i.e. don't add decimal points if not needed)
        			String key = (String) XslxUtil.objectFrom(workbook, row.getCell(0));
        			String value = null;
        			try {
        				value = (String) XslxUtil.objectFrom(workbook, row.getCell(1));
        				logger.debug("Adding variable to map: " + key + ":" + value);
        				map.put(key, value);	
        				
        				row = sheet.getRow(row.getRowNum()+1);
        				
        				if (row == null || (row.getRowNum() == sheet.getLastRowNum() + 1)) {
        					done = true;
        				}
        			} catch (NullPointerException e) {
        				//throw new CannotCreateVariableException("No value found for variable '" + key + "' in dataset: " + sheet.getSheetName());
        				done = true;
        			}
        		}
        	}
        }
        
        JavassistTestBuilderImpl builder = JavassistTestBuilderImpl.getInstance();
        
        // Parse Test sheets into Test objects
        for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
	        Sheet sheet = workbook.getSheetAt(s);        
	        int i = 0;

	        // Ignore data sheets
	        if (sheet.getSheetName().startsWith("suite")) {
	        
	        	int maxRows = sheet.getPhysicalNumberOfRows();
	        	int currentRow = sheet.getFirstRowNum();
	        	logger.debug("Nr rows in sheet: " + maxRows);	        

	        	// Create Test Class
	        	String testCaseName = "Test" + Excel2SeleniumParser.toCamelCase(sheet.getSheetName());
	        	logger.debug("Creating Test class with name: " + testCaseName);
	        	builder.makeTestClass(testCaseName, this.dataMap);
	        	boolean testCaseInProgress = false;
	        	boolean dataProviderAdded = false;
	        	
		        // Get First row, containing the test name and the data to be injected
		        while (i < maxRows) {
		        	logger.debug("i: " + i);
		        	logger.debug("currentRow: " + currentRow);
		        	Row row = sheet.getRow(currentRow);
		        	TestCommand command = null;
		        			        	
		        	// Check for empty row
		        	if (row != null && row.getPhysicalNumberOfCells() != 0) {
		        		i++;
		        		
				        // Get Cells
		        		Iterator<Cell> iterator = row.cellIterator();
				        while(iterator.hasNext()) {
				        	Cell cell = iterator.next();
					        String cellValue = (cell == null || cell.toString() == "") ? "" : XslxUtil.objectFrom(workbook, cell).toString();		        	
					        logger.debug("Cell: " + cellValue);
					        
					        if (cellValue.startsWith("test")) {
				        		logger.debug("Test case found: " + cellValue + ". Creating Test Case");

				        		// Create new Test CASE
				        		try {				        			
				        			// Create new test case
									builder.addTest(cellValue);
									testCaseInProgress = true;
								} catch (VariableNotFoundException e) {
									// TODO Auto-generated catch block
									throw new CannotCreateTestClassException("Could not create Test Class as there was a variable not found in test assertion. Embedded exception: " + e.getMessage());
								}
				        		break;
				        	} else {
				        		if (command == null & !cellValue.equals("")) {
				        			logger.debug("Command found: " + cellValue + ". Creating new TestCommand"); 
				        			command = new TestCommand(cellValue);
				        		} else if (!cellValue.equals("")) {
				        			logger.debug("Command argument found: " + cellValue);
				        			command.addParameter(cellValue);
				        		}
				        	}
				        }
		        	} else {		        		
						// Blank row could mean a test case has just been completed
						// Complete last test case by adding a data provider
	        			if (testCaseInProgress && dataProviderAdded == false) {
	        				try {
	        					logger.debug("In Progress Test Case now being closed off and added to class...");
	        					builder.addDataProvider();
	        					dataProviderAdded = true;
	        					logger.debug("In Progress Test Case now closed off!");
	        				} catch (CannotCreateDataProviderException e) {
	        					throw new CannotCreateTestClassException("Could not create Test case as a DataProvider for the method could not be created. Embedded exception: " + e.getMessage());
	        				}
	        			}		        		
		        	}
		        	try {
		        		if (command != null) {
		        			logger.debug("Adding command to method");
		        			builder.appendTestToLastMethod(command);
		        		}
					} catch (CannotModifyTestMethodException e) {
						throw new CannotCreateTestClassException("Unable to add Test Case '" + command.toString() + "' to Test Class. Embedded exception: " + e.getMessage());
					} catch (VariableNotFoundException e) {
						throw new CannotCreateTestClassException("Unable to add Test Case '" + command.toString() + "' to Test Class as a variable was not found. Embedded exception: " + e.getMessage());
					}
				    currentRow++;
		        }
				// Blank row could mean a test case has just been completed
				// Complete last test case by adding a data provider
    			if (testCaseInProgress && dataProviderAdded == false) {
    				try {
    					logger.debug("In Progress Test Case now being closed off and added to class...");
    					builder.addDataProvider();
    					dataProviderAdded = true;
    					logger.debug("In Progress Test Case now closed off!");
    				} catch (CannotCreateDataProviderException e) {
    					throw new CannotCreateTestClassException("Could not create Test case as a DataProvider for the method could not be created. Embedded exception: " + e.getMessage());
    				}
    			}
    			
		        if (testCaseInProgress) {
		        	logger.debug("Generating class file");
		        	this.tests.add(builder.getCreatedClass());
		        	testCaseInProgress = false;
		        }
	        }
        }
        
        try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        logger.info("Looking at our classes...");
        
        // Look at the Test Objects
        for (Class<Test> clazz: tests) {
        	logger.info("Class: " + clazz.getName());
        	for (Method m : clazz.getMethods()) {
        		logger.info("Method: " + m);
        		if (m.getName().equalsIgnoreCase("testRetailDataProvider")) {
        			logger.info("invoking data provider");
        			Test test = clazz.newInstance();
        			Object[][] data = (Object[][]) m.invoke(test);
        			for (Object[] obs: data) {
        				for (Object o: obs) {
        					logger.info("data value: " + o);
        				}
        				
        			}
        		}
        	}
        }
	}
	
	@Override
	public Class<Test>[] parseTests(InputStream stream) {
		try {
			this.parse(stream);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			logger.info("cce e dataMap");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("nfe e dataMap");
			e.printStackTrace();
		} catch (CannotCreateTestClassException e) {
			logger.info("cctc e dataMap");
			e.printStackTrace();			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.info("illa e dataMap");
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.info("ia e dataMap");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.info("iae e dataMap");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.info("invocation e dataMap");
			e.printStackTrace();
		}
		
		logger.info("returning tests...");
        return this.tests.toArray(new Class[this.tests.size()]);
	}

	@Override
	public List <Map<String, Object>> parseDataSet(InputStream stream) {
		try {
			this.parse(stream);
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			logger.info("cce e dataMap");
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("nfe e dataMap");
			e.printStackTrace();
		} catch (CannotCreateTestClassException e) {
			logger.info("cctc e dataMap");
			e.printStackTrace();			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			logger.info("illa e dataMap");
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			logger.info("ia e dataMap");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.info("iae e dataMap");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.info("invocation e dataMap");
			e.printStackTrace();
		}
		
		logger.info("Returning dataMap");
		for (Map<String, Object> map: this.dataMap) {
			Iterator<String> i = map.keySet().iterator();
			while(i.hasNext()) {
				String key = i.next();
				logger.debug("DataMap: key" + key + ", value: " + map.get(key));
			}
		}
		
		return this.dataMap;
	}
	
	/**
	 * Convert a string to camel case for Java Class Naming conventions.
	 * 
	 * @param s The string to convert to camel case.
	 * @return The converted string.
	 */
	static String toCamelCase(String s) {
		// Remove illegal chars
		s = s.replaceAll("[^a-zA-Z0-9]*", "");
		
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part);
		}
		return camelCaseString;
	}

	/**
	 * Convert a String to proper-case.
	 * 
	 * @param s The string to convert to proper-case.
	 * @return The converted string.
	 */
	static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	@Override
	public void start(Context context) {
		// TODO Auto-generated method stub
		logger.debug("Starting Plugin: " + this.getClass().getName() + "...");
		
	}

	/**
	 * Return the file extensions the test provider provides.
	 * 
	 * @return
	 */
	@Override
	public String[] providesTestExtensions() {
		return new String[]{"xlsx"};
	}	
	/**
	 * Return the file extensions the data provider provides.
	 * 
	 * @return
	 */
	@Override
	public String[] providesDataExtensions() {
		return new String[]{"xlsx"};
	}	
}