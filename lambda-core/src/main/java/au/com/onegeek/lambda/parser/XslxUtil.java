package au.com.onegeek.lambda.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestXslxData {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        InputStream excelInputStream = new FileInputStream("/Users/mfellows/Desktop/test.xlsx");
        
        Workbook workbook = new XSSFWorkbook(excelInputStream);
        System.out.println("workbook" + workbook.toString());

        // Parse Data sheets into Data object (Map?)
        // Should variables start with $ or something?
        //Sheet sheet = workbook.getSheet("data");
        
        // Parse Test sheets        
        Sheet sheet = workbook.getSheetAt(0);        
        int maxRows = sheet.getPhysicalNumberOfRows();
        int i = 0;
        int currentRow = sheet.getFirstRowNum();
        System.out.println("Nr rows in sheet: " + maxRows);
        
        // Get First row, containing the test name and the data to be injected
        while (i < maxRows) {
        	System.out.println("i: " + i);
        	System.out.println("currentRow: " + currentRow);
        	Row row = sheet.getRow(currentRow);
        	
        	// Check for empty row
        	if (row != null && row.getPhysicalNumberOfCells() != 0) {
        		i++;
        		
		        // Get Cells
        		Iterator<Cell> iterator = row.cellIterator();
		        while(iterator.hasNext()) {
		        	Cell cell = iterator.next();
		        	System.out.println("Cell: " + TestXslxData.objectFrom(workbook, cell));
		        }
		        
		        // Create Test Case w\
		        
		        
        	}
		    currentRow++;
        }
        
        
        
//        System.out.println(excelInputStream);
//		Collection data = new SpreadsheetData(excelInputStream).getData();
//		
//		for (Object obj: data) {
//			System.out.println(obj);
//		}
	}
	
    public static Object objectFrom(final Workbook workbook, final Cell cell) {
        Object cellValue = null;

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            cellValue = cell.getRichStringCellValue().getString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cellValue = cell.getRichStringCellValue().getString();
            //cellValue = getNumericCellValue(cell);
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            cellValue = cell.getBooleanCellValue();
        } else if (cell.getCellType()  ==Cell.CELL_TYPE_FORMULA) {
            cellValue = evaluateCellFormula(workbook, cell);
        }

        return cellValue;
    
    }

    public static Object getNumericCellValue(final Cell cell) {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell)) {
            cellValue = new Date(cell.getDateCellValue().getTime());
        } else {
            cellValue = cell.getNumericCellValue();
        }
        return cellValue;
    }

    public static Object evaluateCellFormula(final Workbook workbook, final Cell cell) {
        FormulaEvaluator evaluator = workbook.getCreationHelper()
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        Object result = null;
        
        if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            result = cellValue.getBooleanValue();
        } else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            result = cellValue.getNumberValue();
        } else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
            result = cellValue.getStringValue();   
        }
        
        return result;
    }	
}