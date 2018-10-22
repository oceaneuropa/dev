package org.orbit.component.runtime.tier2.appstore.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.model.appstore.AppManifest;
import org.orbit.component.runtime.model.appstore.AppQuery;
import org.origin.common.io.IOUtil;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.util.StringUtil;

/*
 * 

------------------------------------------
{
	"id" : 1,
	"appId": "org.eclipse.xsd.editor",
	"appVersion": "1.0.0",
	"name": "XSD editor",
	"vender": "IBM",
	"description": "XSD editor"
}
------------------------------------------

 * 
 */
public class AppMetadataTableHandler implements DatabaseTableAware {

	// public static AppMetadataTableHandler INSTANCE_FOR_MYSQL = new AppMetadataTableHandler(DatabaseTableAware.MYSQL);
	// public static AppMetadataTableHandler INSTANCE_FOR_POSTGRESQL = new AppMetadataTableHandler(DatabaseTableAware.POSTGRESQL);

	protected String database;

	public AppMetadataTableHandler(String database) {
		this.database = database;
	}

	@Override
	public String getTableName() {
		return "AppMetadata";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id int NOT NULL AUTO_INCREMENT,"); // unique id
			sb.append("    appId varchar(500) NOT NULL,");
			sb.append("    appVersion varchar(100) NOT NULL,");
			sb.append("    type varchar(500),");
			sb.append("    name varchar(500),");
			sb.append("    appManifest varchar(2000),");
			sb.append("    appFileName varchar(500),");
			sb.append("    appFileLength bigint DEFAULT 0,");
			sb.append("    appContent mediumblob,");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id serial NOT NULL,"); // unique id
			sb.append("    appId varchar(500) NOT NULL,");
			sb.append("    appVersion varchar(100) NOT NULL,");
			sb.append("    type varchar(500),");
			sb.append("    name varchar(500),");
			sb.append("    appManifest varchar(2000),");
			sb.append("    appFileName varchar(500),");
			sb.append("    appFileLength bigint DEFAULT 0,");
			sb.append("    appContent bytea,");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Create a AppManifestRTO from a ResultSet record.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected AppManifest toApp(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String appId = rs.getString("appId");
		String appVersion = rs.getString("appVersion");
		String name = rs.getString("name");
		String type = rs.getString("type");
		String appManifest = rs.getString("appManifest");
		String appFileName = rs.getString("appFileName");
		long appFileLength = rs.getLong("appFileLength");
		String description = rs.getString("description");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new AppManifest(id, appId, appVersion, name, type, appManifest, appFileName, appFileLength, description, dateCreated, dateModified);
	}

	/**
	 * Get apps.
	 * 
	 * @param conn
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public List<AppManifest> getApps(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY appId ASC";
		ResultSetListHandler<AppManifest> handler = new ResultSetListHandler<AppManifest>() {
			@Override
			protected AppManifest handleRow(ResultSet rs) throws SQLException {
				return toApp(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get apps.
	 * 
	 * @param conn
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public List<AppManifest> getApps(Connection conn, AppQuery query) throws SQLException {
		if (query.isEmpty()) {
			return getApps(conn);
		}

		String appId = query.getAppId();
		String appVersion = query.getAppVersion();
		String name = query.getName();
		String type = query.getType();
		String description = query.getDescription();

		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY appId ASC WHERE";
		List<Object> params = new ArrayList<Object>();

		boolean appendANDPrefix = false;
		if (appId != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "appId", query.getAppId_oper(), params, appId);
			appendANDPrefix = true;
		}
		if (appVersion != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "appVersion", query.getAppVersion_oper(), params, appVersion);
			appendANDPrefix = true;
		}
		if (name != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "name", query.getName_oper(), params, name);
			appendANDPrefix = true;
		}
		if (type != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "type", query.getType_oper(), params, type);
			appendANDPrefix = true;
		}
		if (description != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "description", query.getDescription_oper(), params, description);
			appendANDPrefix = true;
		}

		ResultSetListHandler<AppManifest> handler = new ResultSetListHandler<AppManifest>() {
			@Override
			protected AppManifest handleRow(ResultSet rs) throws SQLException {
				return toApp(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, params.toArray(new Object[params.size()]), handler);
	}

	/**
	 * Check if an app exists.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String appId, String appVersion) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { appId, appVersion }, handler);
	}

	/**
	 * Get an app.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 */
	public AppManifest getApp(Connection conn, String appId, String appVersion) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
		ResultSetSingleHandler<AppManifest> handler = new ResultSetSingleHandler<AppManifest>() {
			@Override
			protected AppManifest handleRow(ResultSet rs) throws SQLException {
				return toApp(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { appId, appVersion }, handler);
	}

	/**
	 * Get an app.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public AppManifest getApp(Connection conn, int id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<AppManifest> handler = new ResultSetSingleHandler<AppManifest>() {
			@Override
			protected AppManifest handleRow(ResultSet rs) throws SQLException {
				return toApp(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Insert an app.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @param name
	 * @param appManifest
	 * @param appFileName
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public AppManifest insert(Connection conn, String appId, String appVersion, String type, String name, String appManifest, String appFileName, String description, long dateCreated, long dateModified) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (appId, appVersion, type, name, appManifest, appFilename, description, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { appId, appVersion, type, name, appManifest, appFileName, description, dateCreated, dateModified }, 1);
		if (succeed) {
			return getApp(conn, appId, appVersion);
		}
		return null;
	}

	/**
	 * Update an app.
	 * 
	 * @param conn
	 * @param updateAppRequest
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, AppManifest updateAppRequest) throws SQLException {
		int id = updateAppRequest.getId();
		String appId = updateAppRequest.getAppId();
		String appVersion = updateAppRequest.getAppVersion();

		Map<String, Object> columnToValueMap = new LinkedHashMap<String, Object>();

		if (id > 0) {
			// update app by id, which means appId and appVersion could be changed in this context.
			AppManifest existingApp = getApp(conn, id);
			if (existingApp == null) {
				return false;
			}

			String oldAppId = existingApp.getAppId();
			String oldAppVersion = existingApp.getAppVersion();
			String oldType = existingApp.getType();
			String oldName = existingApp.getName();
			String oldManifest = existingApp.getAppManifest();
			String oldFileName = existingApp.getAppFileName();
			String oldDescription = existingApp.getDescription();
			long oldDateModified = existingApp.getDateModified();

			String newAppId = updateAppRequest.getAppId();
			String newAppVersion = updateAppRequest.getAppVersion();
			String newType = updateAppRequest.getType();
			String newName = updateAppRequest.getName();
			String newManifest = updateAppRequest.getAppManifest();
			String newFileName = updateAppRequest.getAppFileName();
			String newDescription = updateAppRequest.getDescription();
			long newDateModified = updateAppRequest.getDateModified();

			if (!StringUtil.equals(oldAppId, newAppId)) {
				columnToValueMap.put("appId", newAppId);
			}
			if (!StringUtil.equals(oldAppVersion, newAppVersion)) {
				columnToValueMap.put("appVersion", newAppVersion);
			}
			if (!StringUtil.equals(oldName, newName)) {
				columnToValueMap.put("name", newName);
			}
			if (!StringUtil.equals(oldType, newType)) {
				columnToValueMap.put("type", newType);
			}
			if (!StringUtil.equals(oldManifest, newManifest)) {
				columnToValueMap.put("appManifest", newManifest);
			}
			if (!StringUtil.equals(oldFileName, newFileName)) {
				columnToValueMap.put("appFileName", newFileName);
			}
			if (!StringUtil.equals(oldDescription, newDescription)) {
				columnToValueMap.put("description", newDescription);
			}
			if (newDateModified <= 0) {
				newDateModified = new Date().getTime();
			}
			if (oldDateModified != newDateModified) {
				columnToValueMap.put("dateModified", newDateModified);
			}

			if (columnToValueMap.containsKey("dateModified")) {
				Object value = columnToValueMap.get("dateModified");
				if (value instanceof Long) {
					newDateModified = (long) value;
				}
			}
			if (newDateModified <= 0) {
				newDateModified = new Date().getTime();
				columnToValueMap.put("dateModified", newDateModified);
			}

			return DatabaseUtil.update(conn, getTableName(), "id", id, columnToValueMap);

		} else {
			// update app by appId and appVersion, which means appId and appVersion are not changed in this context.
			assert (appId != null) : "appId is null";
			assert (appVersion != null) : "appVersion is null";

			AppManifest existingApp = getApp(conn, appId, appVersion);
			if (existingApp == null) {
				return false;
			}

			String oldName = existingApp.getName();
			String oldType = existingApp.getType();
			String oldManifest = existingApp.getAppManifest();
			String oldFileName = existingApp.getAppFileName();
			String oldDescription = existingApp.getDescription();
			long oldDateModified = existingApp.getDateModified();

			String newName = updateAppRequest.getName();
			String newType = updateAppRequest.getType();
			String newManifest = updateAppRequest.getAppManifest();
			String newFileName = updateAppRequest.getAppFileName();
			String newDescription = updateAppRequest.getDescription();
			long newDateModified = updateAppRequest.getDateModified();

			if (!StringUtil.equals(oldName, newName)) {
				columnToValueMap.put("name", newName);
			}
			if (!StringUtil.equals(oldType, newType)) {
				columnToValueMap.put("type", newType);
			}
			if (!StringUtil.equals(oldManifest, newManifest)) {
				columnToValueMap.put("appManifest", newManifest);
			}
			if (!StringUtil.equals(oldFileName, newFileName)) {
				columnToValueMap.put("appFileName", newFileName);
			}
			if (!StringUtil.equals(oldDescription, newDescription)) {
				columnToValueMap.put("description", newDescription);
			}
			if (newDateModified <= 0) {
				newDateModified = new Date().getTime();
			}
			if (oldDateModified != newDateModified) {
				columnToValueMap.put("dateModified", newDateModified);
			}

			if (columnToValueMap.containsKey("dateModified")) {
				Object value = columnToValueMap.get("dateModified");
				if (value instanceof Long) {
					newDateModified = (long) value;
				}
			}
			if (newDateModified <= 0) {
				newDateModified = new Date().getTime();
				columnToValueMap.put("dateModified", newDateModified);
			}

			return DatabaseUtil.update(conn, getTableName(), "appId", appId, "appVersion", appVersion, columnToValueMap);
		}
	}

	/**
	 * Update type.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public boolean updateType(Connection conn, String appId, String appVersion, String type) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET type=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { type, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String appId, String appVersion, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update version.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateVersion(Connection conn, String appId, String appVersion, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET version=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update manifest.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param manifest
	 * @return
	 * @throws SQLException
	 */
	public boolean updateManifest(Connection conn, String appId, String appVersion, String manifest) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET appManifest=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { manifest, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update file name.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	public boolean updateFileName(Connection conn, String appId, String appVersion, String fileName) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET appFileName=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { fileName, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update file length.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param fileLength
	 * @return
	 * @throws SQLException
	 */
	public boolean updateFileLength(Connection conn, String appId, String appVersion, long fileLength) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET appFileLength=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { fileLength, dateModified, appId, appVersion }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, String appId, String appVersion, String description) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET description=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, dateModified, appId, appVersion }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 */
	public boolean updateModifiedDate(Connection conn, String appId, String appVersion) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, appId, appVersion }, 1);
	}

	/**
	 * Delete an app.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String appId, String appVersion) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { appId, appVersion }, 1);
	}

	/**
	 * Get app content.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 * @see FileContentResource
	 * @see DatabaseFileSystem
	 * @see FsFileContentTableHandler
	 * @see FsTableUtil
	 */
	public byte[] getContent(Connection conn, String appId, String appVersion) throws SQLException {
		byte[] bytes = null;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			InputStream inputStream = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);
				pstmt.setString(2, appVersion);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					inputStream = rs.getBinaryStream("appContent");
				}
				if (inputStream != null) {
					try {
						bytes = IOUtil.toByteArray(inputStream);
					} catch (IOException e) {
						throw new SQLException(e);
					}
				}
			} finally {
				IOUtil.closeQuietly(inputStream, true);
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);
				pstmt.setString(2, appVersion);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					bytes = rs.getBytes("appContent");
				}
			} finally {
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}
			return bytes;

		} else {
			System.out.println(getClass().getName() + ".getAppContent() ### ### Unsupported database: '" + this.database + "'.");
		}

		if (bytes == null) {
			bytes = new byte[0];
		}
		return bytes;
	}

	/**
	 * Get app content.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 * @see FileContentResource
	 * @see DatabaseFileSystem
	 * @see FsFileContentTableHandler
	 * @see FsTableUtil
	 */
	public InputStream getContentInputStream(Connection conn, String appId, String appVersion) throws SQLException {
		InputStream inputStream = null;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);
				pstmt.setString(2, appVersion);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					inputStream = rs.getBinaryStream("appContent");
				}
			} finally {
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);
				pstmt.setString(2, appVersion);

				rs = pstmt.executeQuery();
				if (rs.next()) {
					byte[] bytes = rs.getBytes("appContent");
					if (bytes != null) {
						inputStream = new ByteArrayInputStream(bytes);
					}
				}
			} finally {
				DatabaseUtil.closeQuietly(rs, true);
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else {
			System.out.println(getClass().getName() + ".getContentInputStream() ### ### Unsupported database: '" + this.database + "'.");
		}

		return inputStream;
	}

	/**
	 * Set app content.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param inputStream
	 * @return
	 * @throws SQLException
	 * @see FsFileContentTableHandler
	 * @see FsTableUtil
	 */
	public long setContent(Connection conn, String appId, String appVersion, InputStream inputStream) throws SQLException {
		byte[] bytes = null;
		ByteArrayInputStream bais = null;
		try {
			bytes = IOUtil.toByteArray(inputStream);
			bais = new ByteArrayInputStream(bytes);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (bytes == null) {
			bytes = new byte[0];
		}
		int contentLength = bytes.length;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			PreparedStatement pstmt = null;
			try {
				String updateSQL = "UPDATE " + getTableName() + " SET appContent=? WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(updateSQL);

				pstmt.setBinaryStream(1, bais, contentLength);
				pstmt.setString(2, appId);
				pstmt.setString(3, appVersion);

				int updatedRowCount = pstmt.executeUpdate();
				if (updatedRowCount == 1) {
					return contentLength;
				}
			} finally {
				DatabaseUtil.closeQuietly(pstmt, true);
			}

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(this.database)) {
			PreparedStatement pstmt = null;
			try {
				String updateSQL = "UPDATE " + getTableName() + " SET appContent=? WHERE appId=? AND appVersion=?";
				pstmt = conn.prepareStatement(updateSQL);

				pstmt.setBinaryStream(1, bais, contentLength); // need to specify length of the input stream.
				pstmt.setString(2, appId);
				pstmt.setString(3, appVersion);

				int updatedRowCount = pstmt.executeUpdate();
				if (updatedRowCount == 1) {
					return contentLength;
				}
			} finally {
				DatabaseUtil.closeQuietly(pstmt, true);
			}
		}

		IOUtil.closeQuietly(bais, true);
		return 0;
	}

}

/// **
// * Update an app.
// *
// * @param conn
// * @param id
// * @param columnToValueMap
// * @return
// * @throws SQLException
// */
// public boolean update(Connection conn, String id, Map<String, Object> columnToValueMap) throws SQLException {
// long newDateModified = 0;
// if (columnToValueMap.containsKey("dateModified")) {
// Object value = columnToValueMap.get("dateModified");
// if (value instanceof Long) {
// newDateModified = (long) value;
// }
// }
// if (newDateModified <= 0) {
// newDateModified = new Date().getTime();
// columnToValueMap.put("dateModified", newDateModified);
// }
// return DatabaseUtil.update(conn, getTableName(), "id", id, columnToValueMap);
// }

// /**
// * Update ranking.
// *
// * @param conn
// * @param appId
// * @param appVersion
// * @param ranking
// * @return
// * @throws SQLException
// */
// public boolean updateRanking(Connection conn, String appId, String appVersion, int ranking) throws SQLException {
// String updateSQL = "UPDATE " + getTableName() + " SET ranking=?, dateModified=? WHERE appId=? AND appVersion=?";
// long dateModified = new Date().getTime();
// return DatabaseUtil.update(conn, updateSQL, new Object[] { ranking, dateModified, appId, appVersion }, 1);
// }
