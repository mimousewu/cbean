/******************************
 * 版权所有：cbean.net
 * 创建日期: 2006-3-14 10:44:03
 * 创建作者：吴涛
 * 文件名称：SheetHelper.java
 * 最后修改时间：
 * 修改记录：
 *****************************************/
package net.cbean.office;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.Sheet;

public class JxlSheetHelper implements SheetHelper {

	private Sheet sheet;
	private Map<String, Integer> columns = new HashMap<String, Integer>();
	private int rowCount;
	/**
	 * line number in Excel files. start from 0
	 */
	private int columnNameRow;

	public JxlSheetHelper(Sheet sheet) {
		this(sheet, DEFAULT_COLUMN_NAME_ROW);
	}

	public JxlSheetHelper(Sheet sheet, int columnNameRow) {
		this.sheet = sheet;
		this.columnNameRow = columnNameRow;

		for (int i = 0; i < sheet.getColumns(); i++) {
			columns.put(sheet.getCell(i, columnNameRow).getContents(), i);
		}

		rowCount = sheet.getRows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.office.SheetHelper#getRows()
	 */
	public int getRows() {
		return this.rowCount - columnNameRow - 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.office.SheetHelper#containsColumn(java.lang.String)
	 */
	public boolean containsColumn(String colName) {
		return columns.containsKey(colName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.office.SheetHelper#getValue(java.lang.String, int)
	 */
	public String getValue(String colName, int rowIndex) {
		Integer colIndex = columns.get(colName);
		if(colIndex == null){
			throw new IllegalArgumentException("ColumnName not found! "+colName);
		}
		return this.sheet.getCell(colIndex, rowIndex + columnNameRow + 1)
				.getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.office.SheetHelper#iterator()
	 */
	public Iterator<Map<String, String>> iterator() {
		return new Iterator<Map<String, String>>() {
			private int position = 0;

			public boolean hasNext() {
				return position < getRows();
			}

			public Map<String, String> next() {
				Map<String, String> row = new HashMap<String, String>();
				for (String columnName : columns.keySet()) {
					row.put(columnName, getValue(columnName, position));
				}
				position++;
				return row;
			}

			public void remove() {
				throw new IllegalStateException(
						"can't remove row from Excel sheet");
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.cbean.office.SheetHelper#getRowValue(int)
	 */
	public Map<String, String> getRowValue(int rowIndex) {
		Map<String, String> row = new HashMap<String, String>();
		int position = rowIndex + columnNameRow + 1;
		for (String columnName : columns.keySet()) {
			row.put(columnName, getValue(columnName, position));
		}
		return row;
	}

}
