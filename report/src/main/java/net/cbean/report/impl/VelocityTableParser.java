/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: 0:54:59
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  VelocityTableParser.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: VelocityTableParser.java,v $  $Revision: 1.0 $  $Date: 0:54:59 $
 * $Log: VelocityTableParser.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.report.impl;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.cbean.report.ColumnParser;
import net.cbean.report.TableParser;
import net.cbean.report.model.TableColumn;
import net.cbean.report.model.TableConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.util.SimplePool;

/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 * 
 */
public class VelocityTableParser implements TableParser<String> {
	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final Log log = LogFactory.getLog(VelocityTableParser.class);

	private static SimplePool writerPool = new SimplePool(40);
	private Map<String, Template> templates = new HashMap<String, Template>();

	public VelocityTableParser(String templateConf) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(templateConf));
			for (Map.Entry<Object, Object> entry : p.entrySet()) {
				Template t = RuntimeSingleton.getTemplate(entry.getValue()
						.toString(), DEFAULT_ENCODING);
				templates.put(entry.getKey().toString(), t);
			}
		} catch (Exception e) {
			log.error("can't init velocitry configurations.", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.report.TableParser#parse(java.util.List,
	 * net.cbean.report.model.TableConfig)
	 */
	public String parse(List<?> reportList, TableConfig config) {
		VelocityContext context = new VelocityContext();
		context.put("columns", config.getLabels());

		List<List<Object>> tableData = new ArrayList<List<Object>>();
		for (Object line : reportList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> row = (Map<String, Object>) line;
			List<Object> rowData = new ArrayList<Object>();
			for (TableColumn column : config.getColumns()) {
				Object value = row.get(column.getName());
				rowData.add((value == null) ? "" : value);
			}
			tableData.add(rowData);
		}

		context.put("table", tableData);

		String fullTableName = config.getReportName() + "." + config.getId();
		Template temp = this.templates.get(fullTableName);
		if (temp == null) {
			temp = this.templates.get("default");
			// log.warn("can't find special velocity template from table "+fullTableName+" now use default template.");
		}
		StringWriter sw = new StringWriter();
		mergeTemplate(temp, context, sw);

		return sw.toString();
	}

	public void mergeTemplate(Template template, Context context, Writer writer) {
		VelocityWriter vw = null;
		try {
			vw = (VelocityWriter) writerPool.get();

			if (vw == null) {
				vw = new VelocityWriter(writer, 4 * 1024, true);
			} else {
				vw.recycle(writer);
			}

			template.merge(context, vw);
		} catch (Exception e) {
			log.error("use velocity to merge template faild.", e);
		} finally {
			try {
				if (vw != null) {
					/*
					 * flush and put back into the pool don't close to allow us
					 * to play nicely with others.
					 */
					vw.flush();

					/*
					 * Clear the VelocityWriter's reference to its internal
					 * OutputStreamWriter to allow the latter to be GC'd while
					 * vw is pooled.
					 */
					vw.recycle(null);

					writerPool.put(vw);
				}
			} catch (Exception e) {
				log.error("some happened when put to writerPool", e);
			}
		}
	}

	public void parse(String blankTable, List<?> values, TableConfig config) {
		throw new java.lang.IllegalAccessError("Simple type can not use this method!");
	}

	public void setColumnParser(String columnName, ColumnParser<?> colParser) {
		// TODO Auto-generated method stub
		
	}
}
