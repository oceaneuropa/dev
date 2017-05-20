package org.orbit.component.server.tier2.appstore.handler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;

public class AppContentTableHandler implements DatabaseTableAware {

	public static AppContentTableHandler INSTANCE_FOR_MYSQL = new AppContentTableHandler(DatabaseTableAware.MYSQL);
	public static AppContentTableHandler INSTANCE_FOR_POSTGRESQL = new AppContentTableHandler(DatabaseTableAware.POSTGRESQL);

	protected String database;

	public AppContentTableHandler(String database) {
		this.database = database;
	}

	@Override
	public String getTableName() {
		return "AppContent";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    appId varchar(500) NOT NULL,");
			sb.append("    appContent mediumblob,");
			sb.append("    PRIMARY KEY (appId)");
			// not allowed to drop the metadata table without dropping this table first.
			// sb.append(" FOREIGN KEY (fileId) REFERENCES FsFileMetadata(fileId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    appId varchar(500) NOT NULL,");
			sb.append("    appContent bytea,");
			sb.append("    PRIMARY KEY (appId)");
			// not allowed to drop the metadata table without dropping this table first.
			// sb.append(" FOREIGN KEY (fileId) REFERENCES FsFileMetadata(fileId)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Check whether an app content record exists.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean hasContentRecord(Connection conn, String appId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return (boolean) DatabaseUtil.query(conn, querySQL, new Object[] { appId }, handler);
	}

	/**
	 * Insert an app content record.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(Connection conn, String appId) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (appId) VALUES (?)";
		return DatabaseUtil.update(conn, insertSQL, new Object[] { appId }, 1);
	}

	/**
	 * Get app content.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public byte[] getContent(Connection conn, String appId) throws SQLException {
		return null;
	}

	/**
	 * Set app content.
	 * 
	 * @param conn
	 * @param appId
	 * @param inputStream
	 * @return
	 * @throws SQLException
	 */
	public boolean setContent(Connection conn, String appId, InputStream inputStream) throws SQLException {
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			PreparedStatement pstmt = null;
			try {
				String updateSQL = "UPDATE " + getTableName() + " SET appContent=? WHERE appId=?";

				pstmt = conn.prepareStatement(updateSQL);
				pstmt.setBinaryStream(1, inputStream);
				pstmt.setString(2, appId);

				int updatedRowCount = pstmt.executeUpdate();
				if (updatedRowCount == 1) {
					return true;
				}
			} finally {
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
			PreparedStatement pstmt = null;
			try {
				String updateSQL = "UPDATE " + getTableName() + " SET appContent=? WHERE appId=?";

				pstmt = conn.prepareStatement(updateSQL);
				try {
					pstmt.setBinaryStream(1, inputStream, inputStream.available());
				} catch (IOException e) {
					e.printStackTrace();
				} // need to specify length of the input stream.
				pstmt.setString(2, appId);

				int updatedRowCount = pstmt.executeUpdate();
				if (updatedRowCount == 1) {
					return true;
				}
			} finally {
				DatabaseUtil.closeQuietly(pstmt, true);
			}
		}
		return false;
	}

	/**
	 * Delete app content record.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String appId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE appId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { appId }, -1);
	}

}
