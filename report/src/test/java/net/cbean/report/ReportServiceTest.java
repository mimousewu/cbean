package net.cbean.report;

import java.util.HashMap;
import java.util.Map;

import net.cbean.report.impl.AbstractReportService;
import net.cbean.report.model.Report;
import net.cbean.report.poi.BillingReportParser;
import net.cbean.report.poi.BillingReportService;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportServiceTest {
	ReportService ps;
	ReportParser<Workbook, Report> rp;

	@Before
	public void setUp() throws Exception {
		ps = new BillingReportService();
		((AbstractReportService)ps).setPath("src/test/resources");
		rp = new BillingReportParser();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		String reportName = "demoReport";
		Map<String, Object> params = new HashMap<String, Object>();
//		Report report = ps.getReport(reportName, params);
//		Workbook workbook = rp.parse(report, ps.getConfig(reportName));
	}

}
