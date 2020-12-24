package org.orbit.infra.runtime.subscription.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.orbit.infra.model.subs.SubsType;
import org.orbit.infra.model.subs.impl.SubsTypeImpl;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTargetTypesTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "SubsTargetsTypes";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,"); // unique id
			sb.append("    type varchar(250),");
			sb.append("    name varchar(250),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else {
			throw new UnsupportedOperationException("Database '" + database + "' is not supported.");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected SubsType toObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String name = rs.getString("name");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new SubsTypeImpl(Integer.valueOf(id), type, name, dateCreated, dateModified);
	}

	/**
	 * Get target types.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<SubsType> getTypes(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY type ASC";
		ResultSetListHandler<SubsType> handler = new ResultSetListHandler<SubsType>() {
			@Override
			protected SubsType handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get a target type by id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public SubsType getType(Connection conn, Integer id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<SubsType> handler = new ResultSetSingleHandler<SubsType>() {
			@Override
			protected SubsType handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id, }, handler);
	}

	/**
	 * Get a target type by type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public SubsType getType(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=?";
		ResultSetSingleHandler<SubsType> handler = new ResultSetSingleHandler<SubsType>() {
			@Override
			protected SubsType handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, }, handler);
	}

	/**
	 * Check whether a target type exists.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean typeExists(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type }, handler);
	}

	/**
	 * Create a target type.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public SubsType createType(Connection conn, String type, String name) throws SQLException {
		if (typeExists(conn, type)) {
			throw new SQLException("Target type already exists.");
		}

		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (type, name, dateCreated, dateModified) VALUES (?, ?, ?, ?)";

		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { type, name, dateCreated, dateModified }, 1);
		if (succeed) {
			return getType(conn, type);
		}
		return null;
	}

	/**
	 * Update target type by id.
	 * 
	 * @param conn
	 * @param id
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean updateType(Connection conn, Integer id, String type) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET type=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { type, getCurrentTime(), id }, 1);
	}

	/**
	 * Update target name by id.
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, Integer id, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), id }, 1);
	}

	/**
	 * Update target name by type.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String type, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE type=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), type }, 1);
	}

	/**
	 * Delete a target type by id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteType(Connection conn, Integer id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

	/**
	 * Delete a target type by type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteType(Connection conn, String type) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE type=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { type }, 1);
	}

}
