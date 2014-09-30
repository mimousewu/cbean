package net.cbean.report.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Wu Tao
 * @E-Mail: mimousewu@gmail.com
 *
 */
public class Report {
	private String title;
	private Map<String, String> tableTitles = new HashMap<String, String>();
	private Map<String, List<?>> tables = new HashMap<String, List<?>>();
	private Map<String, Integer> tableRows;

	public Map<String, List<?>>  getTables() {
		return tables;
	}

	public void setTables(Map<String, List<?>> tables) {
		this.tables = tables;
		this.tableRows = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getTableTitles() {
		return tableTitles;
	}

	public void setTableTitles(Map<String, String> tableTitles) {
		this.tableTitles = tableTitles;
	}

	public Map<String, Integer> getTableRows() {
		if (tableRows != null)
			return tableRows;
		tableRows = new HashMap<String, Integer>();
		for (Map.Entry<String, List<?>> entry : tables.entrySet()) {
			tableRows.put(entry.getKey(), entry.getValue().size());
		}
		return tableRows;
	}
}
