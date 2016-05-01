package org.origin.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class ResultSetListHandler<T> implements AbstractResultSetHandler<List<T>> {

	@Override
	public List<T> handle(ResultSet rs) throws SQLException {
		List<T> rows = new ArrayList<T>();
		while (rs.next()) {
			T row = handleRow(rs);
			if (row != null) {
				rows.add(row);
			}
		}
		return rows;
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
