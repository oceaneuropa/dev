package org.orbit.infra.runtime.subs.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.impl.SubsMappingImpl;
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
public class SubsMappingsTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "SubsMappings";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,"); // unique id
			sb.append("    sourceId bigint DEFAULT -1,");
			sb.append("    targetId bigint DEFAULT -1,");
			sb.append("    clientId varchar(250),");
			sb.append("    clientURL varchar(250),");
			sb.append("    clientHeartbeatTime bigint DEFAULT 0,");
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
	protected SubsMapping toObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int sourceId = rs.getInt("sourceId");
		int targetId = rs.getInt("targetId");
		String clientId = rs.getString("clientId");
		String clientURL = rs.getString("clientURL");
		long clientHeartbeatTime = rs.getLong("clientHeartbeatTime");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		return new SubsMappingImpl(Integer.valueOf(id), Integer.valueOf(sourceId), Integer.valueOf(targetId), clientId, clientURL, clientHeartbeatTime, properties, dateCreated, dateModified);
	}

	/**
	 * Get mappings.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<SubsMapping> getMappings(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<SubsMapping> handler = new ResultSetListHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get mappings by source.
	 * 
	 * @param conn
	 * @param sourceId
	 * @return
	 * @throws SQLException
	 */
	public List<SubsMapping> getMappingsOfSource(Connection conn, Integer sourceId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? ORDER BY id ASC";
		ResultSetListHandler<SubsMapping> handler = new ResultSetListHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId }, handler);
	}

	/**
	 * Get mappings by target.
	 * 
	 * @param conn
	 * @param targetId
	 * @return
	 * @throws SQLException
	 */
	public List<SubsMapping> getMappingsOfTarget(Connection conn, Integer targetId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE targetId=? ORDER BY id ASC";
		ResultSetListHandler<SubsMapping> handler = new ResultSetListHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { targetId }, handler);
	}

	/**
	 * Get mappings between source and target.
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @return
	 * @throws SQLException
	 */
	public List<SubsMapping> getMappings(Connection conn, Integer sourceId, Integer targetId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? AND targetId=? ORDER BY id ASC";
		ResultSetListHandler<SubsMapping> handler = new ResultSetListHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId, targetId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public SubsMapping getMapping(Connection conn, Integer id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<SubsMapping> handler = new ResultSetSingleHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @param clientId
	 * @return
	 * @throws SQLException
	 */
	public SubsMapping getMappingByClientId(Connection conn, Integer sourceId, Integer targetId, String clientId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? AND targetId=? AND clientId=?";
		ResultSetSingleHandler<SubsMapping> handler = new ResultSetSingleHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId, targetId, clientId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @param clientURL
	 * @return
	 * @throws SQLException
	 */
	public SubsMapping getMappingByClientURL(Connection conn, Integer sourceId, Integer targetId, String clientURL) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? AND targetId=? AND clientURL=?";
		ResultSetSingleHandler<SubsMapping> handler = new ResultSetSingleHandler<SubsMapping>() {
			@Override
			protected SubsMapping handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId, targetId, clientURL }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param mappingId
	 * @return
	 * @throws SQLException
	 */
	public boolean mappingExists(Connection conn, Integer mappingId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { mappingId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @param clientId
	 * @return
	 * @throws SQLException
	 */
	public boolean mappingExistsByClientId(Connection conn, Integer sourceId, Integer targetId, String clientId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? AND targetId=? AND clientId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId, targetId, clientId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @param clientURL
	 * @return
	 * @throws SQLException
	 */
	public boolean mappingExistsByClientURL(Connection conn, Integer sourceId, Integer targetId, String clientURL) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE sourceId=? AND targetId=? AND clientURL=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { sourceId, targetId, clientURL }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param sourceId
	 * @param targetId
	 * @param clientId
	 * @param clientURL
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public SubsMapping createMapping(Connection conn, Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws SQLException {
		if (mappingExistsByClientId(conn, sourceId, targetId, clientId)) {
			throw new SQLException("SubsMapping with same sourceId, targetId and clientId already exists.");
		}
		if (mappingExistsByClientURL(conn, sourceId, targetId, clientURL)) {
			throw new SQLException("SubsMapping with same sourceId, targetId and clientURL already exists.");
		}

		String propertiesString = JSONUtil.toJsonString(properties);
		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (sourceId, targetId, clientId, clientURL, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?)";

		SubsMapping mapping = null;
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { sourceId, targetId, clientId, clientURL, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			if (clientId != null) {
				mapping = getMappingByClientId(conn, sourceId, targetId, clientId);
			}
			if (mapping == null && clientURL != null) {
				mapping = getMappingByClientURL(conn, sourceId, targetId, clientURL);
			}
		}
		return mapping;
	}

	/**
	 * Update mapping clientId and clientURL.
	 * 
	 * @param conn
	 * @param id
	 * @param clientId
	 * @param clientURL
	 * @return
	 * @throws SQLException
	 */
	public boolean updateClientURL(Connection conn, Integer id, String clientId, String clientURL) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET clientId=?, clientURL=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { clientId, clientURL, getCurrentTime(), id }, 1);
	}

	/**
	 * Update source client heartbeat time.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean updateClientHeartbeatTime(Connection conn, Integer id) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET clientHeartbeatTime=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { getCurrentTime(), id }, 1);
	}

	/**
	 * Delete a mapping.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteMapping(Connection conn, Integer id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

}
