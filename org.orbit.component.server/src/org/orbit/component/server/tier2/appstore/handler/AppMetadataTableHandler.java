package org.orbit.component.server.tier2.appstore.handler;

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

import org.orbit.component.model.tier2.appstore.AppManifestRTO;
import org.orbit.component.model.tier2.appstore.AppQueryRTO;
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

	public static AppMetadataTableHandler INSTANCE_FOR_MYSQL = new AppMetadataTableHandler(DatabaseTableAware.MYSQL);
	public static AppMetadataTableHandler INSTANCE_FOR_POSTGRESQL = new AppMetadataTableHandler(DatabaseTableAware.POSTGRESQL);

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
			sb.append("    name varchar(500),");
			sb.append("    type varchar(500) NOT NULL,");
			sb.append("    priority int DEFAULT 1000,");
			sb.append("    appManifest varchar(2000),");
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
			sb.append("    name varchar(500),");
			sb.append("    type varchar(500) NOT NULL,");
			sb.append("    priority int DEFAULT 1000,");
			sb.append("    appManifest varchar(2000),");
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
	protected AppManifestRTO toRTO(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String appId = rs.getString("appId");
		String appVersion = rs.getString("appVersion");
		String name = rs.getString("name");
		int priority = rs.getInt("priority");
		String type = rs.getString("type");
		String appManifest = rs.getString("appManifest");
		String description = rs.getString("description");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new AppManifestRTO(id, appId, appVersion, name, type, priority, appManifest, description, dateCreated, dateModified);
	}

	/**
	 * Get apps.
	 * 
	 * @param conn
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public List<AppManifestRTO> getApps(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY priority ASC";
		ResultSetListHandler<AppManifestRTO> handler = new ResultSetListHandler<AppManifestRTO>() {
			@Override
			protected AppManifestRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
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
	public List<AppManifestRTO> getApps(Connection conn, AppQueryRTO query) throws SQLException {
		if (query.isEmpty()) {
			return getApps(conn);
		}

		String appId = query.getAppId();
		String appVersion = query.getAppVersion();
		String name = query.getName();
		String type = query.getType();
		String description = query.getDescription();

		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY priority ASC WHERE";
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

		ResultSetListHandler<AppManifestRTO> handler = new ResultSetListHandler<AppManifestRTO>() {
			@Override
			protected AppManifestRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
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
	public boolean appExists(Connection conn, String appId, String appVersion) throws SQLException {
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
	public AppManifestRTO getApp(Connection conn, String appId, String appVersion) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=? AND appVersion=?";
		ResultSetSingleHandler<AppManifestRTO> handler = new ResultSetSingleHandler<AppManifestRTO>() {
			@Override
			protected AppManifestRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { appId }, handler);
	}

	/**
	 * Get an app.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public AppManifestRTO getApp(Connection conn, int id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<AppManifestRTO> handler = new ResultSetSingleHandler<AppManifestRTO>() {
			@Override
			protected AppManifestRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param appToAdd
	 * @return
	 * @throws SQLException
	 */
	public AppManifestRTO insert(Connection conn, AppManifestRTO appToAdd) throws SQLException {
		String appId = appToAdd.getAppId();
		String appVersion = appToAdd.getAppVersion();
		String name = appToAdd.getName();
		String type = appToAdd.getType();
		int priority = appToAdd.getPriority();
		String appManifest = appToAdd.getAppManifest();
		String description = appToAdd.getDescription();
		long dateCreated = appToAdd.getDateCreated();
		return insert(conn, appId, appVersion, name, type, priority, appManifest, description, dateCreated);
	}

	/**
	 * Insert an app.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param name
	 * @param type
	 * @param priority
	 * @param appManifest
	 * @param description
	 * @param dateCreated
	 * @return
	 * @throws SQLException
	 */
	public AppManifestRTO insert(Connection conn, String appId, String appVersion, String name, String type, int priority, String appManifest, String description, long dateCreated) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (appId, appVersion, name, type, priority, appManifest, description, dateCreated) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { appId, appVersion, name, type, priority, appManifest, description, dateCreated }, 1);
		if (succeed) {
			return getApp(conn, appId, appVersion);
		}
		return null;
	}

	/**
	 * Update an app.
	 * 
	 * @param conn
	 * @param updateAppRequestDTO
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, AppManifestRTO updateAppRequestDTO) throws SQLException {
		int id = updateAppRequestDTO.getId();
		String appId = updateAppRequestDTO.getAppId();
		String appVersion = updateAppRequestDTO.getAppVersion();

		Map<String, Object> columnToValueMap = new LinkedHashMap<String, Object>();

		if (id > 0) {
			// update app by id, which means appId and appVersion could be changed in this context.
			AppManifestRTO existingApp = getApp(conn, id);
			if (existingApp == null) {
				return false;
			}

			String oldAppId = existingApp.getAppId();
			String oldAppVersion = existingApp.getAppVersion();
			String oldName = existingApp.getName();
			String oldType = existingApp.getType();
			int oldPriority = existingApp.getPriority();
			String oldManifest = existingApp.getAppManifest();
			String oldDescription = existingApp.getDescription();
			long oldDateCreated = existingApp.getDateCreated();
			long oldDateModified = existingApp.getDateModified();

			String newAppId = updateAppRequestDTO.getAppId();
			String newAppVersion = updateAppRequestDTO.getAppVersion();
			String newName = updateAppRequestDTO.getName();
			String newType = updateAppRequestDTO.getType();
			int newPriority = updateAppRequestDTO.getPriority();
			String newManifest = updateAppRequestDTO.getAppManifest();
			String newDescription = updateAppRequestDTO.getDescription();
			long newDateCreated = updateAppRequestDTO.getDateCreated();
			long newDateModified = updateAppRequestDTO.getDateModified();

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
			if (oldPriority != newPriority) {
				columnToValueMap.put("priority", newPriority);
			}
			if (!StringUtil.equals(oldManifest, newManifest)) {
				columnToValueMap.put("appManifest", newManifest);
			}
			if (!StringUtil.equals(oldDescription, newDescription)) {
				columnToValueMap.put("description", newDescription);
			}
			if (oldDateCreated != newDateCreated) {
				columnToValueMap.put("dateCreated", newDateCreated);
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

			return DatabaseUtil.update(conn, getTableName(), "id", String.valueOf(id), columnToValueMap);

		} else {
			// update app by appId and appVersion, which means appId and appVersion are not changed in this context.
			assert (appId != null) : "appId is null";
			assert (appVersion != null) : "appVersion is null";

			AppManifestRTO existingApp = getApp(conn, appId, appVersion);
			if (existingApp == null) {
				return false;
			}

			String oldName = existingApp.getName();
			String oldType = existingApp.getType();
			int oldPriority = existingApp.getPriority();
			String oldManifest = existingApp.getAppManifest();
			String oldDescription = existingApp.getDescription();
			long oldDateCreated = existingApp.getDateCreated();
			long oldDateModified = existingApp.getDateModified();

			String newName = updateAppRequestDTO.getName();
			String newType = updateAppRequestDTO.getType();
			int newPriority = updateAppRequestDTO.getPriority();
			String newManifest = updateAppRequestDTO.getAppManifest();
			String newDescription = updateAppRequestDTO.getDescription();
			long newDateCreated = updateAppRequestDTO.getDateCreated();
			long newDateModified = updateAppRequestDTO.getDateModified();

			if (!StringUtil.equals(oldName, newName)) {
				columnToValueMap.put("name", newName);
			}
			if (!StringUtil.equals(oldType, newType)) {
				columnToValueMap.put("type", newType);
			}
			if (oldPriority != newPriority) {
				columnToValueMap.put("priority", newPriority);
			}
			if (!StringUtil.equals(oldManifest, newManifest)) {
				columnToValueMap.put("appManifest", newManifest);
			}
			if (!StringUtil.equals(oldDescription, newDescription)) {
				columnToValueMap.put("description", newDescription);
			}
			if (oldDateCreated != newDateCreated) {
				columnToValueMap.put("dateCreated", newDateCreated);
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
	 * Update priority.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @param priority
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePriority(Connection conn, String appId, String appVersion, int priority) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET priority=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { priority, dateModified, appId, appVersion }, 1);
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
		String updateSQL = "UPDATE " + getTableName() + " SET fileName=?, dateModified=? WHERE appId=? AND appVersion=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { fileName, dateModified, appId, appVersion }, 1);
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
	 * Get app content.
	 * 
	 * @param conn
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 */
	public byte[] getAppContent(Connection conn, String appId, String appVersion) throws SQLException {
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
	 */
	public InputStream getAppContentInputStream(Connection conn, String appId, String appVersion) throws SQLException {
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
			System.out.println(getClass().getName() + ".getAppContent() ### ### Unsupported database: '" + this.database + "'.");
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
	 */
	public boolean setAppContent(Connection conn, String appId, String appVersion, InputStream inputStream) throws SQLException {
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			PreparedStatement pstmt = null;
			try {
				String updateSQL = "UPDATE " + getTableName() + " SET appContent=? WHERE appId=? AND appVersion=?";

				pstmt = conn.prepareStatement(updateSQL);
				pstmt.setBinaryStream(1, inputStream);
				pstmt.setString(2, appId);
				pstmt.setString(3, appVersion);

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
	 * Delete an app.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String appId, String appVersion) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE appId=? && appVersion=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { appId, appVersion }, 1);
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