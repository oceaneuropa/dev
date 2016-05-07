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
import org.origin.mgm.model.vo.IndexItemLogVO;

/**
 * CRUD methods for the IndexItemLog table.
 *
 */
public class IndexItemLogTableHandler implements DatabaseTableAware {

	@Override
	public String getTableName() {
		return "IndexItemLog";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	revision int NOT NULL AUTO_INCREMENT,";
			sql += "	command varchar(255) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (revision)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Get all log items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemLogVO> get(Connection conn) throws SQLException {
		ResultSetListHandler<IndexItemLogVO> handler = new ResultSetListHandler<IndexItemLogVO>() {
			@Override
			protected IndexItemLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String lastUpdateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemLogVO(revision, command, arguments, lastUpdateTimeString);
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY revision ASC", null, handler);
	}

	/**
	 * Get the log items which begins with the log item (inclusive) with the specified startRevision and extends to the log item with the largest
	 * revision.
	 * 
	 * @param conn
	 * @param startRevision
	 *            the beginning revision, inclusive.
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemLogVO> get(Connection conn, int startRevision) throws SQLException {
		ResultSetListHandler<IndexItemLogVO> handler = new ResultSetListHandler<IndexItemLogVO>() {
			@Override
			protected IndexItemLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String lastUpdateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemLogVO(revision, command, arguments, lastUpdateTimeString);
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE revision>=? ORDER BY revision ASC", new Object[] { startRevision }, handler);
	}

	/**
	 * Get the log items which begins with the log item (inclusive) with the specified startRevision and ends with the log item (exclusive) with the
	 * specified endRevision.
	 * 
	 * @param conn
	 * @param startRevision
	 *            the beginning revision, inclusive.
	 * @param endRevision
	 *            the ending revision, exclusive.
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemLogVO> get(Connection conn, int startRevision, int endRevision) throws SQLException {
		ResultSetListHandler<IndexItemLogVO> handler = new ResultSetListHandler<IndexItemLogVO>() {
			@Override
			protected IndexItemLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String lastUpdateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemLogVO(revision, command, arguments, lastUpdateTimeString);
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE revision>=? AND revision<? ORDER BY revision ASC", new Object[] { startRevision, endRevision }, handler);
	}

	/**
	 * Check whether a log item with specified revision exists.
	 * 
	 * @param conn
	 * @param revision
	 * @return
	 * @throws SQLException
	 */
	public boolean exist(Connection conn, int revision) throws SQLException {
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		};
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE revision=?", new Object[] { revision }, handler);
	}

	/**
	 * Get the max revision of all log items.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer getMaxRevision(Connection conn) throws SQLException {
		AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		};
		return DatabaseUtil.query(conn, "SELECT MAX(revision) FROM " + getTableName() + "", null, handler);
	}

	/**
	 * Insert a log item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
	 * 
	 * @param conn
	 * @param command
	 * @param arguments
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(Connection conn, String command, String arguments) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(new Date(), DateUtil.getDefaultDateFormat());
		return DatabaseUtil.update(conn, "INSERT INTO " + getTableName() + " (command, arguments, lastUpdateTime) VALUES (?, ?, ?)", new Object[] { command, arguments, lastUpdateTimeString }, 1);
	}

	/**
	 * Delete a log item.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-delete-records.htm
	 * 
	 * @param conn
	 * @param revision
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int revision) throws SQLException {
		if (!exist(conn, revision)) {
			throw new SQLException("A log item with same revision does not exist.");
		}
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE revision=?", new Object[] { revision }, 1);
	}

}
