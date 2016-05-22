package org.origin.mgm.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.mgm.model.vo.IndexItemCommandLogVO;

/**
 * CRUD methods for the IndexItemCommandLog table.
 *
 */
public class IndexItemCommandLogTableHandler implements DatabaseTableAware {

	public static IndexItemCommandLogTableHandler INSTANCE = new IndexItemCommandLogTableHandler();

	@Override
	public String getTableName() {
		return "IndexItemCommandLog";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	revision int NOT NULL AUTO_INCREMENT,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	undoCommand varchar(500) NOT NULL,";
			sql += "	undoArguments varchar(20000) NOT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (revision)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	revision serial NOT NULL,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	undoCommand varchar(500) NOT NULL,";
			sql += "	undoArguments varchar(20000) NOT NULL,";
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
	public List<IndexItemCommandLogVO> get(Connection conn) throws SQLException {
		ResultSetListHandler<IndexItemCommandLogVO> handler = new ResultSetListHandler<IndexItemCommandLogVO>() {
			@Override
			protected IndexItemCommandLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String undoCommand = rs.getString("undoCommand");
				String undoArguments = rs.getString("undoArguments");
				String updateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemCommandLogVO(revision, command, arguments, undoCommand, undoArguments, updateTimeString);
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
	public List<IndexItemCommandLogVO> get(Connection conn, int startRevision) throws SQLException {
		ResultSetListHandler<IndexItemCommandLogVO> handler = new ResultSetListHandler<IndexItemCommandLogVO>() {
			@Override
			protected IndexItemCommandLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String undoCommand = rs.getString("undoCommand");
				String undoArguments = rs.getString("undoArguments");
				String updateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemCommandLogVO(revision, command, arguments, undoCommand, undoArguments, updateTimeString);
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
	public List<IndexItemCommandLogVO> get(Connection conn, int startRevision, int endRevision) throws SQLException {
		ResultSetListHandler<IndexItemCommandLogVO> handler = new ResultSetListHandler<IndexItemCommandLogVO>() {
			@Override
			protected IndexItemCommandLogVO handleRow(ResultSet rs) throws SQLException {
				Integer revision = rs.getInt("revision");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String undoCommand = rs.getString("undoCommand");
				String undoArguments = rs.getString("undoArguments");
				String updateTimeString = rs.getString("lastUpdateTime");

				return new IndexItemCommandLogVO(revision, command, arguments, undoCommand, undoArguments, updateTimeString);
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
	 * 
	 * @see http://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
	 * 
	 * @param conn
	 * @param command
	 * @param arguments
	 * @param undoCommand
	 * @param undoArguments
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(Connection conn, String command, String arguments, String undoCommand, String undoArguments, String lastUpdateTime) throws SQLException {
		return DatabaseUtil.update(conn, "INSERT INTO " + getTableName() + " (command, arguments, undoCommand, undoArguments, lastUpdateTime) VALUES (?, ?, ?)", new Object[] { command, arguments, undoCommand, undoArguments, lastUpdateTime }, 1);
	}

	/**
	 * 
	 * @see http://www.tutorialspoint.com/jdbc/jdbc-delete-records.htm
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
