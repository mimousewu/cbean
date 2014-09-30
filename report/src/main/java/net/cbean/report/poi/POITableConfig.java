package net.cbean.report.poi;

import net.cbean.report.model.TableConfig;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Table-Sheet")
public class POITableConfig extends TableConfig {

	@XStreamAsAttribute
	private String tempSheet;

	@XStreamAsAttribute
	private int startRow = 1;

	@XStreamAsAttribute
	private String startColumn;

	@XStreamAsAttribute
	private int templateStartRow = 1;

	@XStreamAsAttribute
	private String templateStartColumn;

	@XStreamAsAttribute
	private int templateRowSize = 1;

	@XStreamAsAttribute
	private boolean vertical = false;

	@XStreamAsAttribute
	private boolean showColumnLabel = false;

	public boolean isVertical() {
		return vertical;
	}

	public boolean isShowColumnLabel() {
		return showColumnLabel;
	}

	public void setShowColumnLabel(boolean showColumnLabel) {
		this.showColumnLabel = showColumnLabel;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

	public String getTempSheet() {
		return tempSheet;
	}

	public void setTempSheet(String tempSheet) {
		this.tempSheet = tempSheet;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getStartRowIndex() {
		if (startRow < 1) {
			startRow = 1;
		}
		return startRow - 1;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getStartColumn() {
		return startColumn;
	}

	public int getStartColumnIndex() {
		return columnCharToInt(startColumn);
	}

	public void setStartColumn(String startColumn) {
		this.startColumn = startColumn;
	}

	public int getTemplateStartRow() {
		return templateStartRow;
	}

	public int getTemplateStartRowIndex() {
		if (templateStartRow < 1) {
			templateStartRow = startRow;
		}
		return templateStartRow - 1;
	}

	public void setTemplateStartRow(int templateStartRow) {
		this.templateStartRow = templateStartRow;
	}

	public String getTemplateStartColumn() {
		return templateStartColumn;
	}

	public int getTemplateStartColumnIndex() {
		if (templateStartColumn == null) {
			templateStartColumn = startColumn;
		}
		return columnCharToInt(templateStartColumn);
	}

	public void setTemplateStartColumn(String templateStartColumn) {
		this.templateStartColumn = templateStartColumn;
	}

	public int getTemplateRowSize() {
		if (templateRowSize < 1) {
			templateRowSize = this.isShowColumnLabel() ? 2 : 1;
		}
		return templateRowSize;
	}

	public void setTemplateRowSize(int templateRowSize) {
		this.templateRowSize = templateRowSize;
	}

	private int columnCharToInt(String charIndex) {
		if (charIndex == null) {
			return 0;
		} else {
			int colIndex = 0;
			byte[] bytes = charIndex.getBytes();
			int grade = 0;
			for (int i = bytes.length; i > 0; i--) {
				colIndex = colIndex + (bytes[i - 1] - 64)
						* (int) Math.pow(26, (grade++));
			}
			return colIndex - 1;
		}
	}

}
