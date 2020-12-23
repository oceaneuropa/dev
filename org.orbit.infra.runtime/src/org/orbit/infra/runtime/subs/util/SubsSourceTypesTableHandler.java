package org.orbit.infra.runtime.subs.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.orbit.infra.model.subs.SubsSourceType;
import org.orbit.infra.model.subs.impl.SubsSourceTypeImpl;
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
public class SubsSourceTypesTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "SubsSourcesTypes";
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
	protected SubsSourceType toObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String name = rs.getString("name");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new SubsSourceTypeImpl(Integer.valueOf(id), type, name, dateCreated, dateModified);
	}

	/**
	 * Get source types.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<SubsSourceType> getTypes(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY type ASC";
		ResultSetListHandler<SubsSourceType> handler = new ResultSetListHandler<SubsSourceType>() {
			@Override
			protected SubsSourceType handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get a source type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public SubsSourceType getType(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=?";
		ResultSetSingleHandler<SubsSourceType> handler = new ResultSetSingleHandler<SubsSourceType>() {
			@Override
			protected SubsSourceType handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, }, handler);
	}

	/**
	 * Check whether a source type exists.
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
	 * Create a resource type.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public SubsSourceType createType(Connection conn, String type, String name) throws SQLException {
		if (typeExists(conn, type)) {
			throw new SQLException("Source type already exists.");
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
	 * Update source type by id.
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
	 * Update source name by id.
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
	 * Update source name by type.
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
	 * Delete a source type by id.
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
	 * Delete a source type by type.
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
