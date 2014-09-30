package net.cbean.report.db;

import net.cbean.report.model.TableConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Table-Databse")
public class DatabaseTableConfig extends TableConfig {

	@XStreamAsAttribute
	private String dataQuery;
	
	@XStreamAsAttribute
	private String rowCountQuery;

	public String getDataQuery() {
		return dataQuery;
	}

	public void setDataQuery(String dataQuery) {
		this.dataQuery = dataQuery;
	}

	public String getRowCountQuery() {
		return rowCountQuery;
	}

	public void setRowCountQuery(String rowCountQuery) {
		this.rowCountQuery = rowCountQuery;
	}
}
