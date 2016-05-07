package org.origin.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ResultSetSingleHandler<T> implements AbstractResultSetHandler<T> {

	@Override
	public T handle(ResultSet rs) throws SQLException {
		T row = null;
		if (rs.next()) {
			row = handleRow(rs);
		} else {
			row = getEmptyValue();
		}
		return row;
	}

	protected T getEmptyValue() {
		return null;
	}

	/**
	 * Row handler. Method converts current row into some Java object.
	 *
	 * @param rs
	 *            ResultSet to process.
	 * @return row processing result
	 * @throws SQLException
	 *             error occurs
	 */
	protected abstract T handleRow(ResultSet rs) throws SQLException;

}