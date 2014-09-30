package net.cbean.office;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PoiSheetHelperTest extends TestCase {

	private PipedInputStream in;
	private PipedOutputStream out;

	private Runnable runnable;

	public PoiSheetHelperTest() throws Exception {
		out = new PipedOutputStream();
		in = new PipedInputStream(out);

		runnable = new Runnable() {
			public void run() {
				addCells();
			}
		};

	}

	private void addCells() {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("exportsheet");

			addCell(sheet, 0, (short) 0, "Column1");
			addCell(sheet, 1, (short) 0, "Value1сп");
			addCell(sheet, 2, (short) 0, "Value11");
			addCell(sheet, 0, (short) 1, "Column2");
			addCell(sheet, 1, (short) 1, "Value2");
			addCell(sheet, 2, (short) 1, "Value22");
			workbook.write(out);
			out.close();
			
//			OutputStream fout = new FileOutputStream(new File("demo.xls"));
//			workbook.write(fout);
//			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addCell(HSSFSheet sheet, int rowc, short colc, String content) {
		HSSFRow row = sheet.createRow(rowc);
		HSSFCell cell = row.createCell(colc);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		HSSFRichTextString str = new HSSFRichTextString(content);
		cell.setCellValue(str);
	}

	public void testIterator() throws Exception {

		Runnable msg = new Runnable() {
			public void run() {
				sysout();
			}
		};

		new Thread(runnable).start();
//		new Thread(msg).start();
		sysout();

	}

	private void sysout() {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			SheetHelper helper = new PoiSheetHelper(workbook.getSheetAt(0),1);
			for (Iterator<Map<String, String>> iter = helper.iterator(); iter
					.hasNext();) {
				Map<String, String> entry = iter.next();
				for (Map.Entry<String, String> en : entry.entrySet()) {
					System.out.println(en.getKey() + " " + en.getValue());
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
