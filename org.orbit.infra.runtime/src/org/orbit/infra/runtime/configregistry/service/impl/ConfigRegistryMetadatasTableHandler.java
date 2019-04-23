package org.orbit.infra.runtime.configregistry.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		ConfigRegistries
 * 
 */
public class ConfigRegistryMetadatasTableHandler implements DatabaseTableAware {

	protected static Map<String, ConfigRegistryMetadatasTableHandler> tableHandlerMap = new HashMap<String, ConfigRegistryMetadatasTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param databaseProperties
	 * @return
	 * @throws SQLException
	 */
	public static synchronized ConfigRegistryMetadatasTableHandler getInstance(Connection conn, Properties databaseProperties) throws SQLException {
		String database = null;
		try {
			database = DatabaseUtil.getDatabase(databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		ConfigRegistryMetadatasTableHandler tableHandler = tableHandlerMap.get(database);
		if (tableHandler == null) {
			tableHandler = new ConfigRegistryMetadatasTableHandler(database);
			tableHandlerMap.put(database, tableHandler);
		}

		String tableName = tableHandler.getTableName();

		if (!DatabaseUtil.tableExist(conn, tableName)) {
			boolean initialized = DatabaseUtil.initialize(conn, tableHandler);
			if (!initialized) {
				throw new SQLException("Table '" + tableName + "' cannot be created.");
			}
		}
		return tableHandler;
	}

	protected String database;

	public ConfigRegistryMetadatasTableHandler(String database) {
		this.database = database;
	}

	@Override
	public String getTableName() {
		return "ConfigRegistries";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(50) NOT NULL,");
			sb.append("    type varchar(50),");
			sb.append("    name varchar(250),");
			sb.append("    properties varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(50) NOT NULL,");
			sb.append("    type varchar(50),");
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
	protected ConfigRegistryMetadata toValueObject(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String type = rs.getString("type");
		String name = rs.getString("name");
		String propertiesString = rs.getString("properties");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		Map<String, Object> properties = RuntimeModelConverter.COMMON.toProperties(propertiesString);

		return new ConfigRegistryMetadataImpl(id, type, name, properties, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<ConfigRegistryMetadata> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY name ASC";
		ResultSetListHandler<ConfigRegistryMetadata> handler = new ResultSetListHandler<ConfigRegistryMetadata>() {
			@Override
			protected ConfigRegistryMetadata handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<ConfigRegistryMetadata> getList(Connection conn, String type) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE type=? ORDER BY name ASC";
		ResultSetListHandler<ConfigRegistryMetadata> handler = new ResultSetListHandler<ConfigRegistryMetadata>() {
			@Override
			protected ConfigRegistryMetadata handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { type }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public ConfigRegistryMetadata getById(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<ConfigRegistryMetadata> handler = new ResultSetSingleHandler<ConfigRegistryMetadata>() {
			@Override
			protected ConfigRegistryMetadata handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public ConfigRegistryMetadata getByName(Connection conn, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE name=?";
		ResultSetSingleHandler<ConfigRegistryMetadata> handler = new ResultSetSingleHandler<ConfigRegistryMetadata>() {
			@Override
			protected ConfigRegistryMetadata handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean existsById(Connection conn, String id) throws SQLException {
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
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByName(Connection conn, String name) throws SQLException {
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
	 * @param id
	 * @param type
	 * @param name
	 * @param properties
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public ConfigRegistryMetadata create(Connection conn, String id, String type, String name, Map<String, Object> properties) throws SQLException, IOException {
		if (existsById(conn, id)) {
			throw new IOException("Config registry with same id already exists.");
		}
		if (existsByName(conn, name)) {
			throw new IOException("Config registry with same name already exists.");
		}

		String propertiesString = RuntimeModelConverter.COMMON.toPropertiesString(properties);

		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (id, type, name, properties, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, type, name, propertiesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getById(conn, id);
		}
		return null;
	}

	/**
	 * Update type.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean updateType(Connection conn, String id, String type) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET type=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { type, getCurrentTime(), id }, 1);
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String id, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String id, Map<String, Object> properties) throws SQLException {
		String propertiesString = RuntimeModelConverter.COMMON.toPropertiesString(properties);

		String updateSQL = "UPDATE " + getTableName() + " SET properties=?, dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { propertiesString, getCurrentTime(), id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean updateModifiedDate(Connection conn, String id) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE id=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { getCurrentTime(), id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteById(Connection conn, String id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByName(Connection conn, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { name }, 1);
	}

}
