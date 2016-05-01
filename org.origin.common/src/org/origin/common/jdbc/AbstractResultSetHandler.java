package org.origin.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface AbstractResultSetHandler<T> {

	/**
	 * Turn the ResultSet into an Object.
	 *
	 * @param rs
	 *            The ResultSet to handle. It has not been touched before being passed to this method.
	 *
	 * @return An Object initialized with ResultSet data.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	T handle(ResultSet rs) throws SQLException;

}