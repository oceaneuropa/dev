package org.origin.mgm.persistence.impl;

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

/**
 * CRUD methods for the IndexItemData table.
 *
 */
public class IndexItemDataTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "IndexItemData";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	id int NOT NULL AUTO_INCREMENT,";
			sql += "	type varchar(255) NOT NULL,";
			sql += "	name varchar(255) NOT NULL,";
			sql += "	properties varchar(20000) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (id)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Get all index items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> get(Connection conn) throws SQLException {
		return get(conn, null);
	}

	/**
	 * Get all index items with specified type.
	 * 
	 * @param conn
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemDataVO> get(Connection conn, String type) throws SQLException {
		ResultSetListHandler<IndexItemDataVO> handler = new ResultSetListHandler<IndexItemDataVO>() {
			@Override
			protected IndexItemDataVO handleRow(ResultSet rs) throws SQLException {
				Integer id = rs.getInt("id");
				String type = rs.getString("type");
				String name = rs.getString("name");
				String propertiesString = rs.getString("properties");
				String lastUpdateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemDataVO(id, type, name, propertiesString, lastUpdateTimeString);
			}
		};
		if (type == null) {
			return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY id ASC", null, handler);
		} else {
			return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? ORDER BY id ASC ", new Object[] { type }, handler);
		}
	}

	/**
	 * Get an index item with specified type and name.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public IndexItemDataVO get(Connection conn, String type, String name) throws SQLException {
		AbstractResultSetHandler<IndexItemDataVO> handler = new AbstractResultSetHandler<IndexItemDataVO>() {
			@Override
			public IndexItemDataVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					Integer id = rs.getInt("id");
					String type = rs.getString("type");
					String name = rs.getString("name");
					String propertiesString = rs.getString("properties");
					String lastUpdateTimeString = rs.getString("lastUpdateTime");

					return new IndexItemDataVO(id, type, name, propertiesString, lastUpdateTimeString);
				}
				return null;
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? AND name=? ORDER BY id ASC", new Object[] { type, name }, handler);
	}

	/**
	 * Get the id of an index item with specified type and name.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Integer getId(Connection conn, String type, String name) throws SQLException {
		AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					Integer id = rs.getInt("id");
					return id;
				}
				return -1;
			}
		};
		return DatabaseUtil.query(conn, "SELECT id FROM " + getTableName() + " WHERE type=? AND name=? ORDER BY id ASC", new Object[] { type, name }, handler);
	}

	/**
	 * Check whether an index item exists.
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean exist(Connection conn, String type, String name) throws SQLException {
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE type=? AND name=? ORDER BY id ASC", new Object[] { type, name }, handler);
	}

	/**
	 * Get the max ID of all index items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer getMaxId(Connection conn) throws SQLException {
		AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		};
		return DatabaseUtil.query(conn, "SELECT MAX(id) FROM " + getTableName() + "", null, handler);
	}

	/**
	 * Insert an index item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(Connection conn, String type, String name, String propertiesString) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(new Date(), DateUtil.getDefaultDateFormat());
		return DatabaseUtil.update(conn, "INSERT INTO " + getTableName() + " (type, name, properties, lastUpdateTime) VALUES (?, ?, ?, ?)", new Object[] { type, name, propertiesString, lastUpdateTimeString }, 1);
	}

	/**
	 * Delete an index item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-delete-records.htm
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String type, String name) throws SQLException {
		if (!exist(conn, type, name)) {
			throw new SQLException("An index item with same type and name does not exist.");
		}
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE type=? AND name=?", new Object[] { type, name }, 1);
	}

	/**
	 * Update the properties of an index item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-update-records.htm
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, String type, String name, String propertiesString) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(new Date(), DateUtil.getDefaultDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE type=? AND name=?", new Object[] { propertiesString, lastUpdateTimeString, type, name }, 1);
	}

	/**
	 * Update the properties of an index item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-update-records.htm
	 * 
	 * @param conn
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @return
	 * @throws SQLException
	 */
	public boolean updateProperties(Connection conn, int id, String propertiesString) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(new Date(), DateUtil.getDefaultDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET properties=?, lastUpdateTime=? WHERE id=?", new Object[] { propertiesString, lastUpdateTimeString, id }, 1);
	}

}
