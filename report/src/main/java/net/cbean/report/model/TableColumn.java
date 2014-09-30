package net.cbean.report.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Column")
public class TableColumn {

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String label;

	/**
	 * Support cascade type, start with base data type, then append some
	 * business type, split by colon, such as: <br>
	 * string:col1<br>
	 * The base data types is base on how to implement the table parser. 
	 * <p>
	 * There are 7 base data type for POI: <br>
	 * string<br>
	 * double<br>
	 * boolean<br>
	 * date<br>
	 * calendar<br>
	 * richText<br>
	 * formula
	 * 
	 */
	@XStreamAsAttribute
	private String type;

	@XStreamAsAttribute
	private String parser;

	@XStreamAsAttribute
	private boolean hidden = false;

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
