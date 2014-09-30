package net.cbean.report;

import java.util.Map;

import net.cbean.report.model.ReportConfig;

/**
 * The Report service handle report configuration and get report from database
 * or other persistence storage.
 * 
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 */
public interface ReportService<R> {
	/**
	 * Get report configuration by name
	 * 
	 * @param reportName
	 * @return
	 */
	ReportConfig getConfig(String reportName);

	/**
	 * Save report configuration
	 * 
	 * @param config
	 */
	void saveConfig(ReportConfig config);

	/**
	 * Get Report with parameters
	 * 
	 * @param reportName
	 * @return
	 */
	R getReport(String reportName, Map<String, Object> params);

}
