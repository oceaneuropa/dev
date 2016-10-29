package org.origin.mgm.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.util.DateUtil;
import org.origin.mgm.model.vo.IndexItemDataVO;

/*
 * CRUD methods for the IndexItemData table.
 * 
 * http://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
 * http://www.tutorialspoint.com/jdbc/jdbc-delete-records.htm
 * 
 */
public class IndexItemDataTableHandler implements DatabaseTableAware {

	public static IndexItemDataTableHandler INSTANCE = new IndexItemDataTableHandler();

	protected ResultSetListHandler<IndexItemDataVO> rsListHandler;
	protected AbstractResultSetHandler<IndexItemDataVO> rsSingleHandler;

	public IndexItemDataTableHandler() {
		this.rsListHandler = new ResultSetListHandler<IndexItemDataVO>() {
			@Override
			protected IndexItemDataVO handleRow(ResultSet rs) throws SQLException {
				return createVO(rs);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<IndexItemDataVO>() {
			@Override
			public IndexItemDataVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return createVO(rs);
				}
				return null;
			}
		};
	}

	/**
	 * Create a IndexItemDataVO from a ResultSet.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected IndexItemDataVO createVO(ResultSet rs) throws SQLException {
		Integer indexItemId = rs.getInt("indexItemId");
		String indexProviderId = rs.getString("indexProviderId");
		String type = rs.getString("type");
		String name = rs.getString("name");
		String propertiesString = rs.getString("properties");
		String createTimeString = rs.getString("createTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");

		Date createTime = createTimeString != null ? DateUtil.toDate(createTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new IndexItemDataVO(indexItemId, indexProviderId, type, name, propertiesString, createTime, lastUpdateTime);
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String getTableName() {
		return "IndexItemData";
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
			sql += "	indexProviderId varchar(500) NOT NULL,";
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
			sql += "	indexProviderId varchar(500) NOT NULL,";
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
	 * Get a list of index items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> getIndexItems(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * Get a list of index items by indexProviderId.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> getIndexItems(Connection conn, String indexProviderId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE indexProviderId=? ORDER BY " + getPKName() + " ASC ", new Object[] { indexProviderId }, this.rsListHandler);
	}

	/**
	 * Get a list of index items by indexProviderId and type.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> getIndexItems(Connection conn, String indexProviderId, String type) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE indexProviderId=? AND type=? ORDER BY " + getPKName() + " ASC ", new Object[] { indexProviderId, type }, this.rsListHandler);
	}

	/**
	 * Get an index item by indexItemId.
	 * 
	 * @param conn
	 * @param indexItemId
	 * @return
	 * @throws SQLException
	 */
	public IndexItemDataVO getIndexItem(Connection conn, Integer indexItemId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { indexItemId }, this.rsSingleHandler);
	}

	/**
	 * Get the max indexItemId.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer getMaxIndexItemId(Connection conn) throws SQLException {
		AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		};
		return DatabaseUtil.query(conn, "SELECT MAX(" + getPKName() + ") FROM " + getTableName() + "", null, handler);
	}

	/**
	 * Insert an index item.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemDataVO insert(Connection conn, String indexProviderId, String type, String name, String propertiesString, Date createTime, Date lastUpdateTime) throws SQLException {
		IndexItemDataVO newIndexItemVO = null;

		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		Integer indexItemId = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (indexProviderId, type, name, properties, createTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?)", new Object[] { indexProviderId, type, name, propertiesString, createTimeString, lastUpdateTimeString });
		if (indexItemId > 0) {
			newIndexItemVO = new IndexItemDataVO(indexItemId, indexProviderId, type, name, propertiesString, createTime, lastUpdateTime);
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
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, DateUtil.getDefaultDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE indexItemId=?", new Object[] { propertiesString, lastUpdateTimeString, indexItemId }, 1);
	}

}
