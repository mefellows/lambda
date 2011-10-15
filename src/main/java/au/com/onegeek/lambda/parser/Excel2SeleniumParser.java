package au.com.onegeek.lambda.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javassist.CtClass;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.onegeek.lambda.core.IDataParser;
import au.com.onegeek.lambda.core.ITestParser;
import au.com.onegeek.lambda.core.Test;
import au.com.onegeek.lambda.core.TestCommand;
import au.com.onegeek.lambda.core.TestBuilder;
import au.com.onegeek.lambda.core.exception.UnableToParseDataException;
import au.com.onegeek.lambda.core.exception.UnableToParseTestsException;
import au.com.onegeek.lambda.framework.selenium.analytics.Analytics;
import au.com.onegeek.lambda.tests.SeleniumAssertions;
import au.com.onegeek.lambda.tests.TestXslxData;

import com.thoughtworks.selenium.Selenium;

public class Excel2SeleniumParser implements ITestParser, IDataParser {
	private static final Logger logger = LoggerFactory.getLogger(Excel2SeleniumParser.class);
	
	/**
	 * The Analytics implementation specific to the site being tested.
	 */
	//@Autowired 
	private Analytics analytics;
	
	//@Autowired
	private SeleniumAssertions assertions;
	
	/**
	 * Data set.
	 */
	protected List <Map<String, Object>> dataMap;
	
	/**
	 * Tests parsed from Excel spreadsheet.
	 */
	protected List<Test> tests;
	
	public Excel2SeleniumParser() {
		this.dataMap = new ArrayList<HashMap<String, Object>>();
		this.tests = new ArrayList<Test>();
	}
	
	/**
	 * Selenium object.
	 *  
	 */
	protected Selenium selenium;
	
	private void parse(InputStream stream) {
		
		if (this.dataMap != null && this.tests != null) {
			return;
		}
        
        Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("workbook" + workbook.toString());

        // Parse Data sheets into Data object (Map?)
        List <HashMap<String, Object>> dataMap = new LinkedList<HashMap<String, Object>>();
        
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {        	
        	Sheet sheet = workbook.getSheetAt(i);
        	if (sheet.getSheetName().startsWith("data")) {
        		// parse key\value pairs
        		HashMap<String, Object> map = new HashMap<String, Object>();
        		dataMap.add(map);
        		boolean done = false;
        		
        		Row row = sheet.getRow(sheet.getFirstRowNum());
        		while (!done) {
        			// TODO: parse numerics correctly (i.e. don't add decimal points if not needed)
        			String key = (String) TestXslxData.objectFrom(workbook, row.getCell(0));
        			String value = (String) TestXslxData.objectFrom(workbook, row.getCell(1));
        			logger.debug("Adding variable to map: " + key + ":" + value);
        			map.put(key, value);	
        			
        			row = sheet.getRow(row.getRowNum()+1);
        					
        			if (row == null || (row.getRowNum() == sheet.getLastRowNum() + 1)) {
        				done = true;
        			}
        		}
        	}
        }
        
        // Temporary Test Class creation objects
        List<String> testClassNames = new ArrayList<String>();
        Map<String, List<String>> classToMethodMap = new HashMap<String, List<String>>();
        Map<String, List<TestCommand>> methodToTestCommandMap = new HashMap<String, List<TestCommand>>();
        String currentTestClassName = "";
        
        
        
        TestBuilder builder = new TestBuilder();
        
        // Parse Test sheets into Test objects
        for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
	        Sheet sheet = workbook.getSheetAt(s);        
	        int i = 0;

	        // Ignore data sheets
	        if (!sheet.getSheetName().startsWith("data")) {
	        
	        	int maxRows = sheet.getPhysicalNumberOfRows();
	        	int currentRow = sheet.getFirstRowNum();
	        	logger.debug("Nr rows in sheet: " + maxRows);	        

		        // Get First row, containing the test name and the data to be injected
		        while (i < maxRows) {
		        	logger.debug("i: " + i);
		        	logger.debug("currentRow: " + currentRow);
		        	Row row = sheet.getRow(currentRow);
		        	
		        	// Test Class
		        	Test testClass = null;
		        	CtClass testClassClass = null;
		        	Object testClass2 = null;
		        	
		        	// Check for empty row
		        	if (row != null && row.getPhysicalNumberOfCells() != 0) {
		        		i++;
		
		        		String command = null;
		        		Object[] args = new Object[3];
		        		int j = 0;
		        		
				        // Get Cells
		        		Iterator<Cell> iterator = row.cellIterator();
				        while(iterator.hasNext()) {
				        	Cell cell = iterator.next();
				        	logger.debug("Cell: " + TestXslxData.objectFrom(workbook, cell));
					        String cellValue = TestXslxData.objectFrom(workbook, cell).toString();		        	
					        
					        if (cellValue.startsWith("test")) {
				        		logger.debug("Test case found: " + cellValue);
				        		
				        		
				        		// Complete Creation of PREVIOUS test case
				        		
				        		
				        		// Create new Test class

				        		
				        		break;
				        		// TODO: create test case...
				        		
				        	} else {
				        		if (command == null) {
				        			logger.debug("Command found: " + cellValue); 
				        			command = cellValue;
				        		} else {
				        			logger.debug("Command argument found: " + cellValue);
				        			args[j] = cellValue;
				        			j++;
				        		}
				        	}
				        }
		        	}
				    currentRow++;
		        }
	        }
        }
        
        // Sheets have been parsed, now create all of the Test Cases with the Maps
        
        
        
	}
	
	@Override
	public Class<Test>[] parseTests(InputStream stream) throws UnableToParseTestsException {
		this.parse(stream);		
        return this.tests.toArray(new Class[this.tests.size()]);
	}

	@Override
	public List <Map<String, Object>> parseDataSet(InputStream stream) throws UnableToParseDataException {
		this.parse(stream);
		return this.dataMap;
	}
}