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
import org.origin.mgm.model.vo.IndexItemRevisionVO;

/*
 * CRUD methods for the IndexItemCommandLog table.
 * 
 * @see http://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
 * @see http://www.tutorialspoint.com/jdbc/jdbc-delete-records.htm
 * 
 */
public class IndexItemRevisionTableHandler implements DatabaseTableAware {

	public static IndexItemRevisionTableHandler INSTANCE = new IndexItemRevisionTableHandler();

	protected ResultSetListHandler<IndexItemRevisionVO> rsListHandler;
	protected AbstractResultSetHandler<IndexItemRevisionVO> rsSingleHandler;

	public IndexItemRevisionTableHandler() {
		this.rsListHandler = new ResultSetListHandler<IndexItemRevisionVO>() {
			@Override
			protected IndexItemRevisionVO handleRow(ResultSet rs) throws SQLException {
				Integer revisionId = rs.getInt("revisionId");
				String indexProviderId = rs.getString("indexProviderId");
				String command = rs.getString("command");
				String arguments = rs.getString("arguments");
				String undoCommand = rs.getString("undoCommand");
				String undoArguments = rs.getString("undoArguments");
				String updateTimeString = rs.getString("updateTime");

				Date lastUpdate = updateTimeString != null ? DateUtil.toDate(updateTimeString, DateUtil.getCommonDateFormats()) : null;

				return new IndexItemRevisionVO(revisionId, indexProviderId, command, arguments, undoCommand, undoArguments, lastUpdate);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<IndexItemRevisionVO>() {
			@Override
			public IndexItemRevisionVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					Integer revisionId = rs.getInt("revisionId");
					String indexProviderId = rs.getString("indexProviderId");
					String command = rs.getString("command");
					String arguments = rs.getString("arguments");
					String undoCommand = rs.getString("undoCommand");
					String undoArguments = rs.getString("undoArguments");
					String updateTimeString = rs.getString("updateTime");

					Date lastUpdate = updateTimeString != null ? DateUtil.toDate(updateTimeString, DateUtil.getCommonDateFormats()) : null;

					return new IndexItemRevisionVO(revisionId, indexProviderId, command, arguments, undoCommand, undoArguments, lastUpdate);
				}
				return null;
			}
		};
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String getTableName() {
		return "IndexItemRevision";
	}

	protected String getPKName() {
		return "revisionId";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	revisionId int NOT NULL AUTO_INCREMENT,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(10000) NOT NULL,";
			sql += "	undoCommand varchar(500) NOT NULL,";
			sql += "	undoArguments varchar(10000) NOT NULL,";
			sql += "	updateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (revisionId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS origin." + getTableName() + " (";
			sql += "	revisionId serial NOT NULL,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(10000) NOT NULL,";
			sql += "	undoCommand varchar(500) NOT NULL,";
			sql += "	undoArguments varchar(10000) NOT NULL,";
			sql += "	updateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (revisionId)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Get a list of revisions.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemRevisionVO> getRevisions(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * Get a list of revisions which begins with (inclusive) the specified startRevisionId and extends to the latest revision.
	 * 
	 * @param conn
	 * @param startRevisionId
	 *            the beginning revisionId, inclusive.
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemRevisionVO> getRevisions(Connection conn, int startRevisionId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + ">=? ORDER BY " + getPKName() + " ASC", new Object[] { startRevisionId }, this.rsListHandler);
	}

	/**
	 * Get a list of revisions which begins with (inclusive) the specified startRevisionId and ends with (exclusive) the specified endRevisionId.
	 * 
	 * @param conn
	 * @param startRevisionId
	 *            the beginning revisionId, inclusive.
	 * @param endRevisionId
	 *            the ending revisionId, exclusive.
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemRevisionVO> getRevisions(Connection conn, int startRevisionId, int endRevisionId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + ">=? AND " + getPKName() + "<? ORDER BY " + getPKName() + " ASC", new Object[] { startRevisionId, endRevisionId }, this.rsListHandler);
	}

	/**
	 * Get a revision by revisionId.
	 * 
	 * @param conn
	 * @param revisionId
	 * @return
	 * @throws SQLException
	 */
	public IndexItemRevisionVO getRevision(Connection conn, Integer revisionId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { revisionId }, this.rsSingleHandler);
	}

	/**
	 * Get the max revisionId.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer getMaxRevisionId(Connection conn) throws SQLException {
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
	 * Insert a revision.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param command
	 * @param arguments
	 * @param undoCommand
	 * @param undoArguments
	 * @param updateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemRevisionVO insert(Connection conn, String indexProviderId, String command, String arguments, String undoCommand, String undoArguments, Date updateTime) throws SQLException {
		IndexItemRevisionVO newRevisionVO = null;
		String updateTimeString = DateUtil.toString(updateTime, getDateFormat());

		Integer revisionId = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (indexProviderId, command, arguments, undoCommand, undoArguments, updateTime) VALUES (?, ?, ?, ?, ?, ?)", new Object[] { indexProviderId, command, arguments, undoCommand, undoArguments, updateTimeString });
		if (revisionId > 0) {
			newRevisionVO = new IndexItemRevisionVO(revisionId, indexProviderId, command, arguments, undoCommand, undoArguments, updateTime);
		}
		return newRevisionVO;
	}

	/**
	 * Delete a revision.
	 * 
	 * @param conn
	 * @param revisionId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int revisionId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE revisionId=?", new Object[] { revisionId }, 1);
	}

	// /**
	// * Check whether a log item with specified revision exists.
	// *
	// * @param conn
	// * @param revision
	// * @return
	// * @throws SQLException
	// */
	// public boolean exist(Connection conn, int revision) throws SQLException {
	// AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
	// @Override
	// public Boolean handle(ResultSet rs) throws SQLException {
	// if (rs.next()) {
	// return true;
	// }
	// return false;
	// }
	// };
	// return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE revision=?", new Object[] { revision }, handler);
	// }

}
