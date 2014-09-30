package net.cbean.office;

import java.util.Iterator;
import java.util.Map;

public interface SheetHelper {

	public static final int DEFAULT_COLUMN_NAME_ROW = 0;

	public int getRows();

	public boolean containsColumn(String colName);

	/**
	 * @param colName
	 * @param rowIndex start from 0
	 * @return
	 */
	public String getValue(String colName, int rowIndex);

	public Iterator<Map<String, String>> iterator();
	
	/**
	 * @param rowIndex start from 0
	 * @return
	 */
	public Map<String, String> getRowValue(int rowIndex);

}