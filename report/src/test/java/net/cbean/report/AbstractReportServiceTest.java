package net.cbean.report;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.cbean.report.impl.AbstractReportService;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.ReportParam;
import net.cbean.report.model.TableColumn;
import net.cbean.report.model.TableConfig;
import net.cbean.report.poi.POITableConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractReportServiceTest {
	private AbstractReportService aps;
	
	@Before
	public void setUp() throws Exception {
		aps = new AbstractReportService();
		aps.setPath("target/report");
		new File("target/report").mkdirs();
	}

	@After
	public void tearDown() throws Exception {
		new File("target/report").delete();
	}

	@Test
	public void testSaveThenGetConfig() {
		ReportConfig config = new ReportConfig();
		config.setName("myreport");
		config.setTitle("My Report");
		config.setTemplate("Some thing");
		config.setMailFrom("mailFrom");
		Set<String> attachments = new HashSet<String>();
		attachments.add("attachement.file");
		config.setAttachments(attachments);
		
		List<TableConfig> tables = new ArrayList<TableConfig>();
//		aps.addAnnotationClass(DatabaseTableConfig.class);
		TableConfig table = new TableConfig();
		table.setId("table1");
		table.setTitle("Table 1");
		table.setParser("tableParser");
		Set<ReportParam> params = new HashSet<ReportParam>();
		ReportParam param = new ReportParam();
		param.setName("startTime");
		param.setType("date");
		param.setInnerValue("default");
		params.add(param);
		table.setParams(params);
		
		List<TableColumn> columns = new ArrayList<TableColumn>();
		TableColumn col = new TableColumn();
		col.setName("col1");
		col.setLabel("Column 1");
		col.setParser("colParser");
		col.setType("String");
		columns.add(col);
		table.setColumns(columns);
		tables.add(table);
		config.setTables(tables);
		aps.saveConfig(config);
		File savedFile = new File("target/report/myreport.xml");
		assertTrue(savedFile.exists());
		
		ReportConfig out = aps.getConfig(config.getName());
		assertNotNull(out);
		savedFile.delete();
	}

	@Test
	public void testGetConfig() {
		aps.setPath("src/test/resources");
		aps.addAnnotationClass(POITableConfig.class);
		ReportConfig out = aps.getConfig("demoReport");
		assertNotNull(out);
	}
}
