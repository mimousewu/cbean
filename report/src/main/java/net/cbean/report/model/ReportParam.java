package net.cbean.report.model;

import net.cbean.commons.RegexUtil;
import net.cbean.exceptions.ParseException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Parameter")
public class ReportParam {
	public static final String STRING = "string";
	public static final String LONG = "long";
	public static final String INT = "int";
	public static final String DATE = "date";

	@XStreamAsAttribute
	private String name;

	@XStreamAsAttribute
	private String type;

	@XStreamAlias("default-value")
	private String innerValue;
	
	public String getInnerValue() {
		return innerValue;
	}

	public void setInnerValue(String innerValue) {
		this.innerValue = innerValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		Object result = null;
		try {
			if (getType().equals(STRING)) {
				result = (innerValue == null) ? "" : innerValue;
			} else if (getType().equals(LONG)) {
				result = (innerValue == null) ? new Long(0) : new Long(
						innerValue.trim());
			} else if (getType().equals(INT)) {
				result = (innerValue == null) ? new Integer(0) : new Integer(
						innerValue.trim());
			} else if (getType().equals(DATE)) {
				result = (innerValue == null) ? RegexUtil.DATE_FORMAT
						.parse("1970-01-01 00:00:00") : RegexUtil.DATE_FORMAT
						.parse(innerValue.trim());
			}
		} catch (Exception e) {
			throw new ParseException(e);
		}
		return result;
	}
}
