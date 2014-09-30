package net.cbean.report.dao;

import java.util.List;
import java.util.Map;

public interface ReportDao {
	int size(String query, Map<String, Object> params);

	@SuppressWarnings("rawtypes")
	List<Map> getReportData(String query, Map<String, Object> params);
}
