package net.cbean.report.poi;

import static org.junit.Assert.assertNotNull;
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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class POITableParserTest {
	String outputFile = "target/test.xls";
	POITableParser poiParser;
	ReportConfig repConfig;
	HSSFWorkbook workbook;

	@Before
	public void setUp() throws Exception {
		poiParser = new POITableParser();
		workbook = new HSSFWorkbook();
		poiParser.setWorkbook(workbook );
		
		AbstractReportService aps = new AbstractReportService();
		aps.addAnnotationClass(POITableConfig.class);
		aps.setPath("src/test/resources");
		repConfig = aps.getConfig("demoReport");
	}

	@After
	public void tearDown() throws Exception {
		new File(outputFile).delete();
	}

	@Test
	public void test() throws Exception{
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
		config.setTempSheet("temp");
		config.setStartRow(1);
		Sheet sheet = poiParser.parse(values, config );
		assertNotNull(sheet);
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(HSSFColor.BLUE.index);
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short)20);
		style.setFont(font );
		sheet.getRow(config.getStartRowIndex()).getCell(0).setCellStyle(style);
		sheet.getRow(config.getStartRowIndex()+1).getCell(1).setCellStyle(style);
		
		sheet.createRow(7).createCell(0).setCellStyle(style);
		
		Cell cell = sheet.getRow(config.getStartRowIndex()+1).createCell(2);
		cell.setCellValue("Added Cell");
		cell.setCellStyle(style);
		workbook.setSheetName(workbook.getSheetIndex(sheet), "temp");
		//Save template
		saveOutputFile();
		
		POIFSFileSystem fis = new POIFSFileSystem(new FileInputStream(outputFile));
		row1.put("col1", "Value 1");
		row1.put("col2", "Value 2");
		row1.put("col3", "Value 3");
		
		workbook = new HSSFWorkbook(fis);
		poiParser.setWorkbook(workbook);
		sheet = poiParser.parse(values, config);
		
		config.setStartRow(6);
		Map<String, String> rowX = new HashMap<String, String>();
		rowX.put("col1", "Other Value 1");
		rowX.put("col2", "Other Value 2");
		rowX.put("col3", "Other Value 3");
		values.add(rowX);
		config.setVertical(true);
		config.getColumns().get(2).setHidden(false);
		sheet = poiParser.parse(values, config);
		
		saveOutputFile();
	}

	private void saveOutputFile() throws FileNotFoundException, IOException {
		FileOutputStream fOut = new FileOutputStream(outputFile);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();
		assertTrue(new File(outputFile).exists());
	}

}
