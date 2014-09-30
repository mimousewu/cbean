package net.cbean.report.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Table")
public class TableConfig {

	@XStreamAsAttribute
	private String id;
	private String title;

	@XStreamAsAttribute
	private String reportName; // parent

	@XStreamAsAttribute
	private String parser;

	@XStreamImplicit
	private Set<ReportParam> params;

	@XStreamAlias("column-list")
	private List<TableColumn> columns;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<ReportParam> getParams() {
		return params;
	}

	public void setParams(Set<ReportParam> params) {
		this.params = params;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<TableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TableColumn> columns) {
		this.columns = columns;
	}

	public Map<String, Object> getParamsMap() {
		Map<String, Object> re = new HashMap<String, Object>();
		if (getParams() == null)
			return re;
		for (ReportParam param : getParams()) {
			re.put(param.getName(), param.getValue());
		}
		return re;
	}

	public List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		for (TableColumn col : this.columns) {
			labels.add(col.getLabel());
		}
		return labels;
	}

}
