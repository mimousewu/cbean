package net.cbean.report.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Report-Configuration")
public class ReportConfig {
	@XStreamAsAttribute
	private String name;
	
	private String mailFrom;
	private String title;
	
	@XStreamImplicit
	private List<TableConfig> tables;
	private String template;

	@XStreamImplicit(itemFieldName="attachment")
	private Set<String> attachments;
	
	@XStreamAlias("reports-email")
	private Set<String> reportsTo;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getReportsTo() {
		return reportsTo;
	}
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public void setReportsTo(Set<String> reportsTo) {
		this.reportsTo = reportsTo;
	}
	public List<TableConfig> getTables() {
		return tables;
	}
	public void setTables(List<TableConfig> tables) {
		this.tables = tables;
	}
	public Set<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(Set<String> attachments) {
		this.attachments = attachments;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Map<String, TableConfig> getTableMap() {
		Map<String, TableConfig> re = new HashMap<String, TableConfig>();
		if (getTables() == null)
			return re;
		for (TableConfig tc : getTables()) {
			re.put(tc.getId(), tc);
		}
		return re;
	}
}
