package net.cbean.report.poi;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cbean.report.ColumnParser;
import net.cbean.report.TableParser;
import net.cbean.report.model.TableColumn;
import net.cbean.report.model.TableConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class POITableParser implements TableParser<Sheet> {
	private static final Log log = LogFactory.getLog(POITableParser.class);

	private CellStyle[][] cellStyles;
	protected POITableConfig config;

	private List<TableColumn> reverseShowRows = new ArrayList<TableColumn>();

	private Map<String, ColumnParser<?>> parserMap = new HashMap<String, ColumnParser<?>>();

	public void setColumnParser(String type, ColumnParser<?> colParser) {
		parserMap.put(type, colParser);
	}

	public ColumnParser<?> getColumnParser(String type) {
		return parserMap.get(type);
	}

	public Sheet parse(List<?> values, TableConfig tableConfig) {
		setConfig(tableConfig);
		Sheet sheet = generateSheet();
		parse(sheet, values, tableConfig);
		return sheet;
	}

	public void parse(Sheet blankTable, List<?> values, TableConfig tableConfig) {
		if (blankTable == null) {
			throw new IllegalArgumentException(
					"Can not generate sheet or sheet can not access!");
		}
		setConfig(tableConfig);

		if (config.isVertical() && values != null) {
			values = converse(values);
		}

		IndexHolder indexs = new IndexHolder(config);

		createHeaders(blankTable, indexs);
		if (values != null)
			createDataRows(blankTable, values, indexs);
		
		reverseShowRows.clear();

	}

	private void setConfig(TableConfig tableConfig) {
		if (tableConfig instanceof POITableConfig) {
			config = (POITableConfig) tableConfig;
		} else {
			throw new IllegalArgumentException(
					"the parameter of config is not instanceof POITableConfig!");
		}
	}

	public void getCellStyles(Sheet tempSheet, POITableConfig config) {
		if (tempSheet == null)
			return;

		int templateRowSize = config.isVertical() ? config.getColumns().size()
				: config.getTemplateRowSize();
		int templateColSize = config.isVertical() ? config.getTemplateRowSize()
				: config.getColumns().size();

		cellStyles = new CellStyle[templateRowSize][templateColSize];
		for (int i = 0; i < templateRowSize; i++) {
			for (int j = 0; j < templateColSize; j++) {
				int rownum = config.getTemplateStartRowIndex() + i;
				int cellnum = config.getTemplateStartColumnIndex() + j;

				Row row = tempSheet.getRow(rownum);
				Cell cell = (row != null) ? row.getCell(cellnum) : null;
				if (cell != null) {
					cellStyles[i][j] = cell.getCellStyle();
				} else {
					if (log.isDebugEnabled())
						log.debug("Template Row: " + rownum + " Col: "
								+ cellnum + " of sheet "
								+ config.getTempSheet() + " is null!");
				}
			}
		}
	}

	protected void createHeaders(Sheet sheet, IndexHolder indexs) {
		if (config.isVertical() || !config.isShowColumnLabel()) {
			if (log.isDebugEnabled())
				log.debug("Do not draw table column labels of sheet ["
						+ sheet.getSheetName()
						+ "] because of "
						+ (!config.isShowColumnLabel() ? "showColumnLabel attribute set to false."
								: "")
						+ (config.isVertical() ? "it is vertical table, the columns conversed to datas before."
								: ""));
			return;
		}

		Row headerRow = createDataRow(sheet, indexs.rowIndex++);
		for (TableColumn column : config.getColumns()) {
			if (column.isHidden())
				continue;
			CellStyle style = getTemplateStyle(indexs.tempRowIndex,
					indexs.tempColIndex++);
			Cell cell = headerRow.createCell(indexs.colIndex++);
			cell.setCellValue(column.getLabel());

			setCellStyle(cell, style, "string");
		}
		indexs.tempRowIndex++;
	}

	@SuppressWarnings("unchecked")
	protected void createDataRows(Sheet sheet, List<?> values,
			IndexHolder indexs) {
		int startDataTempRowIndex = indexs.tempRowIndex;
		for (Object obj : values) {
			Map<String, Object> map = null;
			if (obj instanceof Map) {
				map = (Map<String, Object>) obj;
			} else {
				map = new HashMap<String, Object>();
				copyProperties(map, obj);
			}

			Row dataRow = createDataRow(sheet, indexs.rowIndex++);

			indexs.colIndex = config.getStartColumnIndex();
			indexs.tempColIndex = 0; // config.getTemplateStartColumnIndex();

			if (config.isVertical()) {
				String type = reverseShowRows.get(
						indexs.rowIndex - config.getStartRowIndex() - 1)
						.getType();
				for (int i = 0; i < map.size(); i++) {
					Object valueObject = map.get(Integer.toString(i));
					genCell(indexs, dataRow, valueObject, type);
				}
			} else {
				for (TableColumn column : config.getColumns()) {
					if (column.isHidden())
						continue;

					Object valueObject = prepareCellValue(column, map,
							indexs.sequenceIndex);
					genCell(indexs, dataRow, valueObject, column.getType());
				}
			}

			indexs.sequenceIndex++;
			indexs.tempRowIndex++;

			// Restart template rows
			if (cellStyles != null && indexs.tempRowIndex >= cellStyles.length) {
				indexs.tempRowIndex = startDataTempRowIndex;
			}
		}
	}

	private void genCell(IndexHolder indexs, Row dataRow, Object valueObject,
			String colType) {
		CellStyle style = getTemplateStyle(indexs.tempRowIndex,
				indexs.tempColIndex++);
		Cell cell = dataRow.createCell(indexs.colIndex++);
		fillCellValue(cell, valueObject, colType);

		setCellStyle(cell, style, colType);
	}

	protected void setCellStyle(Cell cell, CellStyle style, String type) {
		if (type == null) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (type.startsWith("formula")) {
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
		} else if (type.startsWith("double")) {
//			short fmt = cell.getSheet().getWorkbook().createDataFormat()
//					.getFormat(("-#,##0.00"));
//			style.setDataFormat(fmt);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (type.startsWith("boolean")) {
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
		} else if (type.startsWith("date")) {
//			short fmt = cell.getSheet().getWorkbook().createDataFormat()
//					.getFormat(("yyyy/m/d h:mm"));
//			style.setDataFormat(fmt);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		} else if (type.startsWith("calendar")) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else if (type.startsWith("richText")) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		
		if (style != null) {
			cell.setCellStyle(style);
		}
	}

	private CellStyle getTemplateStyle(int tempRowIndex, int tempColIndex) {
		if (cellStyles == null || tempRowIndex >= cellStyles.length
				|| tempColIndex >= cellStyles[0].length) {
			return null;
		}

		if (cellStyles[tempRowIndex] != null
				&& cellStyles[tempRowIndex][tempColIndex] != null) {
			return cellStyles[tempRowIndex][tempColIndex];
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void fillCellValue(Cell cell, Object value, String type) {
		ColumnParser<?> columnParser = getColumnParser(type);
		if (columnParser != null && value != null) {
			if (type.startsWith("string")) {
				ColumnParser<String> cp = (ColumnParser<String>) columnParser;
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("formula")) {
				ColumnParser<Double> cp = (ColumnParser<Double>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("double")) {
				ColumnParser<Double> cp = (ColumnParser<Double>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("boolean")) {
				ColumnParser<Boolean> cp = (ColumnParser<Boolean>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("date")) {
				ColumnParser<Date> cp = (ColumnParser<Date>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("calendar")) {
				ColumnParser<Calendar> cp = (ColumnParser<Calendar>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else if (type.startsWith("richText")) {
				ColumnParser<RichTextString> cp = (ColumnParser<RichTextString>) getColumnParser(type);
				cell.setCellValue(cp.value(value));
			} else {
				cell.setCellValue(value.toString());
			}
		} else {
			if (value != null)
				cell.setCellValue(value.toString());
		}
	}

	/**
	 * prepare Cell value from original row data
	 * 
	 * @param col
	 * @param originalRow
	 * @param seqence
	 * @return
	 */
	private Object prepareCellValue(TableColumn col,
			Map<String, Object> originalRow, int seqence) {
		Object value = originalRow.get(col.getName());
		if ("SEQUENCE".equalsIgnoreCase(col.getType())) {
			value = seqence;
		}
		return value;
	}

	/**
	 * Create row when it is not exist.
	 * 
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	private Row createDataRow(Sheet sheet, int rowIndex) {
		Row dataRow = (sheet.getRow(rowIndex) == null) ? sheet
				.createRow(rowIndex) : sheet.getRow(rowIndex);
		return dataRow;
	}

	private Workbook workbook;
	private boolean keepTemplateCells;

	private Sheet generateSheet() {
		if (workbook == null) {
			throw new IllegalStateException("Workbook not set first.");
		}

		Sheet sheet = null;
		int tempSheetIndex = workbook.getSheetIndex(config.getTempSheet());
		if (tempSheetIndex < 0) {
			sheet = workbook.createSheet(config.getTitle());
		} else {
			if (workbook.getSheetIndex(config.getTitle()) >= 0) {
				sheet = workbook.getSheet(config.getTitle());
			} else {
				sheet = workbook.cloneSheet(tempSheetIndex);
				// sheet = workbook.createSheet();
				workbook.setSheetName(workbook.getSheetIndex(sheet),
						config.getTitle());

				initSheet(sheet);
			}

			Sheet tempSheet = workbook.getSheet(config.getTempSheet());
			this.getCellStyles(tempSheet, config);

			if (!keepTemplateCells) {
				this.clearTempInTable(sheet);
			}
		}

		return sheet;
	}

	public void removeTemplateSheet(POITableConfig... tableConfig) {
		if (tableConfig == null || tableConfig.length == 0) {
			removeSheet(config.getTempSheet());
		} else {
			for (POITableConfig tc : tableConfig) {
				removeSheet(tc.getTempSheet());
			}
		}

	}

	private void removeSheet(String template) {
		int tempSheetIndex = workbook.getSheetIndex(template);
		if (tempSheetIndex >= 0) {
			workbook.removeSheetAt(tempSheetIndex);
		}
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public void setKeepTemplateCells(boolean keepTemplateCells) {
		this.keepTemplateCells = keepTemplateCells;
	}

	public static void copyProperties(Map<String, Object> map, Object bean) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
		PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
		for (int i = 0; i < descriptor.length; i++) {
			String name = descriptor[i].getName();
			map.put(name, beanWrapper.getPropertyValue(name));
		}
	}

	protected void initSheet(Sheet sheet) {
		log.info("No special init sheet actions!");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<?> converse(List<?> values) {
		List<Map> output = new ArrayList<Map>();
		for (TableColumn col : config.getColumns()) {
			if (col.isHidden()) {
				continue;
			} else {
				reverseShowRows.add(col);
			}

			Map<String, Object> rowData = new HashMap<String, Object>();

			int rowIndex = 0;
			if (config.isShowColumnLabel()) {
				rowData.put(Integer.toString(rowIndex++), col.getLabel());
			}

			for (int i = 0; i < values.size(); i++) {
				Map<String, Object> orginalRow = (Map<String, Object>) values
						.get(i);
				Object valueObj = prepareCellValue(col, orginalRow, i + 1);
				rowData.put(Integer.toString(rowIndex++), valueObj);
			}
			output.add(rowData);
		}
		return output;
	}

	public void clearTempInTable(Sheet sheet) {
		for (int i = 0; i < config.getTemplateRowSize(); i++) {
			for (int colIndex = config.getTemplateStartColumnIndex(); colIndex < config
					.getTemplateStartColumnIndex() + config.getColumns().size(); colIndex++) {
				int rownum = i + config.getTemplateStartRowIndex();
				if (sheet.getRow(rownum) != null
						&& sheet.getRow(rownum).getCell(colIndex) != null) {
					CellStyle style = sheet.getRow(rownum).getCell(colIndex)
							.getCellStyle();
					sheet.getRow(rownum).createCell(colIndex)
							.setCellStyle(style);

				}
			}
		}
	}

	class IndexHolder {
		int rowIndex;
		int colIndex;
		int tempColIndex;
		int tempRowIndex;
		int sequenceIndex = 1;

		public IndexHolder(POITableConfig poiConfig) {
			rowIndex = poiConfig.getStartRowIndex();
			colIndex = poiConfig.getStartColumnIndex();
			tempColIndex = poiConfig.getTemplateStartColumnIndex();
			tempRowIndex = 0;
		}

	}

}
