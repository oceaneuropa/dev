package org.orbit.component.runtime.tier1.config.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.orbit.component.runtime.model.configregistry.EPath;
import org.orbit.component.runtime.model.configregistry.RegistryPropertyVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class ConfigRegistryPropertyTableHandler implements DatabaseTableAware {

	public static ConfigRegistryPropertyTableHandler INSTANCE = new ConfigRegistryPropertyTableHandler();

	@Override
	public String getTableName() {
		return "ConfigRegistryProperty";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    path varchar(2000) NOT NULL,");
			sb.append("    name varchar(2000) NOT NULL,");
			sb.append("    value varchar(2000) NOT NULL,");
			sb.append("    PRIMARY KEY (userId, path, name)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    path varchar(2000) NOT NULL,");
			sb.append("    name varchar(2000) NOT NULL,");
			sb.append("    value varchar(2000) NOT NULL,");
			sb.append("    PRIMARY KEY (userId, path, name)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	protected static String checkPath(String path) {
		if (path != null && path.length() > 1 && path.endsWith(EPath.SEPARATOR)) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	/**
	 * Convert a ResultSet record to a RegistryPropertyRTO object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static RegistryPropertyVO toRTO(ResultSet rs) throws SQLException {
		String userId = rs.getString("userId");
		String path = rs.getString("path");
		String name = rs.getString("name");
		String value = rs.getString("value");
		return new RegistryPropertyVO(userId, path, name, value);
	}

	/**
	 * Get properties in a path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public List<RegistryPropertyVO> getProperties(Connection conn, String userId, String path) throws SQLException {
		path = checkPath(path);

		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? AND path=? ORDER BY path ASC, name ASC";
		ResultSetListHandler<RegistryPropertyVO> handler = new ResultSetListHandler<RegistryPropertyVO>() {
			@Override
			protected RegistryPropertyVO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId, path }, handler);
	}

	/**
	 * Check whether a property exists.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean hasProperty(Connection conn, String userId, String path, String name) throws SQLException {
		path = checkPath(path);

		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? AND path=? AND name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId, path, name }, handler);
	}

	/**
	 * Get a property.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public RegistryPropertyVO getProperty(Connection conn, String userId, String path, String name) throws SQLException {
		path = checkPath(path);

		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? AND path=? AND name=?";
		ResultSetSingleHandler<RegistryPropertyVO> handler = new ResultSetSingleHandler<RegistryPropertyVO>() {
			@Override
			protected RegistryPropertyVO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId, path, name }, handler);
	}

	/**
	 * Set a property.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param name
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public boolean setProperty(Connection conn, String userId, String path, String name, String value) throws SQLException {
		path = checkPath(path);

		// make sure path exists in the path table
		ConfigRegistryPathTableHandler.INSTANCE.ensurePathExists(conn, userId, path, null);

		RegistryPropertyVO existingProperty = getProperty(conn, userId, path, name);
		if (existingProperty != null) {
			// property name exists
			String existingValue = existingProperty.getValue();

			if ((existingValue == null && value != null) || (existingValue != null && !existingValue.equals(value))) {
				// value is changed --- need to update
				String updateSQL = "UPDATE " + getTableName() + " SET value=? WHERE userId=? AND path=? AND name=?";
				return DatabaseUtil.update(conn, updateSQL, new Object[] { value, userId, path, name }, 1);
			} else {
				// value is not changed --- no need to update
			}

		} else {
			// property name doesn't exist --- insert new property
			String insertSQL = "INSERT INTO " + getTableName() + " (userId, path, name, value) VALUES (?, ?, ?, ?)";
			return DatabaseUtil.update(conn, insertSQL, new Object[] { userId, path, name, value }, 1);
		}
		return false;
	}

	/**
	 * Update properties' path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param newPath
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePaths(Connection conn, String userId, String path, String newPath) throws SQLException {
		path = checkPath(path);

		String updateSQL = "UPDATE " + getTableName() + " SET path=? WHERE userId=? AND path=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { newPath, userId, path }, -1);
	}

	/**
	 * Delete a property.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteProperty(Connection conn, String userId, String path, String name) throws SQLException {
		path = checkPath(path);

		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE userId=? AND path=? AND name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { userId, path, name }, 1);
	}

	/**
	 * Delete properties in a path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteProperties(Connection conn, String userId, String path) throws SQLException {
		path = checkPath(path);

		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE userId=? AND path=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { userId, path }, 1);
	}

	/**
	 * Delete all properties of a user.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteAllProperties(Connection conn, String userId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE userId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { userId }, 1);
	}

}
