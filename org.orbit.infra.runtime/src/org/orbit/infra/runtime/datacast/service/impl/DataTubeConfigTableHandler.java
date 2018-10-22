package org.orbit.infra.runtime.datacast.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.util.UUIDUtil;

/*
 * Table name:
 * 		{data_cast_id}_datatubes
 * 
 * Table name examples:
 * 		cast1_datatubes
 *      cast2_datatubes
 *      cast3_datatubes
 *      cast4_datatubes
 * 
 */
public class DataTubeConfigTableHandler implements DatabaseTableAware {

	public static Map<String, DataTubeConfigTableHandler> tableHandlerMap = new HashMap<String, DataTubeConfigTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param dataCastId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized DataTubeConfigTableHandler getInstance(Connection conn, String dataCastId) throws SQLException {
		String tableName = doGetTableName(dataCastId);
		DataTubeConfigTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			DataTubeConfigTableHandler newTableHandler = new DataTubeConfigTableHandler(dataCastId);
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

		DataTubeConfigTableHandler tableHandler = tableHandlerMap.get(tableName);
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
	public DataTubeConfigTableHandler(String dataCastId) {
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
		return dataCastId + "_datatubes";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			// sb.append(" id int NOT NULL AUTO_INCREMENT,");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    dataTubeId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    enabled boolean NOT NULL DEFAULT false,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			// sb.append(" id serial NOT NULL,");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    dataTubeId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    enabled boolean NOT NULL DEFAULT false,");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a DataTubeConfig object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected DataTubeConfig toDataTubeConfig(ResultSet rs) throws SQLException {
		// int id = rs.getInt("id");
		String id = rs.getString("id");
		String dataTubeId = rs.getString("dataTubeId");
		String name = rs.getString("name");
		boolean isEnabled = rs.getBoolean("enabled");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		if (dataTubeId == null || dataTubeId.isEmpty()) {
			// this should never happen. just in case.
			dataTubeId = "-1";
		}

		Map<String, Object> properties = ModelConverter.COMMON.toProperties(propertiesString);

		return new DataTubeConfigImpl(id, this.dataCastId, dataTubeId, name, isEnabled, properties, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<DataTubeConfig> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<DataTubeConfig> handler = new ResultSetListHandler<DataTubeConfig>() {
			@Override
			protected DataTubeConfig handleRow(ResultSet rs) throws SQLException {
				return toDataTubeConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param isEnabled
	 * @return
	 * @throws SQLException
	 */
	public List<DataTubeConfig> getList(Connection conn, boolean isEnabled) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE enabled=? ORDER BY id ASC";
		ResultSetListHandler<DataTubeConfig> handler = new ResultSetListHandler<DataTubeConfig>() {
			@Override
			protected DataTubeConfig handleRow(ResultSet rs) throws SQLException {
				return toDataTubeConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { isEnabled }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByConfigId(Connection conn, String configId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { configId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param dataTubeId
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByDataTubeId(Connection conn, String dataTubeId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE dataTubeId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { dataTubeId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public DataTubeConfig getByConfigId(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<DataTubeConfig> handler = new ResultSetSingleHandler<DataTubeConfig>() {
			@Override
			protected DataTubeConfig handleRow(ResultSet rs) throws SQLException {
				return toDataTubeConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param dataTubeId
	 * @return
	 * @throws SQLException
	 */
	public DataTubeConfig getByDataTubeId(Connection conn, String dataTubeId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE dataTubeId=?";
		ResultSetSingleHandler<DataTubeConfig> handler = new ResultSetSingleHandler<DataTubeConfig>() {
			@Override
			protected DataTubeConfig handleRow(ResultSet rs) throws SQLException {
				return toDataTubeConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { dataTubeId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param dataTubeId
	 * @param name
	 * @param isEnabled
	 * @param properties
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public DataTubeConfig create(Connection conn, String dataTubeId, String name, boolean isEnabled, Map<String, Object> properties) throws SQLException, IOException {
		String id = generateDataTubeConfigId();
		String propertiesString = ModelConverter.COMMON.toPropertiesString(properties);
		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (id, dataTubeId, name, enabled, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, dataTubeId, name, isEnabled, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getByConfigId(conn, id);
		}
		return null;
	}

	public String generateDataTubeConfigId() {
		String configId = UUIDUtil.generateBase64EncodedUUID();
		return configId;
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String configId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), configId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @param isEnabled
	 * @return
	 * @throws SQLException
	 */
	public boolean updateEnabled(Connection conn, String configId, boolean isEnabled) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET enabled=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { isEnabled, getCurrentTime(), configId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String configId, Map<String, Object> properties) throws SQLException {
		String propertiesString = ModelConverter.COMMON.toPropertiesString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), configId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDateModified(Connection conn, String configId, long dateModified) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, configId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param configId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByConfigId(Connection conn, String configId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { configId }, 1);
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

}
