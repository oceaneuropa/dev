package com.osgi.example1.fs.server.service.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

import com.osgi.example1.fs.common.vo.FileMetadataVO;

public class FsFileMetadataTableHandler implements DatabaseTableAware {

	public static FsFileMetadataTableHandler INSTANCE = new FsFileMetadataTableHandler();

	@Override
	public String getTableName() {
		return "FsFileMetadata";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    fileId int NOT NULL AUTO_INCREMENT,");
			sb.append("    parentFileId int NOT NULL DEFAULT -1,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    isDirectory boolean NOT NULL DEFAULT false,");
			sb.append("    isHidden boolean NOT NULL DEFAULT false,");
			sb.append("    canExecute boolean NOT NULL DEFAULT true,");
			sb.append("    canRead boolean NOT NULL DEFAULT true,");
			sb.append("    canWrite boolean NOT NULL DEFAULT true,");
			sb.append("    length bigint DEFAULT 0,");
			sb.append("    lastModified bigint DEFAULT 0,");
			sb.append("    inTrash boolean DEFAULT false,");
			sb.append("    PRIMARY KEY (fileId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    fileId serial NOT NULL,");
			sb.append("    parentFileId int NOT NULL DEFAULT -1,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    isDirectory boolean NOT NULL DEFAULT false,");
			sb.append("    isHidden boolean NOT NULL DEFAULT false,");
			sb.append("    canExecute boolean NOT NULL DEFAULT true,");
			sb.append("    canRead boolean NOT NULL DEFAULT true,");
			sb.append("    canWrite boolean NOT NULL DEFAULT true,");
			sb.append("    length bigint DEFAULT 0,");
			sb.append("    lastModified bigint DEFAULT 0,");
			sb.append("    inTrash boolean DEFAULT false,");
			sb.append("    PRIMARY KEY (fileId)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Create a FileMetadataVO from a ResultSet record.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected FileMetadataVO toVO(ResultSet rs) throws SQLException {
		int fileId = rs.getInt("fileId");
		int parentFileId = rs.getInt("parentFileId");
		String name = rs.getString("name");
		boolean isDirectory = rs.getBoolean("isDirectory");
		boolean isHidden = rs.getBoolean("isHidden");
		boolean canExecute = rs.getBoolean("canExecute");
		boolean canRead = rs.getBoolean("canRead");
		boolean canWrite = rs.getBoolean("canWrite");
		long length = rs.getLong("length");
		long lastModified = rs.getLong("lastModified");

		return new FileMetadataVO(fileId, parentFileId, name, isDirectory, isHidden, canExecute, canRead, canWrite, length, lastModified);
	}

	/**
	 * Return current time as last modified time.
	 * 
	 * @return
	 */
	protected long getLastModifiedTime() {
		return new Date().getTime();
	}

	/**
	 * Get boolean value from specified column.
	 * 
	 * @param conn
	 * @param fileId
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	protected boolean getBoolean(Connection conn, int fileId, final String columnName) throws SQLException {
		String querySQL = "SELECT " + columnName + " FROM " + getTableName() + " WHERE fileId=?";
		ResultSetSingleHandler<Boolean> handler = new ResultSetSingleHandler<Boolean>() {
			@Override
			protected Boolean handleRow(ResultSet rs) throws SQLException {
				boolean value = rs.getBoolean(columnName);
				return value ? true : false;
			}

			@Override
			protected Boolean getEmptyValue() {
				return false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * Get all file records (including in trash and not in trash) in the whole table.
	 * 
	 * @param conn
	 * @return return a list of file records.
	 * @throws SQLException
	 */
	public List<FileMetadataVO> getAll(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY fileId ASC";
		ResultSetListHandler<FileMetadataVO> handler = new ResultSetListHandler<FileMetadataVO>() {
			@Override
			protected FileMetadataVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get file records (including in trash and not in trash) in a parent directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> get(Connection conn, int parentFileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? ORDER BY fileId ASC";
		ResultSetListHandler<FileMetadataVO> handler = new ResultSetListHandler<FileMetadataVO>() {
			@Override
			protected FileMetadataVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId }, handler);
	}

	/**
	 * Get file records not in trash in a parent directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param includeInTrash
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> get(Connection conn, int parentFileId, boolean includeInTrash) throws SQLException {
		if (includeInTrash) {
			return get(conn, parentFileId);
		} else {
			return getInOrNotInTrash(conn, parentFileId, false);
		}
	}

	/**
	 * Get file records in trash in a parent directory.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @return
	 * @throws SQLException
	 */
	public List<FileMetadataVO> getInTrash(Connection conn, int parentFileId) throws SQLException {
		return getInOrNotInTrash(conn, parentFileId, true);
	}

	/**
	 * Get file records in a directory. When parentFileId is -1, this method will return the root file records.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param inTrash
	 * @return return a list of file records.
	 * @throws SQLException
	 */
	private List<FileMetadataVO> getInOrNotInTrash(Connection conn, int parentFileId, boolean inTrash) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND inTrash=? ORDER BY fileId ASC";
		ResultSetListHandler<FileMetadataVO> handler = new ResultSetListHandler<FileMetadataVO>() {
			@Override
			protected FileMetadataVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, inTrash }, handler);
	}

	/**
	 * Check if a file record with specified file id exists.
	 * 
	 * @param conn
	 * @param fileId
	 * @return return true if file record is found. return false if file record is not found.
	 * @throws SQLException
	 */
	public boolean hasRecord(Connection conn, int fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * Check if a file record with specified parent file id and file name exists.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return return true if file record is found. return false if file record is not found.
	 * @throws SQLException
	 */
	public boolean hasRecord(Connection conn, int parentFileId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, name }, handler);
	}

	/**
	 * Get a file record with specified fileId.
	 * 
	 * @param conn
	 * @param fileId
	 * @return return the file record. return null if not found.
	 * @throws SQLException
	 */
	public FileMetadataVO getById(Connection conn, int fileId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE fileId=?";
		ResultSetSingleHandler<FileMetadataVO> handler = new ResultSetSingleHandler<FileMetadataVO>() {
			@Override
			protected FileMetadataVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { fileId }, handler);
	}

	/**
	 * Get a file record with specified parent file id and file name.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return return the file record. return null if not found.
	 * @throws SQLException
	 */
	public FileMetadataVO getByName(Connection conn, int parentFileId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		ResultSetSingleHandler<FileMetadataVO> handler = new ResultSetSingleHandler<FileMetadataVO>() {
			@Override
			protected FileMetadataVO handleRow(ResultSet rs) throws SQLException {
				return toVO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentFileId, name }, handler);
	}

	/**
	 * Check whether a file is a directory.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isDirectory(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "isDirectory");
	}

	/**
	 * Check whether a file is hidden.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isHidden(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "isHidden");
	}

	/**
	 * Check whether a file can execute.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean canExecute(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "canExecute");
	}

	/**
	 * Check whether a file can read.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean canRead(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "canRead");
	}

	/**
	 * Check whether a file can write.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean canWrite(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "canWrite");
	}

	/**
	 * Check whether a file is marked as deleted.
	 * 
	 * @param conn
	 * @param fileId
	 * @return
	 * @throws SQLException
	 */
	public boolean isInTrash(Connection conn, int fileId) throws SQLException {
		return getBoolean(conn, fileId, "inTrash");
	}

	/**
	 * Insert a file record to the table.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @param isDirectory
	 * @param length
	 * @return return the new file record. return null of new file record is not created.
	 * @throws SQLException
	 * @throws IOException
	 */
	public FileMetadataVO insert(Connection conn, int parentFileId, String name, boolean isDirectory, long length) throws SQLException, IOException {
		if (hasRecord(conn, parentFileId, name)) {
			throw new IOException("File metadata with same parentFileId and file name already exists.");
		}
		String insertSQL = "INSERT INTO " + getTableName() + " (parentFileId, name, isDirectory, isHidden, canExecute, canRead, canWrite, length, lastModified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { parentFileId, name, isDirectory, false, true, true, true, length, getLastModifiedTime() }, 1);
		if (succeed) {
			return getByName(conn, parentFileId, name);
		}
		return null;
	}

	/**
	 * Update parent fild id.
	 * 
	 * @param conn
	 * @param fileId
	 * @param parentFileId
	 * @return return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateParentId(Connection conn, int fileId, int parentFileId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET parentFileId=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { parentFileId, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file name.
	 * 
	 * @param conn
	 * @param fileId
	 * @param name
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, int fileId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file isDirectory.
	 * 
	 * @param conn
	 * @param fileId
	 * @param isDirectory
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateIsDirectory(Connection conn, int fileId, boolean isDirectory) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET isDirectory=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { isDirectory, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file isHidden.
	 * 
	 * @param conn
	 * @param fileId
	 * @param isHidden
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateIsHidden(Connection conn, int fileId, boolean isHidden) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET isHidden=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { isHidden, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file canExecute.
	 * 
	 * @param conn
	 * @param fileId
	 * @param canExecute
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateCanExecute(Connection conn, int fileId, boolean canExecute) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET canExecute=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { canExecute, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file canRead.
	 * 
	 * @param conn
	 * @param fileId
	 * @param canRead
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateCanRead(Connection conn, int fileId, boolean canRead) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET canRead=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { canRead, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file canWrite.
	 * 
	 * @param conn
	 * @param fileId
	 * @param canWrite
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateCanWrite(Connection conn, int fileId, boolean canWrite) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET canWrite=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { canWrite, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file length.
	 * 
	 * @param conn
	 * @param fileId
	 * @param length
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateLength(Connection conn, int fileId, long length) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET length=?, lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { length, getLastModifiedTime(), fileId }, 1);
	}

	/**
	 * Update file lastModified time.
	 * 
	 * @param conn
	 * @param fileId
	 * @param lastModified
	 * @return true if a file record is updated. return false if no file record is updated.
	 * @throws SQLException
	 */
	public boolean updateLastModified(Connection conn, int fileId, long lastModified) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET lastModified=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { lastModified, fileId }, 1);
	}

	/**
	 * Update file inTrash.
	 * 
	 * @param conn
	 * @param fileId
	 * @param inTrash
	 * @return
	 * @throws SQLException
	 */
	public boolean updateInTrash(Connection conn, int fileId, boolean inTrash) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET inTrash=? WHERE fileId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { inTrash, fileId }, 1);
	}

	/**
	 * Delete a file record with specified fileId.
	 * 
	 * @param conn
	 * @param fileId
	 * @return true if a file record is deleted. return false if no file record is deleted.
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int fileId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE fileId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { fileId }, 1);
	}

	/**
	 * Delete a file record with specified parent file id and file name.
	 * 
	 * @param conn
	 * @param parentFileId
	 * @param name
	 * @return true if a file record is deleted. return false if no file record is deleted.
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, int parentFileId, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE parentFileId=? AND name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { parentFileId, name }, 1);
	}

}
