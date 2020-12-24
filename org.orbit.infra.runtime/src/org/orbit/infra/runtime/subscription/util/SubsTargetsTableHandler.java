package org.orbit.infra.runtime.subscription.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.impl.SubsTargetImpl;
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
public class SubsTargetsTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "SubsTargets";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,"); // unique id
			sb.append("    type varchar(250),");
			sb.append("    instanceId varchar(250),");
			sb.append("    name varchar(250),");
			sb.append("    serverId varchar(250),");
			sb.append("    serverURL varchar(250),");
			sb.append("    serverHeartbeatTime bigint DEFAULT 0,");
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
	protected SubsTarget toObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String instanceId = rs.getString("instanceId");
		String name = rs.getString("name");
		String serverId = rs.getString("serverId");
		String serverURL = rs.getString("serverURL");
		long serverHeartbeatTime = rs.getLong("serverHeartbeatTime");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		return new SubsTargetImpl(Integer.valueOf(id), type, instanceId, name, serverId, serverURL, serverHeartbeatTime, properties, dateCreated, dateModified);
	}

	/**
	 * Get targets.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<SubsTarget> getTargets(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<SubsTarget> handler = new ResultSetListHandler<SubsTarget>() {
			@Override
			protected SubsTarget handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get targets with type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<SubsTarget> getTargets(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? ORDER BY id ASC";
		ResultSetListHandler<SubsTarget> handler = new ResultSetListHandler<SubsTarget>() {
			@Override
			protected SubsTarget handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type }, handler);
	}

	/**
	 * Get a target with id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public SubsTarget getTarget(Connection conn, Integer id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<SubsTarget> handler = new ResultSetSingleHandler<SubsTarget>() {
			@Override
			protected SubsTarget handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Get a target with type and instanceId.
	 * 
	 * @param conn
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws SQLException
	 */
	public SubsTarget getTarget(Connection conn, String type, String instanceId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? AND instanceId=?";
		ResultSetSingleHandler<SubsTarget> handler = new ResultSetSingleHandler<SubsTarget>() {
			@Override
			protected SubsTarget handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, instanceId }, handler);
	}

	/**
	 * Check whether targets with type exists.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean targetExists(Connection conn, String type) throws SQLException {
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
	 * Check whether a target with id exists.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean targetExists(Connection conn, Integer id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Check whether a target with type and instanceId exists.
	 * 
	 * @param conn
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws SQLException
	 */
	public boolean targetExists(Connection conn, String type, String instanceId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? AND instanceId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type, instanceId }, handler);
	}

	/**
	 * Create a target.
	 * 
	 * @param conn
	 * @param type
	 * @param instanceId
	 * @param name
	 * @param serverId
	 * @param serverURL
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public SubsTarget createTarget(Connection conn, String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws SQLException {
		if (targetExists(conn, type, instanceId)) {
			throw new SQLException("SubsTarget with same type and instanceId already exists.");
		}

		String propertiesString = JSONUtil.toJsonString(properties);
		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (type, instanceId, name, serverId, serverURL, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";

		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { type, instanceId, name, serverId, serverURL, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getTarget(conn, type, instanceId);
		}
		return null;
	}

	/**
	 * Update target name.
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
	 * Update target type.
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
	 * Update target instanceId.
	 * 
	 * @param conn
	 * @param id
	 * @param instanceId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateInstanceId(Connection conn, Integer id, String instanceId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET instanceId=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { instanceId, getCurrentTime(), id }, 1);
	}

	/**
	 * Update target serverId.
	 * 
	 * @param conn
	 * @param id
	 * @param serverId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateServerId(Connection conn, Integer id, String serverId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET serverId=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { serverId, getCurrentTime(), id }, 1);
	}

	/**
	 * Update target serverURL.
	 * 
	 * @param conn
	 * @param id
	 * @param serverURL
	 * @return
	 * @throws SQLException
	 */
	public boolean updateServerURL(Connection conn, Integer id, String serverURL) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET serverURL=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { serverURL, getCurrentTime(), id }, 1);
	}

	/**
	 * Update target server heartbeat time.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean updateServerHeartbeatTime(Connection conn, Integer id) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET serverHeartbeatTime=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { getCurrentTime(), id }, 1);
	}

	/**
	 * Update target properties.
	 * 
	 * @param conn
	 * @param id
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, Integer id, Map<String, Object> properties) throws SQLException {
		String propertiesString = JSONUtil.toJsonString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), id }, 1);
	}

	/**
	 * Delete a target.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteTarget(Connection conn, Integer id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

}
