package org.origin.core.resources.impl.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class WorkspacesTableHandler implements DatabaseTableAware {

	protected String id;

	public WorkspacesTableHandler(String id) {
		this.id = id;
	}

	@Override
	public String getTableName() {
		return "Workspaces_" + this.id;
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    workspaceId int NOT NULL AUTO_INCREMENT,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    password varchar(50) NOT NULL,");
			sb.append("    description varchar(500) NOT NULL,");
			sb.append("    lastModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (workspaceId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    workspaceId serial NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    password varchar(50) NOT NULL,");
			sb.append("    description varchar(500) NOT NULL,");
			sb.append("    lastModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (workspaceId)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Convert a ResourceSet record to WorkspaceVO object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected WorkspaceVO toVO(ResultSet rs) throws SQLException {
		int workspaceId = rs.getInt("workspaceId");
		String name = rs.getString("name");
		String password = rs.getString("password");
		String description = rs.getString("description");
		long lastModified = rs.getLong("lastModified");

		return new WorkspaceVO(workspaceId, name, password, description, lastModified);
	}

	/**
	 * Return current time as last modified time.
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

	/**
	 * Get all file records (including in trash and not in trash) in the whole table.
	 * 
	 * @param conn
	 * @return return a list of workspace records.
	 * @throws SQLException
	 */
	public List<WorkspaceVO> getAll(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY workspaceName ASC";
		ResultSetListHandler<WorkspaceVO> handler = new ResultSetListHandler<WorkspaceVO>() {
			@Override
			protected WorkspaceVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Check if a workspace record with specified workspace name exists.
	 * 
	 * @param conn
	 * @param name
	 * @return return the workspace record if found. return null if not found.
	 * @throws SQLException
	 */
	public boolean hasRecord(Connection conn, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { name }, handler);
	}

	/**
	 * Get a workspace record with specified workspaceId.
	 * 
	 * @param conn
	 * @param workspaceId
	 * @return return the workspace record if found. return null if not found.
	 * @throws SQLException
	 */
	public WorkspaceVO getById(Connection conn, int workspaceId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE workspaceId=?";
		ResultSetSingleHandler<WorkspaceVO> handler = new ResultSetSingleHandler<WorkspaceVO>() {
			@Override
			protected WorkspaceVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { workspaceId }, handler);
	}

	/**
	 * Get a workspace record with specified name.
	 * 
	 * @param conn
	 * @param name
	 * @return return the workspace record if found. return null if not found.
	 * @throws SQLException
	 */
	public WorkspaceVO getByName(Connection conn, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE name=?";
		ResultSetSingleHandler<WorkspaceVO> handler = new ResultSetSingleHandler<WorkspaceVO>() {
			@Override
			protected WorkspaceVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { name }, handler);
	}

	/**
	 * Insert a workspace record to the table.
	 * 
	 * @param conn
	 * @param name
	 * @param password
	 * @param description
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public WorkspaceVO insert(Connection conn, String name, String password, String description) throws SQLException, IOException {
		if (hasRecord(conn, name)) {
			throw new IOException("Workspace with same name already exists.");
		}
		String insertSQL = "INSERT INTO " + getTableName() + " (name, password, description, lastModified) VALUES (?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { name, password, description, getCurrentTime() }, 1);
		if (succeed) {
			return getByName(conn, name);
		}
		return null;
	}

	/**
	 * Update workspace name.
	 * 
	 * @param conn
	 * @param workspaceId
	 * @param name
	 * @return true if updated. return false if not updated.
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, int workspaceId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, lastModified=? WHERE workspaceId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), workspaceId }, 1);
	}

	/**
	 * Update workspace password.
	 * 
	 * @param conn
	 * @param workspaceId
	 * @param password
	 * @return true if updated. return false if not updated.
	 * @throws SQLException
	 */
	public boolean updatePassword(Connection conn, int workspaceId, String password) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET password=?, lastModified=? WHERE workspaceId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { password, getCurrentTime(), workspaceId }, 1);
	}

	/**
	 * Update workspace description.
	 * 
	 * @param conn
	 * @param workspaceId
	 * @param description
	 * @return true if updated. return false if not updated.
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, int workspaceId, String description) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET description=?, lastModified=? WHERE workspaceId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, getCurrentTime(), workspaceId }, 1);
	}

	/**
	 * Delete a workspace record by workspaceId.
	 * 
	 * @param conn
	 * @param workspaceId
	 * @return true if deleted. return false if not deleted.
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int workspaceId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE workspaceId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { workspaceId }, 1);
	}

	/**
	 * Delete a workspace record by name.
	 * 
	 * @param conn
	 * @param name
	 * @return true if deleted. return false if not deleted.
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { name }, 1);
	}

}
