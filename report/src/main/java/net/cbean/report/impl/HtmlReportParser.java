package net.cbean.report.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cbean.report.ReportParser;
import net.cbean.report.TableParser;
import net.cbean.report.model.Report;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.TableConfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 */
public class HtmlReportParser implements ReportParser<String, Report>,
		ApplicationContextAware {
	public static final Pattern timeParseReg = Pattern
			.compile("%d\\{(.*?)[\\[]*([yMdHms]*)([+-]*)([\\d]*)[\\]]*\\}");
	public static final Pattern paramParseReg = Pattern
			.compile("#(.*?)\\.\\{(.*?)\\}#");
	public static final Pattern titleParseReg = Pattern
			.compile("#(.*?)\\.title#");
	public static final Pattern bodyParseReg = Pattern
			.compile("#(.*?)\\.body#");
	private ApplicationContext applicationContext;

	public String parse(Report report, ReportConfig config) {
		StringBuffer sb = parseParam(config.getTemplate(), config);
		sb = parseTitle(sb, config);
		String re = parseTime(sb.toString());
		return parseBody(re, report.getTables(), config).toString();
	}

	public static String parseTime(String template) {
		Matcher m = null;
		CharSequence inputStr = template.subSequence(0, template.length());
		m = timeParseReg.matcher(inputStr);

		StringBuffer sb = new StringBuffer();
		boolean found = m.find();
		while (found) {
			Calendar c = Calendar.getInstance();
			if (m.groupCount() > 1 && m.group(4).length() > 0) {
				setOffset(c, m.group(2), m.group(3),
						Integer.parseInt(m.group(4)));
			}
			String timeStr = new SimpleDateFormat(m.group(1)).format(c
					.getTime());
			m.appendReplacement(sb, timeStr);
			found = m.find();
		}
		return m.appendTail(sb).toString();
	}

	private static void setOffset(Calendar c, String pattern,
			String experssion, int offset) {
		if (experssion.equals("-"))
			offset = -offset;
		if (pattern.equals("s")) {
			c.set(Calendar.SECOND, c.get(Calendar.SECOND) + offset);
		} else if (pattern.equals("m")) {
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + offset);
		} else if (pattern.equals("H")) {
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + offset);
		} else if (pattern.equals("d")) {
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + offset);
		} else if (pattern.equals("M")) {
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + offset);
		} else if (pattern.equals("y")) {
			c.set(Calendar.YEAR, c.get(Calendar.YEAR) + offset);
		}
	}

	private StringBuffer parseTitle(StringBuffer template, ReportConfig config) {
		Matcher m = null;
		CharSequence inputStr = template.subSequence(0, template.length());
		m = titleParseReg.matcher(inputStr);

		StringBuffer sb = new StringBuffer();
		boolean found = m.find();
		while (found) {
			String title;
			if ("Main".equals(m.group(1))) {
				title = config.getTitle();
			} else {
				title = getTableTitle(m.group(1), config.getTables());
			}
			m.appendReplacement(sb, title);
			found = m.find();
		}
		return m.appendTail(sb);
	}

	private static String getTableTitle(String id,
			final List<TableConfig> tableConfs) {
		for (Iterator<TableConfig> iter = tableConfs.iterator(); iter.hasNext();) {
			TableConfig conf = (TableConfig) iter.next();
			if (id.equals(conf.getId())) {
				return conf.getTitle();
			}
		}
		return null;
	}

	private StringBuffer parseParam(String template, ReportConfig config) {
		Matcher m = null;
		CharSequence inputStr = template.subSequence(0, template.length());
		m = paramParseReg.matcher(inputStr);

		StringBuffer sb = new StringBuffer();
		boolean found = m.find();
		while (found) {
			String paramValue = getParamValue(m.group(1), m.group(2),
					config.getTables());
			m.appendReplacement(sb, paramValue);
			found = m.find();
		}
		return m.appendTail(sb);
	}

	private String getParamValue(String tabId, String paramKey,
			final List<TableConfig> tableConfs) {
		for (TableConfig conf : tableConfs) {
			if (tabId.equals(conf.getId())) {
				return (String) conf.getParamsMap().get(paramKey);
			}
		}
		return null;
	}

	private StringBuffer parseBody(String template,
			Map<String, List<?>> tables, ReportConfig config) {
		Matcher m = null;
		CharSequence inputStr = template.subSequence(0, template.length());
		m = bodyParseReg.matcher(inputStr);

		StringBuffer sb = new StringBuffer();
		boolean found = m.find();
		while (found) {
			List<?> t = tables.get(m.group(1));
			TableConfig tableConf = config.getTableMap().get(m.group(1));

			@SuppressWarnings("unchecked")
			TableParser<String> parser = applicationContext.getBean(
					tableConf.getParser(), TableParser.class);
			String result = parser.parse(t, tableConf);
			m.appendReplacement(sb, result);
			found = m.find();
		}
		return m.appendTail(sb);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
