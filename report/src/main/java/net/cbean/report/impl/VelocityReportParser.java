/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 0:31:04
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  VelocityReportParser.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: VelocityReportParser.java,v $  $Revision: 1.0 $  $Date: 0:31:04 $
 * $Log: VelocityReportParser.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.report.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Map.Entry;

import net.cbean.report.ReportParser;
import net.cbean.report.TableParser;
import net.cbean.report.model.Report;
import net.cbean.report.model.ReportConfig;
import net.cbean.report.model.TableConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 */
public class VelocityReportParser implements ReportParser<String, Report> {
	private static final Log log = LogFactory
			.getLog(VelocityReportParser.class);

	private TableParser<String> tableParser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.report.ReportParser#parse(net.cbean.report.model.Report,
	 * net.cbean.report.model.ReportConfig)
	 */
	public String parse(Report report, ReportConfig config) {
		VelocityContext context = new VelocityContext();
		context.put("MainTitle", report.getTitle());
		for (Entry<String, List<?>> entry : report.getTables().entrySet()) {
			context.put(entry.getKey() + "_title",
					report.getTableTitles().get(entry.getKey()));
			TableConfig tabConf = config.getTableMap().get(entry);
			String tableValue = tableParser.parse(entry.getValue(), tabConf);
			context.put(entry.getKey() + "_body", tableValue);
		}

		StringWriter w = new StringWriter();
		String template = config.getTemplate();
		try {
			Velocity.evaluate(context, w, "report", template);
		} catch (Exception e) {
			log.error(e);
		}
		return w.toString();
	}

}
