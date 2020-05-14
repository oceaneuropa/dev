package org.orbit.infra.runtime.configregistry.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * Table name:
 * 		ConfigRegistry_{configRegistryId}
 * 
 * Table name examples:
 * 		ConfigRegistry_GisZB_FqQOCmYW7_a8ABIg (e.g. for gaia/earth nodes configurations)
 *      ConfigRegistry_gT3qr3jIRXGVD0APfnAL5g (e.g. for hermes/wing nodes configurations)
 * 
 */
public class ConfigRegistryElementsTableHandler implements DatabaseTableAware {

	public static Map<String, ConfigRegistryElementsTableHandler> tableHandlerMap = new HashMap<String, ConfigRegistryElementsTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param configRegistryId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized ConfigRegistryElementsTableHandler getInstance(Connection conn, String configRegistryId) throws SQLException {
		String tableName = doGetTableName(configRegistryId);
		ConfigRegistryElementsTableHandler tableHandler = tableHandlerMap.get(tableName);
		if (tableHandler == null) {
			ConfigRegistryElementsTableHandler newTableHandler = new ConfigRegistryElementsTableHandler(configRegistryId);
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
	 * @param configRegistryId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String configRegistryId) throws SQLException {
		String tableName = doGetTableName(configRegistryId);

		ConfigRegistryElementsTableHandler tableHandler = tableHandlerMap.get(tableName);
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

	protected String configRegistryId;

	/**
	 * 
	 * @param configRegistryId
	 */
	public ConfigRegistryElementsTableHandler(String configRegistryId) {
		this.configRegistryId = configRegistryId;
	}

	@Override
	public String getTableName() {
		String tableName = doGetTableName(this.configRegistryId);
		return tableName;
	}

	/**
	 * 
	 * @param configRegistryId
	 * @return
	 */
	public static String doGetTableName(String configRegistryId) {
		if (configRegistryId != null) {
			while (configRegistryId.contains("-")) {
				configRegistryId = configRegistryId.replace("-", "_");
			}
		}
		return "ConfigRegistry_" + configRegistryId;
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,");
			sb.append("    elementId varchar(250) NOT NULL,");
			sb.append("    parentElementId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    attributes varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,");
			sb.append("    elementId varchar(250) NOT NULL,");
			sb.append("    parentElementId varchar(250) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    attributes varchar(5000) DEFAULT NULL,");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}
		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a ConfigElement object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected ConfigElement toValueObject(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String elementId = rs.getString("elementId");
		String parentElementId = rs.getString("parentElementId");
		String name = rs.getString("name");
		String attributesString = rs.getString("attributes");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		if (parentElementId == null || parentElementId.isEmpty()) {
			// this should never happen. just in case.
			parentElementId = "-1";
		}

		Map<String, Object> attributes = RuntimeModelConverter.COMMON.toMap(attributesString);

		return new ConfigElementImpl(this.configRegistryId, id, elementId, parentElementId, null, name, attributes, dateCreated, dateModified);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<ConfigElement> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<ConfigElement> handler = new ResultSetListHandler<ConfigElement>() {
			@Override
			protected ConfigElement handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentElementId
	 * @return
	 * @throws SQLException
	 */
	public List<ConfigElement> getList(Connection conn, String parentElementId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentElementId=? ORDER BY id ASC";
		ResultSetListHandler<ConfigElement> handler = new ResultSetListHandler<ConfigElement>() {
			@Override
			protected ConfigElement handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentElementId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @return
	 * @throws SQLException
	 */
	public ConfigElement getByElementId(Connection conn, String elementId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE elementId=?";
		ResultSetSingleHandler<ConfigElement> handler = new ResultSetSingleHandler<ConfigElement>() {
			@Override
			protected ConfigElement handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { elementId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public ConfigElement getByName(Connection conn, String parentElementId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentElementId=? AND name=?";
		ResultSetSingleHandler<ConfigElement> handler = new ResultSetSingleHandler<ConfigElement>() {
			@Override
			protected ConfigElement handleRow(ResultSet rs) throws SQLException {
				return toValueObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentElementId, name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByElementId(Connection conn, String elementId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE elementId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { elementId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean existsByName(Connection conn, String parentElementId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentElementId=? AND name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentElementId, name }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @param parentElementId
	 * @param name
	 * @param attributes
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public ConfigElement create(Connection conn, String elementId, String parentElementId, String name, Map<String, Object> attributes) throws SQLException, IOException {
		if (existsByElementId(conn, elementId)) {
			throw new IOException("Element with same id already exists.");
		}
		if (existsByName(conn, parentElementId, name)) {
			throw new IOException("Element with same name already exists.");
		}

		String attributesString = RuntimeModelConverter.COMMON.toMapString(attributes);

		long dateCreated = getCurrentTime();
		long dateModified = dateCreated;

		String insertSQL = "INSERT INTO " + getTableName() + " (elementId, parentElementId, name, attributes, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { elementId, parentElementId, name, attributesString, dateCreated, dateModified }, 1);
		if (succeed) {
			return getByElementId(conn, elementId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @param parentElementId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateParentElementId(Connection conn, String elementId, int parentElementId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET parentElementId=?, dateModified=? WHERE elementId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { parentElementId, getCurrentTime(), elementId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String elementId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE elementId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getCurrentTime(), elementId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @param attributes
	 * @return
	 * @throws SQLException
	 */
	public boolean updateAttributes(Connection conn, String elementId, Map<String, Object> attributes) throws SQLException {
		String attributesString = RuntimeModelConverter.COMMON.toMapString(attributes);

		String updateSQL = "UPDATE " + getTableName() + " SET attributes=?, dateModified=? WHERE elementId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { attributesString, getCurrentTime(), elementId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDateModified(Connection conn, String elementId, long dateModified) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE elementId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, elementId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param elementId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByElementId(Connection conn, String elementId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE elementId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { elementId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param parentElementId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteByName(Connection conn, String parentElementId, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE parentElementId=? AND name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { parentElementId, name }, 1);
	}

	/**
	 * 
	 * @return
	 */
	protected long getCurrentTime() {
		return new Date().getTime();
	}

}
