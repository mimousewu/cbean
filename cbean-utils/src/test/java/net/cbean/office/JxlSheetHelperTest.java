package net.cbean.office;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;
import java.util.Map;

import net.cbean.office.JxlSheetHelper;

import junit.framework.TestCase;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class JxlSheetHelperTest extends TestCase {

	private PipedInputStream in;
	private PipedOutputStream out;
	
	private Runnable runnable;

	public JxlSheetHelperTest() throws Exception {
		out = new PipedOutputStream();
		in = new PipedInputStream();

		runnable = new Runnable() {

			public void run() {
				try {
					WritableWorkbook book = Workbook.createWorkbook(out);
					book.createSheet("exportsheet", 0);
					WritableSheet sh = book.getSheet(0);

					Label cell = new Label(0, 0, "Column1");
					sh.addCell(cell);

					cell = new Label(1, 0, "Column2");
					sh.addCell(cell);
					book.write();

					out.close();
				} catch (Exception e) {

				}
			}
		};

	}
	
	public void testIterator() throws Exception {
		
	}


//	public void testIterator() throws Exception {
//		out.connect(in);
//		new Thread(runnable).start();
//		
//		Workbook workbook = Workbook.getWorkbook(in);
//		SheetHelper helper = new JxlSheetHelper(workbook.getSheet(0));
//		for (Iterator<Map<String, String>> iter = helper.iterator(); iter
//				.hasNext();) {
//			Map<String, String> entry = iter.next();
//			for (Map.Entry<String, String> en : entry.entrySet()) {
//				System.out.println(en.getKey() + " " + en.getValue());
//			}
//		}
//	}

}
