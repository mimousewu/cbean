package net.cbean.report.poi;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cbean.report.impl.AbstractReportService;
import net.cbean.report.model.ReportConfig;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SXSSFPOITableParserTest {
	String outputFile = "target/billreport.xlsx";
	POITableParser poiParser;
	ReportConfig repConfig;
	XSSFWorkbook tempWorkbook;
	
	@Before
	public void setUp() throws Exception {
		poiParser = new POITableParser();
		try {
			tempWorkbook = (XSSFWorkbook) WorkbookFactory
					.create(new FileInputStream("src/test/resources/billreport.xlsx"));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		AbstractReportService aps = new AbstractReportService();
		aps.addAnnotationClass(POITableConfig.class);
		aps.setPath("src/test/resources");
		repConfig = aps.getConfig("demoReport");
	}

	@After
	public void tearDown() throws Exception {
//		new File(outputFile).delete();
	}

	@Test
	public void testParse() throws Exception {
		List<Map<String,String>> values = new ArrayList<Map<String,String>>();
		Map<String, String> row1 = new HashMap<String, String>();
		row1.put("col1", "temp");
		row1.put("col2", "temp");
		row1.put("col3", "temp");
		values.add(row1);
		Map<String, String> row2 = new HashMap<String, String>();
		row2.put("col1", "temp");
		row2.put("col2", "temp");
		row2.put("col3", "temp");
		values.add(row2);
		
		POITableConfig config = (POITableConfig) repConfig.getTableMap().get("demoTable");
		Sheet tempSheet = tempWorkbook.getSheet(config.getTempSheet());
		poiParser.getCellStyles(tempSheet, config);

		int tempSheetIndex = tempWorkbook.getSheetIndex(config.getTempSheet());
		if (tempWorkbook.getSheetIndex(config.getTitle()) >= 0) {
			tempSheet = tempWorkbook.getSheet(config.getTitle());
		} else {
			tempSheet = tempWorkbook.cloneSheet(tempSheetIndex);
//			tempSheet = tempWorkbook.createSheet();
			tempWorkbook.setSheetName(tempWorkbook.getSheetIndex(tempSheet),
					config.getTitle());
		}
		saveOutputFile(tempWorkbook);
		
		XSSFWorkbook workbookT = (XSSFWorkbook) WorkbookFactory
				.create(new FileInputStream(outputFile));
		SXSSFWorkbook workbook = new SXSSFWorkbook(workbookT);		
		Sheet sheet = workbook.getSheet(config.getTitle());
		poiParser.parse(sheet, values, config);
		
		config.setStartRow(13);
		config.setStartColumn("A");
		poiParser.parse(sheet, values, config);
		saveOutputFile(workbook);
	}
	
	private void saveOutputFile(Workbook workbook) throws FileNotFoundException, IOException {
		FileOutputStream fOut = new FileOutputStream(outputFile);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
		if(workbook instanceof SXSSFWorkbook){
			((SXSSFWorkbook)workbook).dispose();
		}
		assertTrue(new File(outputFile).exists());
	}

}
