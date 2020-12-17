package org.orbit.infra.runtime.subs.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.impl.SubsSourceImpl;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.json.JSONUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsSourcesTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "SubsSources";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,"); // unique id
			sb.append("    type varchar(250),");
			sb.append("    typeId varchar(250),");
			sb.append("    name varchar(250),");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
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
	protected SubsSource toObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String type = rs.getString("type");
		String typeId = rs.getString("typeId");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		// Map<String, Object> properties = RuntimeModelConverter.COMMON.toProperties(propertiesString);
		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		return new SubsSourceImpl(Integer.valueOf(id), name, type, typeId, properties, dateCreated, dateModified);
	}

	/**
	 * Get sources.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<SubsSource> getSources(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<SubsSource> handler = new ResultSetListHandler<SubsSource>() {
			@Override
			protected SubsSource handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get sources with type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<SubsSource> getSources(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? ORDER BY id ASC";
		ResultSetListHandler<SubsSource> handler = new ResultSetListHandler<SubsSource>() {
			@Override
			protected SubsSource handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type }, handler);
	}

	/**
	 * Get a source with id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public SubsSource getSource(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<SubsSource> handler = new ResultSetSingleHandler<SubsSource>() {
			@Override
			protected SubsSource handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Get a source with type and typeId.
	 * 
	 * @param conn
	 * @param type
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public SubsSource getSource(Connection conn, String type, String typeId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? AND typeId=?";
		ResultSetSingleHandler<SubsSource> handler = new ResultSetSingleHandler<SubsSource>() {
			@Override
			protected SubsSource handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, typeId }, handler);
	}

	/**
	 * Check whether a source with type and typeId exists.
	 * 
	 * @param conn
	 * @param type
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public boolean sourceExists(Connection conn, String type, String typeId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? AND typeId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, typeId }, handler);
	}

	/**
	 * Create a resource.
	 * 
	 * @param conn
	 * @param type
	 * @param typeId
	 * @param name
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public SubsSource createSource(Connection conn, String type, String typeId, String name, Map<String, Object> properties) throws SQLException {
		if (sourceExists(conn, type, typeId)) {
			throw new SQLException("SubsSource with same type and typeId already exists.");
		}

		// String propertiesString = RuntimeModelConverter.COMMON.toPropertiesString(properties);
		String propertiesString = JSONUtil.toJsonString(properties);
		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (type, typeId, name, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";

		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { type, typeId, name, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getSource(conn, type, typeId);
		}
		return null;
	}

	/**
	 * Update source name.
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
	 * Update source type and typeId.
	 * 
	 * @param conn
	 * @param id
	 * @param type
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateType(Connection conn, Integer id, String type, String typeId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET type=?, typeId=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { type, typeId, getCurrentTime(), id }, 1);
	}

	/**
	 * Update source properties.
	 * 
	 * @param conn
	 * @param id
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, Integer id, Map<String, Object> properties) throws SQLException {
		// String propertiesString = RuntimeModelConverter.COMMON.toPropertiesString(properties);
		String propertiesString = JSONUtil.toJsonString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), id }, 1);
	}

	/**
	 * Delete a source.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteSource(Connection conn, Integer id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

}