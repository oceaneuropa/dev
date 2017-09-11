package org.origin.core.resources.impl.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetSingleHandler;

/*
 * MySQL
 * Writing and Reading MySQL BLOB Using JDBC
 * http://www.mysqltutorial.org/mysql-jdbc-blob
 * http://www.javaxp.com/2012/11/mysql-jdbc-example-for-blob-storage.html
 *
 * 
 * http://superuser.com/questions/418814/how-to-increase-mysql-max-allowed-packet-for-client
 * -------------------------------------------------------------------------------------------
 * C:\Program Files\MySQL\MySQL Server 5.7\bin> mysql -u root -p origin
 * Password: admin
 * 
 * mysql> show variables like 'max_allowed_packet';
 * 
 * mysql> set global max_allowed_packet=1024 * 1024 * 512;
 * 
 * mysql> exit;
 * 
 * mysql> show variables like 'max_allowed_packet';
 * -------------------------------------------------------------------------------------------
 * 
 * Postgres
 * Storing Binary Data
 * https://jdbc.postgresql.org/documentation/80/binary-data.html#binary-data-example
 *
 * Need to create bytes array input stream and specify the length of the bytes array to do the file content update.
 * http://stackoverflow.com/questions/4299765/saving-java-object-to-postgresql-problem
 */
public class FileContentTableHandler implements DatabaseTableAware {

	protected String id;

	public FileContentTableHandler(String id) {
		this.id = id;
	}

	@Override
	public String getTableName() {
		return "WorkspaceFileContent_" + this.id;
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    fileContentId int NOT NULL AUTO_INCREMENT,");
			sb.append("    fileId int NOT NULL,");
			sb.append("    fileContent mediumblob,");
			sb.append("    PRIMARY KEY (fileContentId)");
			// not allow me to drop the metadata table without dropping this table first.
			// sb.append(" FOREIGN KEY (fileId) REFERENCES FsFileMetadata(fileId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    fileContentId serial NOT NULL,");
			sb.append("    fileId int NOT NULL,");
			sb.append("    fileContent bytea,");
			sb.append("    PRIMARY KEY (fileContentId)");
			// not allow me to drop the metadata table without dropping this table first.
			// sb.append(" FOREIGN KEY (fileId) REFERENCES FsFileMetadata(fileId)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Create a FileContentVO from a ResultSet record.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected FileContentVO toVO(ResultSet rs) throws SQLException {
		int fileContentId = rs.getInt("fileContentId");
		int fileId = rs.getInt("fileId");
		return new FileContentVO(fileContentId, fileId);
	}

	/**
	 * Check whether file content record exists with specified file content id.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @return return true if a file content record is found. return false if no file content record is found.
	 * @throws SQLException
	 */
	public boolean hasRecordByFileContentId(Connection conn, int fileContentId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileContentId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return (boolean) DatabaseUtil.query(conn, querySQL, new Object[] { fileContentId }, handler);
	}

	/**
	 * Check whether file content record exists with specified file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return return true if a file content record is found. return false if no file content record is found.
	 * @throws SQLException
	 */
	public boolean hasRecordByFileId(Connection conn, int fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return (boolean) DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * Get the file content record with specified file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return return the file content record. return null if not found.
	 * @throws SQLException
	 */
	public FileContentVO getByFileId(Connection conn, int fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		ResultSetSingleHandler<FileContentVO> handler = new ResultSetSingleHandler<FileContentVO>() {
			@Override
			protected FileContentVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * Get the file content record with specified file content id.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @return return the file content record. return null if not found.
	 * @throws SQLException
	 */
	public FileContentVO getByFileContentId(Connection conn, int fileContentId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileContentId=?";
		ResultSetSingleHandler<FileContentVO> handler = new ResultSetSingleHandler<FileContentVO>() {
			@Override
			protected FileContentVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileContentId }, handler);
	}

	/**
	 * Insert a file content record to the FsFileContent table.
	 * 
	 * @param conn
	 * @param fileId
	 * @return return the new file content record. return null of new file content record is not created.
	 * @throws SQLException
	 */
	public FileContentVO insert(Connection conn, int fileId) throws SQLException {
		if (hasRecordByFileId(conn, fileId)) {
			throw new SQLException("File content record with same file id already exists.");
		}

		String insertSQL = "INSERT INTO " + getTableName() + " (fileId) VALUES (?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { fileId }, 1);

		if (succeed) {
			String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=? ORDER BY fileContentId DESC";
			ResultSetSingleHandler<FileContentVO> handler = new ResultSetSingleHandler<FileContentVO>() {
				@Override
				protected FileContentVO handleRow(ResultSet rs) throws SQLException {
					return toVO(rs);
				}
			};
			return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
		}
		return null;
	}

	/**
	 * Read file content from input stream and set it to the FsFileContent table's fileContent blob.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @param inputStream
	 * @throws SQLException
	 */
	public boolean writeFileContent(Connection conn, int fileContentId, InputStream inputStream) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String updateSQL = "UPDATE " + getTableName() + " SET fileContent=? WHERE fileContentId=?";

			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setBinaryStream(1, inputStream);
			pstmt.setInt(2, fileContentId);

			int updatedRowCount = pstmt.executeUpdate();
			if (updatedRowCount == 1) {
				return true;
			}
		} finally {
			DatabaseUtil.closeQuietly(pstmt, true);
		}
		return false;
	}

	/**
	 * Read file content from input stream and set it to the FsFileContent table's fileContent blob.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @param bytes
	 * @throws SQLException
	 */
	public boolean writeFileContent(Connection conn, int fileContentId, byte[] bytes) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String updateSQL = "UPDATE " + getTableName() + " SET fileContent=? WHERE fileContentId=?";

			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setBytes(1, bytes);
			pstmt.setInt(2, fileContentId);

			int updatedRowCount = pstmt.executeUpdate();
			if (updatedRowCount == 1) {
				return true;
			}
		} finally {
			DatabaseUtil.closeQuietly(pstmt, true);
		}
		return false;
	}

	/**
	 * Read file content from input stream and set it to the FsFileContent table's fileContent blob.
	 * 
	 * Used for Postgres database.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @param inputStream
	 * @param length
	 * @throws SQLException
	 */
	public boolean writeFileContentPostgres(Connection conn, int fileContentId, InputStream inputStream, int length) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			String updateSQL = "UPDATE " + getTableName() + " SET fileContent=? WHERE fileContentId=?";

			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setBinaryStream(1, inputStream, length); // need to specify length of the input stream.
			pstmt.setInt(2, fileContentId);

			int updatedRowCount = pstmt.executeUpdate();
			if (updatedRowCount == 1) {
				return true;
			}
		} finally {
			DatabaseUtil.closeQuietly(pstmt, true);
		}
		return false;
	}

	/**
	 * Get the input stream of a file content record with specified file content id. Client which calls this method is responsible for closing the InputStream
	 * object.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @return return the input stream of a file content record, if a file content record is found. return null if no file content record is found.
	 * @throws SQLException
	 */
	public InputStream readFileContent(Connection conn, int fileContentId) throws SQLException {
		InputStream inputStream = null;

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileContentId=?";
			pstmt = conn.prepareStatement(querySQL);
			pstmt.setInt(1, fileContentId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				inputStream = rs.getBinaryStream("fileContent");
			}
		} finally {
			DatabaseUtil.closeQuietly(rs, true);
			DatabaseUtil.closeQuietly(pstmt, true);
		}
		return inputStream;
	}

	/**
	 * Get the bytes array of a file content record with specified file content id.
	 * 
	 * This method is for Postgres database where "fileContent" column type is "bytea".
	 * 
	 * Used for Postgres database.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @return return the input stream of a file content record, if a file content record is found. return null if no file content record is found.
	 * @throws SQLException
	 */
	public byte[] readFileContentPostgres(Connection conn, int fileContentId) throws SQLException {
		byte[] bytes = null;

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileContentId=?";
			pstmt = conn.prepareStatement(querySQL);
			pstmt.setInt(1, fileContentId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				bytes = rs.getBytes("fileContent");
			}
		} finally {
			DatabaseUtil.closeQuietly(rs, true);
			DatabaseUtil.closeQuietly(pstmt, true);
		}
		return bytes;
	}

	/**
	 * Delete file content record by file id.
	 * 
	 * @param conn
	 * @param fileId
	 * @return true if a file content record is deleted. return false if no file content record is deleted.
	 * @throws SQLException
	 */
	public boolean deleteByFileId(Connection conn, int fileId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE fileId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { fileId }, -1);
	}

	/**
	 * Delete file content record by file content id.
	 * 
	 * @param conn
	 * @param fileContentId
	 * @return true if a file content record is deleted. return false if no file content record is deleted.
	 * @throws SQLException
	 */
	public boolean deleteByFileContentId(Connection conn, int fileContentId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE fileContentId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { fileContentId }, 1);
	}

}
