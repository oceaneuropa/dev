package org.orbit.infra.runtime.extensionregistry.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.extensionregistry.ExtensionItemVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.util.DateUtil;

public class ExtensionItemsTableHandler implements DatabaseTableAware {

	public static Map<String, ExtensionItemsTableHandler> tableHandlerMap = new HashMap<String, ExtensionItemsTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param platformId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized ExtensionItemsTableHandler getInstance(Connection conn, String platformId) throws SQLException {
		ExtensionItemsTableHandler tableHandler = tableHandlerMap.get(platformId);
		if (tableHandler == null) {
			ExtensionItemsTableHandler newTableHandler = new ExtensionItemsTableHandler(platformId);
			tableHandlerMap.put(platformId, newTableHandler);
			tableHandler = newTableHandler;
		}
		String tableName = tableHandler.getTableName();
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
	 * @param platformId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String platformId) throws SQLException {
		ExtensionItemsTableHandler tableHandler = tableHandlerMap.get(platformId);
		if (tableHandler != null) {
			String tableName = tableHandler.getTableName();
			if (DatabaseUtil.tableExist(conn, tableHandler)) {
				boolean disposed = DatabaseUtil.dispose(conn, tableHandler);
				if (disposed) {
					tableHandlerMap.remove(platformId);
				} else {
					System.err.println("Table '" + tableName + "' cannot be disposed.");
					throw new SQLException("Table '" + tableName + "' cannot be disposed.");
				}
				return disposed;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param platformId
	 * @return
	 */
	public static String getTableName(String platformId) {
		String name = platformId;
		if (name.contains(".")) {
			name = name.replaceAll("\\.", "_");
		}
		// name = name.toUpperCase();
		String tableName = "Extensions_" + name;
		return tableName;
	}

	protected String platformId;
	protected String tableName;

	protected ResultSetListHandler<ExtensionItemVO> rsListHandler;
	protected AbstractResultSetHandler<ExtensionItemVO> rsSingleHandler;

	/**
	 * 
	 * @param platformId
	 */
	public ExtensionItemsTableHandler(String platformId) {
		this.platformId = platformId;
		this.tableName = getTableName(this.platformId);

		this.rsListHandler = new ResultSetListHandler<ExtensionItemVO>() {
			@Override
			protected ExtensionItemVO handleRow(ResultSet rs) throws SQLException {
				return createVO(rs);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<ExtensionItemVO>() {
			@Override
			public ExtensionItemVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return createVO(rs);
				}
				return null;
			}
		};
	}

	public String getPlatformId() {
		return this.platformId;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	protected String getPKName() {
		return "id";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	id int NOT NULL AUTO_INCREMENT,";
			// sql += " platformId varchar(250) NOT NULL,";
			sql += "	typeId varchar(250) NOT NULL,";
			sql += "	extensionId varchar(250) NOT NULL,";
			sql += "	name varchar(250) DEFAULT NULL,";
			sql += "	description varchar(500) DEFAULT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (id)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	id serial NOT NULL,";
			// sql += " platformId varchar(250) NOT NULL,";
			sql += "	typeId varchar(250) NOT NULL,";
			sql += "	extensionId varchar(250) NOT NULL,";
			sql += "	name varchar(250) DEFAULT NULL,";
			sql += "	description varchar(500) DEFAULT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (id)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Create a ExtensionItemVO from a ResultSet.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected ExtensionItemVO createVO(ResultSet rs) throws SQLException {
		Integer id = rs.getInt("id");
		// String platformId = rs.getString("platformId");
		String typeId = rs.getString("typeId");
		String extensionId = rs.getString("extensionId");
		String name = rs.getString("name");
		String description = rs.getString("description");
		String propertiesString = rs.getString("properties");
		String createTimeString = rs.getString("createTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");

		Date createTime = createTimeString != null ? DateUtil.toDate(createTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new ExtensionItemVO(id, this.platformId, typeId, extensionId, name, description, propertiesString, createTime, lastUpdateTime);
	}

	/**
	 * Get a list of extension items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<ExtensionItemVO> getExtensionItems(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * Get a list of extension items.
	 * 
	 * @param conn
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public List<ExtensionItemVO> getExtensionItems(Connection conn, String typeId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE typeId=? ORDER BY " + getPKName() + " ASC ", new Object[] { typeId }, this.rsListHandler);
	}

	/**
	 * Get an extension item.
	 * 
	 * @param conn
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws SQLException
	 */
	public ExtensionItemVO getExtensionItem(Connection conn, String typeId, String extensionId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE typeId=? and extensionId=? ORDER BY " + getPKName() + " ASC ", new Object[] { typeId, extensionId }, this.rsSingleHandler);
	}

	/**
	 * Get an extension item by indexItemId.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public ExtensionItemVO getExtensionItem(Connection conn, Integer id) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { id }, this.rsSingleHandler);
	}

	/**
	 * Add an extension item.
	 * 
	 * @param conn
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public ExtensionItemVO insert(Connection conn, String typeId, String extensionId, String name, String description, String propertiesString, Date createTime, Date lastUpdateTime) throws SQLException {
		ExtensionItemVO newVO = null;
		if (createTime == null) {
			createTime = new Date();
		}
		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		Integer id = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (typeId, extensionId, name, description, properties, createTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?, ?)", new Object[] { typeId, extensionId, name, description, propertiesString, createTimeString, lastUpdateTimeString });
		if (id > 0) {
			newVO = new ExtensionItemVO(id, this.platformId, typeId, extensionId, name, description, propertiesString, createTime, lastUpdateTime);
		}
		return newVO;
	}

	/**
	 * Update typeId.
	 * 
	 * @param conn
	 * @param id
	 * @param typeId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateTypeId(Connection conn, int id, String typeId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET typeId=?, lastUpdateTime=? WHERE id=?", new Object[] { typeId, lastUpdateTimeString, id }, 1);
	}

	/**
	 * Update extensionId.
	 * 
	 * @param conn
	 * @param id
	 * @param instanceId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateExtensionId(Connection conn, int id, String extensionId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET extensionId=?, lastUpdateTime=? WHERE id=?", new Object[] { extensionId, lastUpdateTimeString, id }, 1);
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, int id, String name, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET name=?, lastUpdateTime=? WHERE id=?", new Object[] { name, lastUpdateTimeString, id }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param id
	 * @param description
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, int id, String description, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET description=?, lastUpdateTime=? WHERE id=?", new Object[] { description, lastUpdateTimeString, id }, 1);
	}

	/**
	 * Delete all extension items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteAll(Connection conn) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName(), new Object[] {}, -1);
	}

	/**
	 * Delete an extension item.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, Integer id) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE id=?", new Object[] { id }, 1);
	}

	/**
	 * Delete an extension item.
	 * 
	 * @param conn
	 * @param typeId
	 * @param extensionId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String typeId, String extensionId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE typeId=? and extensionId=?", new Object[] { typeId, extensionId }, 1);
	}

	/**
	 * Update extension item properties.
	 *
	 * @param conn
	 * @param id
	 * @param propertiesString
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, int id, String propertiesString, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE id=?", new Object[] { propertiesString, lastUpdateTimeString, id }, 1);
	}

	/**
	 * Update extension item properties.
	 * 
	 * @param conn
	 * @param typeId
	 * @param extensionId
	 * @param propertiesString
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String typeId, String extensionId, String propertiesString, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE typeId=? and extensionId=?", new Object[] { propertiesString, lastUpdateTimeString, typeId, extensionId }, 1);
	}

}
