/**
 * 
 */
package net.cbean.report.impl;

import java.util.List;
import java.util.Map;

import net.cbean.report.ColumnParser;
import net.cbean.report.TableParser;
import net.cbean.report.model.TableColumn;
import net.cbean.report.model.TableConfig;


/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 *
 */
public class HtmlTableParser implements TableParser<String> {

	/*
	 * @see com.a8.log.report.Parser#parse(java.util.List)
	 */
	public String parse(List<?> reportList,TableConfig config) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table width=96% border=1 align='center' cellSpacing=0 borderColor=#CCCCCC style='border-collapse: collapse'>");

		buffer.append("<tr>");
		for(TableColumn column : config.getColumns()){
			buffer.append("<th>").append(column.getName()).append("</th>");
		}
		buffer.append("</tr>\n");
		for(Object item : reportList){
			buffer.append("<tr>");
			@SuppressWarnings("unchecked")
			Map<String, Object> rowMap = (Map<String, Object>) item;
			for(Map.Entry<String, Object> entry : rowMap.entrySet()){
				buffer.append("<td>");
				//TODO should parser to string
				buffer.append(entry.getValue());
				buffer.append("</td>");				
			}
			buffer.append("</tr>\n");
			
		}
		

		buffer.append("</table>");
		return buffer.toString();
	}

	public void parse(String blankTable, List<?> values, TableConfig config) {
		throw new java.lang.IllegalAccessError("Simple type can not use this method!");
	}

	public void setColumnParser(String columnName, ColumnParser<?> colParser) {
		// TODO Auto-generated method stub
		
	}

}
