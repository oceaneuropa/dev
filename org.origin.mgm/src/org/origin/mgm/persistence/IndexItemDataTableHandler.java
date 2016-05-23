package org.origin.mgm.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
				Integer indexItemId = rs.getInt("indexItemId");
				String indexProviderId = rs.getString("indexProviderId");
				String namespace = rs.getString("namespace");
				String name = rs.getString("name");
				String propertiesString = rs.getString("properties");
				String createTimeString = rs.getString("createTime");
				String lastUpdateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemDataVO(indexItemId, indexProviderId, namespace, name, propertiesString, createTimeString, lastUpdateTimeString);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<IndexItemDataVO>() {
			@Override
			public IndexItemDataVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					Integer indexItemId = rs.getInt("indexItemId");
					String indexProviderId = rs.getString("indexProviderId");
					String namespace = rs.getString("namespace");
					String name = rs.getString("name");
					String propertiesString = rs.getString("properties");
					String createTimeString = rs.getString("createTime");
					String lastUpdateTimeString = rs.getString("lastUpdateTime");

					return new IndexItemDataVO(indexItemId, indexProviderId, namespace, name, propertiesString, createTimeString, lastUpdateTimeString);
				}
				return null;
			}
		};
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
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	indexItemId int NOT NULL AUTO_INCREMENT,";
			sql += "	indexProviderId varchar(500) NOT NULL,";
			sql += "	namespace varchar(500) NOT NULL,";
			sql += "	name varchar(500) NOT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (indexItemId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	indexItemId serial NOT NULL,";
			sql += "	indexProviderId varchar(500) NOT NULL,";
			sql += "	namespace varchar(500) NOT NULL,";
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
	public List<IndexItemDataVO> getAll(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> getByIndexProviderId(Connection conn, String indexProviderId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE indexProviderId=? ORDER BY " + getPKName() + " ASC ", new Object[] { indexProviderId }, this.rsListHandler);
	}

	/**
	 * Get a list of index items.
	 * 
	 * @param conn
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> getByNamespace(Connection conn, String namespace) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE namespace=? ORDER BY " + getPKName() + " ASC ", new Object[] { namespace }, this.rsListHandler);
	}

	/**
	 * Get a list of index items.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> get(Connection conn, String indexProviderId, String namespace) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE indexProviderId=? AND namespace=? ORDER BY " + getPKName() + " ASC ", new Object[] { indexProviderId, namespace }, this.rsListHandler);
	}

	/**
	 * Get an index item.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemDataVO get(Connection conn, String indexProviderId, String namespace, String name, String propertiesString, Date createTime, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, DateUtil.getJdbcDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, DateUtil.getJdbcDateFormat());

		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE indexProviderId=? AND namespace=? AND name=? AND properties=? AND createTime=? AND lastUpdateTime=? ORDER BY " + getPKName() + " DESC", new Object[] { indexProviderId, namespace, name, propertiesString, createTimeString, lastUpdateTimeString }, this.rsSingleHandler);
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
		return DatabaseUtil.query(conn, "SELECT MAX(" + getPKName() + ") FROM " + getTableName(), null, handler);
	}

	/**
	 * Insert an index item.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemDataVO insert(Connection conn, String indexProviderId, String namespace, String name, String propertiesString, Date createTime, Date lastUpdateTime) throws SQLException {
		IndexItemDataVO vo = null;

		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, DateUtil.getJdbcDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, DateUtil.getJdbcDateFormat());

		Integer indexItemId = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (indexProviderId, namespace, name, properties, createTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?)", new Object[] { indexProviderId, namespace, name, propertiesString, createTimeString, lastUpdateTimeString });
		if (indexItemId > 0) {
			vo = new IndexItemDataVO(indexItemId, indexProviderId, namespace, name, propertiesString, createTimeString, lastUpdateTimeString);
		}

		return vo;
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

	// /**
	// * Get the id of an index item with specified type and name.
	// *
	// * @param conn
	// * @param type
	// * @param name
	// * @return
	// * @throws SQLException
	// */
	// public Integer getId(Connection conn, String type, String name) throws SQLException {
	// AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
	// @Override
	// public Integer handle(ResultSet rs) throws SQLException {
	// if (rs.next()) {
	// Integer id = rs.getInt("id");
	// return id;
	// }
	// return -1;
	// }
	// };
	// return DatabaseUtil.query(conn, "SELECT id FROM " + getTableName() + " WHERE type=? AND name=? ORDER BY id ASC", new Object[] { type, name },
	// handler);
	// }

	// /**
	// * Check whether an index item exists.
	// *
	// * @param conn
	// * @param type
	// * @param name
	// * @return
	// * @throws SQLException
	// */
	// public boolean exist(Connection conn, String type, String name) throws SQLException {
	// AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
	// @Override
	// public Boolean handle(ResultSet rs) throws SQLException {
	// if (rs.next()) {
	// return true;
	// }
	// return false;
	// }
	// };
	// return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? AND name=? ORDER BY id ASC", new Object[] { type, name },
	// handler);
	// }

	// /**
	// * Update the properties of an index item.
	// *
	// * http://www.tutorialspoint.com/jdbc/jdbc-update-records.htm
	// *
	// * @param conn
	// * @param namespace
	// * @param name
	// * @param propertiesString
	// * @return
	// * @throws SQLException
	// */
	// public boolean updateProperties(Connection conn, int id, String propertiesString) throws SQLException {
	// String lastUpdateTimeString = DateUtil.toString(new Date(), DateUtil.getDefaultDateFormat());
	// return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE id=?", new Object[] {
	// propertiesString, lastUpdateTimeString, id }, 1);
	// }

}
