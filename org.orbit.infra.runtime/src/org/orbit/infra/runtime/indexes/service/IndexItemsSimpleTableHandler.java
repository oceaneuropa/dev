package org.orbit.infra.runtime.indexes.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItemVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.util.DateUtil;

public class IndexItemsSimpleTableHandler implements DatabaseTableAware {

	public static Map<String, IndexItemsSimpleTableHandler> tableHandlerMap = new HashMap<String, IndexItemsSimpleTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized IndexItemsSimpleTableHandler getInstance(Connection conn, String indexProviderId) throws SQLException {
		IndexItemsSimpleTableHandler tableHandler = tableHandlerMap.get(indexProviderId);
		if (tableHandler == null) {
			IndexItemsSimpleTableHandler newTableHandler = new IndexItemsSimpleTableHandler(indexProviderId);

			String tableName = newTableHandler.getTableName();
			if (!DatabaseUtil.tableExist(conn, tableName)) {
				boolean initialized = DatabaseUtil.initialize(conn, newTableHandler);
				if (!initialized) {
					System.err.println("Table '" + tableName + "' cannot be initialized.");
					throw new SQLException("Table '" + tableName + "' cannot be initialized.");
				}
			}

			tableHandlerMap.put(indexProviderId, newTableHandler);
			tableHandler = newTableHandler;
		}
		return tableHandler;
	}

	/**
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String indexProviderId) throws SQLException {
		IndexItemsSimpleTableHandler tableHandler = tableHandlerMap.get(indexProviderId);
		if (tableHandler != null) {
			String tableName = tableHandler.getTableName();
			if (DatabaseUtil.tableExist(conn, tableHandler)) {
				boolean disposed = DatabaseUtil.dispose(conn, tableHandler);
				if (disposed) {
					tableHandlerMap.remove(indexProviderId);
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
	 * @param indexProviderId
	 * @return
	 */
	public static String getTableName(String indexProviderId) {
		String name = indexProviderId;
		if (name.contains(".")) {
			name = name.replaceAll("\\.", "_");
		}
		// name = name.toUpperCase();
		String tableName = "IndexItem_" + name;
		return tableName;
	}

	protected String indexProviderId;
	protected String tableName;

	protected ResultSetListHandler<IndexItemVO> rsListHandler;
	protected AbstractResultSetHandler<IndexItemVO> rsSingleHandler;

	/**
	 * 
	 * @param indexProviderId
	 */
	public IndexItemsSimpleTableHandler(String indexProviderId) {
		this.indexProviderId = indexProviderId;
		this.tableName = getTableName(this.indexProviderId);

		this.rsListHandler = new ResultSetListHandler<IndexItemVO>() {
			@Override
			protected IndexItemVO handleRow(ResultSet rs) throws SQLException {
				return createVO(rs);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<IndexItemVO>() {
			@Override
			public IndexItemVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return createVO(rs);
				}
				return null;
			}
		};
	}

	public String getIndexProviderid() {
		return this.indexProviderId;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	protected String getPKName() {
		return "indexItemId";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	indexItemId int NOT NULL AUTO_INCREMENT,";
			// sql += " indexProviderId varchar(500) NOT NULL,";
			sql += "	type varchar(500) NOT NULL,";
			sql += "	name varchar(500) NOT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (indexItemId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	indexItemId serial NOT NULL,";
			// sql += " indexProviderId varchar(500) NOT NULL,";
			sql += "	type varchar(500) NOT NULL,";
			sql += "	name varchar(500) NOT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (indexItemId)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Create a IndexItemVO from a ResultSet.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected IndexItemVO createVO(ResultSet rs) throws SQLException {
		Integer indexItemId = rs.getInt("indexItemId");
		// String indexProviderId = rs.getString("indexProviderId");
		String type = rs.getString("type");
		String name = rs.getString("name");
		String propertiesString = rs.getString("properties");
		String createTimeString = rs.getString("createTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");

		Date createTime = createTimeString != null ? DateUtil.toDate(createTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new IndexItemVO(indexItemId, this.indexProviderId, type, name, propertiesString, createTime, lastUpdateTime);
	}

	/**
	 * Get a list of index items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemVO> getIndexItems(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * Get a list of index items by type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemVO> getIndexItems(Connection conn, String type) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? ORDER BY " + getPKName() + " ASC ", new Object[] { type }, this.rsListHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public IndexItemVO getIndexItem(Connection conn, String type, String name) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? and name=? ORDER BY " + getPKName() + " ASC ", new Object[] { type, name }, this.rsSingleHandler);
	}

	/**
	 * Get an index item by indexItemId.
	 * 
	 * @param conn
	 * @param indexItemId
	 * @return
	 * @throws SQLException
	 */
	public IndexItemVO getIndexItem(Connection conn, Integer indexItemId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { indexItemId }, this.rsSingleHandler);
	}

	/**
	 * Insert an index item.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemVO insert(Connection conn, String type, String name, String propertiesString, Date createTime, Date lastUpdateTime) throws SQLException {
		IndexItemVO newIndexItemVO = null;
		if (createTime == null) {
			createTime = new Date();
		}
		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		Integer indexItemId = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (type, name, properties, createTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?)", new Object[] { type, name, propertiesString, createTimeString, lastUpdateTimeString });
		if (indexItemId > 0) {
			newIndexItemVO = new IndexItemVO(indexItemId, indexProviderId, type, name, propertiesString, createTime, lastUpdateTime);
		}
		return newIndexItemVO;
	}

	/**
	 * Delete an index item.
	 * 
	 * @param conn
	 * @param indexItemId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, Integer indexItemId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE indexItemId=?", new Object[] { indexItemId }, 1);
	}

	/**
	 * Update the properties of an index item.
	 *
	 * http://www.tutorialspoint.com/jdbc/jdbc-update-records.htm
	 * 
	 * @param conn
	 * @param indexItemId
	 * @param propertiesString
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, int indexItemId, String propertiesString, Date lastUpdateTime) throws SQLException {
		// String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, DateUtil.getDefaultDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE indexItemId=?", new Object[] { propertiesString, lastUpdateTimeString, indexItemId }, 1);
	}

}
