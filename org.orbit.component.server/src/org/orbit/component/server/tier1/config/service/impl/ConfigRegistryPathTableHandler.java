package org.orbit.component.server.tier1.config.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.orbit.component.model.tier1.config.EPath;
import org.orbit.component.model.tier1.config.RegistryPathVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class ConfigRegistryPathTableHandler implements DatabaseTableAware {

	public static ConfigRegistryPathTableHandler INSTANCE = new ConfigRegistryPathTableHandler();

	@Override
	public String getTableName() {
		return "ConfigRegistryPath";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    path varchar(2000) NOT NULL,");
			sb.append("    description varchar(2000) NOT NULL,");
			sb.append("    PRIMARY KEY (userId, path)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    path varchar(2000) NOT NULL,");
			sb.append("    description varchar(2000) NOT NULL,");
			sb.append("    PRIMARY KEY (userId, path)");
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
	 * Convert a ResultSet record to a RegistryPathRTO object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static RegistryPathVO toRTO(ResultSet rs) throws SQLException {
		String userId = rs.getString("userId");
		String path = rs.getString("path");
		String description = rs.getString("description");
		return new RegistryPathVO(userId, path, description);
	}

	/**
	 * Get paths.
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<RegistryPathVO> getPaths(Connection conn, String userId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? ORDER BY path ASC";
		ResultSetListHandler<RegistryPathVO> handler = new ResultSetListHandler<RegistryPathVO>() {
			@Override
			protected RegistryPathVO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId }, handler);
	}

	/**
	 * Check whether a path exists.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public boolean hasPath(Connection conn, String userId, String path) throws SQLException {
		path = checkPath(path);

		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? AND path=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId, path }, handler);
	}

	/**
	 * Get a path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public RegistryPathVO getPath(Connection conn, String userId, String path) throws SQLException {
		path = checkPath(path);

		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=? AND path=?";
		ResultSetSingleHandler<RegistryPathVO> handler = new ResultSetSingleHandler<RegistryPathVO>() {
			@Override
			protected RegistryPathVO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId, path }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean ensurePathExists(Connection conn, String userId, String path, String description) throws SQLException {
		path = checkPath(path);

		int retryCount = 0;
		while (!hasPath(conn, userId, path)) {
			insertPath(conn, userId, path, description);

			if (hasPath(conn, userId, path)) {
				return true;
			}

			retryCount++;
			if (retryCount > 3) {
				break;
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return hasPath(conn, userId, path);
	}

	/**
	 * Insert a path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public RegistryPathVO insertPath(Connection conn, String userId, String path, String description) throws SQLException {
		path = checkPath(path);

		String insertSQL = "INSERT INTO " + getTableName() + " (userId, path, description) VALUES (?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { userId, path, description }, 1);
		if (succeed) {
			return getPath(conn, userId, path);
		}
		return null;
	}

	/**
	 * Update path.
	 * 
	 * @param conn
	 * @param userId
	 * @param newPath
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePath(Connection conn, String userId, String path, String newPath) throws SQLException {
		path = checkPath(path);
		newPath = checkPath(newPath);

		// update the path in the properties table
		ConfigRegistryPropertyTableHandler.INSTANCE.updatePaths(conn, userId, path, newPath);

		String updateSQL = "UPDATE " + getTableName() + " SET path=? WHERE userId=? AND path=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { newPath, userId, path }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, String userId, String path, String description) throws SQLException {
		path = checkPath(path);

		String updateSQL = "UPDATE " + getTableName() + " SET description=? WHERE userId=? AND path=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, userId, path }, 1);
	}

	/**
	 * Delete a path.
	 * 
	 * @param conn
	 * @param userId
	 * @param path
	 * @return
	 * @throws SQLException
	 */
	public boolean deletePath(Connection conn, String userId, String path) throws SQLException {
		path = checkPath(path);

		// delete all properties in the path
		ConfigRegistryPropertyTableHandler.INSTANCE.deleteProperties(conn, userId, path);

		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE userId=? AND path=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { userId, path }, 1);
	}

	/**
	 * Dispose all paths and all properties in each path of a user.
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteAllPaths(Connection conn, String userId) throws SQLException {
		// dispose all properties of a user
		ConfigRegistryPropertyTableHandler.INSTANCE.deleteAllProperties(conn, userId);

		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE userId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { userId }, 1);
	}

}
