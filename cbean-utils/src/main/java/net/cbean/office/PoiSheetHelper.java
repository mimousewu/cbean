/****************************************
 * Copyright (c) 2005-2008 by Cbean.net
 * All right reserved.
 * Create Date: ����02:51:39
 * Create Author: Simon.Wu
 * Author E-Mail: mimousewu@gmail.com
 * File Name:  PoiSheetHelper.java
 * Last Update Date:
 * Change Log:
 * ====================================================================
 * $RCSfile: PoiSheetHelper.java,v $  $Revision: 1.0 $  $Date: ����02:51:39 $
 * $Log: PoiSheetHelper.java,v $
 * ====================================================================
 ****************************************/
package net.cbean.office;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Simon.Wu <br>
 *         <a href="mailto:mimousewu@gmail.com">mimousewu@gmail.com</a>
 *         <p>
 * 
 */
public class PoiSheetHelper implements SheetHelper {

	private HSSFSheet sheet;
	private Map<String, Integer> columns = new HashMap<String, Integer>();
	private int rowCount;
	/**
	 * line number in Excel files. start from 0
	 */
	private int columnNameRow;

	public PoiSheetHelper(HSSFSheet sheet) {
		this(sheet, DEFAULT_COLUMN_NAME_ROW);
	}

	public PoiSheetHelper(HSSFSheet sheet, int columnNameRow) {
		if (sheet == null) {
			throw new IllegalArgumentException("sheet not exists!");
		}
		this.sheet = sheet;
		this.columnNameRow = columnNameRow;
		HSSFRow headRow = sheet.getRow((short) columnNameRow);
		if (headRow == null) {
			return;
		}

		int columnCount = headRow.getPhysicalNumberOfCells();
		for (int i = 0; i < columnCount; i++) {
			HSSFRow row = sheet.getRow(columnNameRow);
			HSSFCell cell = row.getCell((short) i);
			if(cell != null)
				columns.put(cell.getStringCellValue(), i);
		}

		rowCount = sheet.getLastRowNum() + sheet.getFirstRowNum() + 1;
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
		HSSFRow row = sheet.getRow(rowIndex + columnNameRow + 1);
		if (row == null) {
			return null;
		}

		if (columns.get(colName) == null) {
			throw new IllegalArgumentException("ColumnName not found! "+colName);
		}
		HSSFCell cell = row.getCell(columns.get(colName).shortValue());
		if (cell == null) {
			return null;
		}
		return cell.getStringCellValue();
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

	/* (non-Javadoc)
	 * @see net.cbean.office.SheetHelper#getRowValue(int)
	 */
	public Map<String, String> getRowValue(int rowIndex) {
		Map<String,String> row = new HashMap<String,String>();
		int position = rowIndex + columnNameRow + 1;
		for(String columnName : columns.keySet()){
			row.put(columnName, getValue(columnName, position));
		}
		return row;
	}

}
