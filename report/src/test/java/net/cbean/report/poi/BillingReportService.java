package net.cbean.report.poi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cbean.report.ReportService;
import net.cbean.report.dao.ReportDao;
import net.cbean.report.impl.AbstractReportService;
import net.cbean.report.model.Report;
import net.cbean.report.model.ReportConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BillingReportService extends AbstractReportService implements
		ReportService<Report> {

	@Autowired
	ReportDao dao;
	
	@Autowired
//	CustomerDao cusDao;

	public BillingReportService() {
		super();
		addAnnotationClass(POITableConfig.class);
	}

	public Report getReport(String reportName, Map<String, Object> params) {
		Report report = new Report();
		ReportConfig config = this.getConfig(reportName);
		report.setTitle(config.getTitle());

		Map<String, List<?>> tables = new HashMap<String, List<?>>();
		Map<String, String> tableTitles = new HashMap<String, String>();
//		for (Customer customer : cusDao.search(null, null, null, null, null,
//				null)) {
//			params.put("customerId", customer.getCustomerId());
//			
//			List<?> data = dao.getReportData("customerTable", params);
//			String key = customer.getCustomerId()+"_detail";
//			tables.put(key, data);
//			tableTitles.put(key, "customerTable");
//			
//			data = dao.getReportData("customerSumTable", params);
//			key = customer.getCustomerId()+"_sum";
//			tables.put(key, data);
//			tableTitles.put(key, "customerSumTable");
//			
//			data = dao.getReportData("customerContractTable", params);
//			key = customer.getCustomerId()+"_contract";
//			tables.put(key, data);
//			tableTitles.put(key, "customerContractTable");
//		}

		tables.put("sumTotalTable", dao.getReportData("sumTotalTable", params));
		tableTitles.put("sumTotalTable", "sumTotalTable");
		
		report.setTables(tables);
		report.setTableTitles(tableTitles);

		return report;
	}

	public Map<String, Integer> retrieveTableRows(String reportName,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDao(ReportDao dao) {
		this.dao = dao;
	}

//	public void setCusDao(CustomerDao cusDao) {
//		this.cusDao = cusDao;
//	}

}
