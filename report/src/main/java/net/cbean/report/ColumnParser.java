package net.cbean.report;

public interface ColumnParser<T> {
	T value(Object value);
	String toString(Object value);
}
