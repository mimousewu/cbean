package net.cbean.report.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cbean.report.ReportService;
import net.cbean.report.impl.AbstractReportService;
import net.cbean.report.impl.HtmlReportParser;
import net.cbean.report.model.Report;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.TableConfig;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DatabaseReportService extends AbstractReportService implements
		ReportService<Report> {
	static final Logger log = LogManager.getLogger(DatabaseReportService.class);

	@SuppressWarnings({ "rawtypes" })
	public Report getReport(String reportName, Map<String, Object> params) {
		Report report = new Report();
		ReportConfig config = getConfig(reportName);
		for (TableConfig tConf : config.getTables()) {
			String title = HtmlReportParser.parseTime(tConf.getTitle());
			report.getTableTitles().put(tConf.getId(), title);

			params.putAll(tConf.getParamsMap());

			if (tConf instanceof DatabaseTableConfig) {
				DatabaseTableConfig dbTConf = (DatabaseTableConfig) tConf;
				List<Map> reportDatas = dao.getReportData(
						dbTConf.getDataQuery(), params);
				report.getTables().put(tConf.getId(), reportDatas);
			}
		}
		report.setTitle(HtmlReportParser.parseTime(config.getTitle()));
		return report;
	}

	public Map<String, Integer> retrieveTableRows(String reportName,
			Map<String, Object> params) {
		Map<String, Integer> rows = new HashMap<String, Integer>();
		ReportConfig config = getConfig(reportName);
		for (TableConfig tConf : config.getTables()) {
			params.putAll(tConf.getParamsMap());

			if (tConf instanceof DatabaseTableConfig) {
				DatabaseTableConfig dbTConf = (DatabaseTableConfig) tConf;
				int size = dao.size(dbTConf.getRowCountQuery(), params);
				rows.put(tConf.getId(), size);
			}
		}
		return rows;
	}

}
