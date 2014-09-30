package net.cbean.report;

import java.util.List;

import net.cbean.report.model.TableConfig;

/**
 * Parse table report from list values by using table config
 * @author cnlwut
 *
 * @param <T>
 */
public interface TableParser<T> {
	
	/**
	 * Parse values and return result by using table config
	 * @param values
	 * @param config
	 * @return
	 */
	public T parse(List<?> values, TableConfig config);
	
	/**
	 * Parse values to target object. so simple type and String can not use this function.
	 * @param target
	 * @param values
	 * @param config
	 */
	public void parse(T target, List<?> values, TableConfig config);
	
	/**
	 * The Column parsers set to parser cell value by type.
	 * @param typeName
	 * @param colParser
	 */
	public void setColumnParser(String typeName, ColumnParser<?> colParser);
}
