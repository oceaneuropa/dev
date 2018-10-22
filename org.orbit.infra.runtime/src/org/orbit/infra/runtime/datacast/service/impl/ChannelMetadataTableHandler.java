package org.orbit.infra.runtime.datacast.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		{data_cast_id}_channels
 * 
 * Table name examples:
 * 		cast1_channels
 *      cast2_channels
 *      cast3_channels
 *      cast4_channels
 * 
 */
public class ChannelMetadataTableHandler implements DatabaseTableAware {

	public static Map<String, ChannelMetadataTableHandler> tableHandlerMap = new HashMap<String, ChannelMetadataTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param dataCastId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized ChannelMetadataTableHandler getInstance(Connection conn, String dataCastId) throws SQLException {
		String tableName = doGetTableName(dataCastId);
		ChannelMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			ChannelMetadataTableHandler newTableHandler = new ChannelMetadataTableHandler(dataCastId);
			tableHandlerMap.put(tableName, newTableHandler);
			tableHandler = newTableHandler;
		}

		if (!DatabaseUtil.tableExist(conn, tableName)) {
			boolean initialized = DatabaseUtil.initialize(conn, tableHandler);
			if (!initialized) {
				System.err.println("Table '" + tableName + "' cannot be initialized.");
				throw new SQLException("Table '" + tableName + "' cannot be initialized.");
			}
		}
		return tableHandler;
	}

	/**
	 * 
	 * @param conn
	 * @param dataCastId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String dataCastId) throws SQLException {
		String tableName = doGetTableName(dataCastId);

		ChannelMetadataTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler != null) {
			if (DatabaseUtil.tableExist(conn, tableHandler)) {
				boolean disposed = DatabaseUtil.dispose(conn, tableHandler);
				if (disposed) {
					tableHandlerMap.remove(tableName);
				} else {
					System.err.println("Table '" + tableName + "' cannot be disposed.");
					throw new SQLException("Table '" + tableName + "' cannot be disposed.");
				}
				return disposed;
			}
		}
		return false;
	}

	protected String dataCastId;

	/**
	 * 
	 * @param dataCastId
	 */
	public ChannelMetadataTableHandler(String dataCastId) {
		this.dataCastId = dataCastId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.dataCastId);
		return tableName;
	}

	/**
	 * 
	 * @param dataCastId
	 * @return
	 */
	public static String doGetTableName(String dataCastId) {
		return dataCastId + "_channels";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,");
			sb.append("    dataTubeId varchar(250) NOT NULL,");
			sb.append("    channelId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    accessType varchar(250) DEFAULT NULL,");
			sb.append("    accessCode varchar(250) DEFAULT NULL,");
			sb.append("    ownerAccountId varchar(250) DEFAULT NULL,");
			sb.append("    accountIds varchar(50000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,");
			sb.append("    dataTubeId varchar(250) NOT NULL,");
			sb.append("    channelId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    accessType varchar(250) DEFAULT NULL,");
			sb.append("    accessCode varchar(250) DEFAULT NULL,");
			sb.append("    ownerAccountId varchar(250) DEFAULT NULL,");
			sb.append("    accountIds varchar(50000) DEFAULT NULL,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a ChannelMetadata object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected ChannelMetadata toChannelMetadata(ResultSet rs) throws SQLException {
		// int id = rs.getInt("id");
		String dataTubeId = rs.getString("dataTubeId");
		String channelId = rs.getString("channelId");
		String name = rs.getString("name");
		String accessType = rs.getString("accessType");
		String accessCode = rs.getString("accessCode");
		String ownerAccountId = rs.getString("ownerAccountId");
		String accountIdsString = rs.getString("accountIds");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		if (dataTubeId == null || dataTubeId.isEmpty()) {
			// this should never happen. just in case.
			dataTubeId = "-1";
		}

		List<String> accountIds = ModelConverter.DATA_CAST.toAccountIds(accountIdsString);
		Map<String, Object> properties = ModelConverter.COMMON.toProperties(propertiesString);

		return new ChannelMetadataImpl(this.dataCastId, dataTubeId, channelId, name, accessType, accessCode, ownerAccountId, accountIds, properties, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<ChannelMetadata> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<ChannelMetadata> handler = new ResultSetListHandler<ChannelMetadata>() {
			@Override
			protected ChannelMetadata handleRow(ResultSet rs) throws SQLException {
				return toChannelMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param dataTubeId
	 * @return
	 * @throws SQLException
	 */
	public List<ChannelMetadata> getList(Connection conn, String dataTubeId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE dataTubeId=? ORDER BY id ASC";
		ResultSetListHandler<ChannelMetadata> handler = new ResultSetListHandler<ChannelMetadata>() {
			@Override
			protected ChannelMetadata handleRow(ResultSet rs) throws SQLException {
				return toChannelMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { dataTubeId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByChannelId(Connection conn, String channelId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE channelId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { channelId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByChannelName(Connection conn, String name) throws SQLException {
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
	 * 
	 * @param conn
	 * @param channelId
	 * @return
	 * @throws SQLException
	 */
	public ChannelMetadata getByChannelId(Connection conn, String channelId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE channelId=?";
		ResultSetSingleHandler<ChannelMetadata> handler = new ResultSetSingleHandler<ChannelMetadata>() {
			@Override
			protected ChannelMetadata handleRow(ResultSet rs) throws SQLException {
				return toChannelMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { channelId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public ChannelMetadata getByChannelName(Connection conn, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE name=?";
		ResultSetSingleHandler<ChannelMetadata> handler = new ResultSetSingleHandler<ChannelMetadata>() {
			@Override
			protected ChannelMetadata handleRow(ResultSet rs) throws SQLException {
				return toChannelMetadata(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param accessType
	 * @param accessCode
	 * @param ownerAccountId
	 * @param accountIds
	 * @param properties
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public ChannelMetadata create(Connection conn, String dataTubeId, String channelId, String name, String accessType, String accessCode, String ownerAccountId, List<String> accountIds, Map<String, Object> properties) throws SQLException, IOException {
		if (existsByChannelId(conn, channelId)) {
			throw new IOException("Channel with same channelId already exists.");
		}
		if (existsByChannelName(conn, name)) {
			throw new IOException("Channel with same name already exists.");
		}

		String accountIdsString = ModelConverter.DATA_CAST.toAccountIdsString(accountIds);
		String propertiesString = ModelConverter.COMMON.toPropertiesString(properties);

		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (dataTubeId, channelId, name, accessType, accessCode, ownerAccountId, accountIds, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { dataTubeId, channelId, name, accessType, accessCode, ownerAccountId, accountIdsString, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getByChannelId(conn, channelId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String channelId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE channelId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), channelId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @param accountIds
	 * @return
	 * @throws SQLException
	 */
	public boolean updateAccountIds(Connection conn, String channelId, List<String> accountIds) throws SQLException {
		String accountIdsString = ModelConverter.DATA_CAST.toAccountIdsString(accountIds);

		String updateSQL = "UPDATE " + getTableName() + " SET accountIds=?, dateModified=? WHERE channelId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { accountIdsString, getCurrentTime(), channelId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String channelId, Map<String, Object> properties) throws SQLException {
		String propertiesString = ModelConverter.COMMON.toPropertiesString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE channelId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), channelId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDateModified(Connection conn, String channelId, long dateModified) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE channelId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, channelId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param channelId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByChannelId(Connection conn, String channelId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE channelId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { channelId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByChannelName(Connection conn, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { name }, 1);
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

}
