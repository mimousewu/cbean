package net.cbean.report;

import net.cbean.report.model.ReportConfig;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 *
 * @param <T> report output, could be html string or excel workbook, or xml, or pdf
 */
public interface ReportParser<T, R> {
	/**
	 *Parse table to string
	 * @param tables
	 * @param config
	 * @return
	 */
	public T parse(R report, ReportConfig config);
}
