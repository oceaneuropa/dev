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
	"id": org.eclipse.xsd.editor,
	"version": "1.0.0",
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
			sb.append("    appId varchar(500) NOT NULL,"); // unique id
			sb.append("    namespace varchar(500) NOT NULL,");
			sb.append("    categoryId varchar(500),");
			sb.append("    name varchar(500),");
			sb.append("    version varchar(50),");
			sb.append("    priority int DEFAULT 1000,");
			sb.append("    appManifest varchar(2000),");
			sb.append("    appContent mediumblob,");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (appId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    appId varchar(500) NOT NULL,"); // unique id
			sb.append("    namespace varchar(500) NOT NULL,");
			sb.append("    categoryId varchar(500),");
			sb.append("    name varchar(500),");
			sb.append("    version varchar(50),");
			sb.append("    priority int DEFAULT 1000,");
			sb.append("    appManifest varchar(2000),");
			sb.append("    appContent bytea,");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (appId)");
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
		String appId = rs.getString("appId");
		String namespace = rs.getString("namespace");
		String categoryId = rs.getString("categoryId");
		String name = rs.getString("name");
		String version = rs.getString("version");
		int priority = rs.getInt("priority");
		String appManifest = rs.getString("appManifest");
		String description = rs.getString("description");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		return new AppManifestRTO(appId, namespace, categoryId, name, version, priority, appManifest, description, dateCreated, dateModified);
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
		String namespace = query.getNamespace();
		String categoryId = query.getCategoryId();
		String name = query.getName();
		String version = query.getVersion();
		String description = query.getDescription();

		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY priority ASC WHERE";
		List<Object> params = new ArrayList<Object>();

		boolean appendANDPrefix = false;
		if (appId != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "appId", query.getAppId_oper(), params, appId);
			appendANDPrefix = true;
		}
		if (namespace != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "namespace", query.getNamespace_oper(), params, namespace);
			appendANDPrefix = true;
		}
		if (categoryId != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "categoryId", query.getCategoryId_oper(), params, categoryId);
			appendANDPrefix = true;
		}
		if (name != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "name", query.getName_oper(), params, name);
			appendANDPrefix = true;
		}
		if (version != null) {
			DatabaseUtil.prepareWhereClauseForColumn(querySQL, appendANDPrefix, "version", query.getVersion_oper(), params, version);
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
	 * @return
	 * @throws SQLException
	 */
	public boolean appExists(Connection conn, String appId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { appId }, handler);
	}

	/**
	 * Get an app.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public AppManifestRTO getApp(Connection conn, String appId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
		ResultSetSingleHandler<AppManifestRTO> handler = new ResultSetSingleHandler<AppManifestRTO>() {
			@Override
			protected AppManifestRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { appId }, handler);
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
		String namespace = appToAdd.getNamespace();
		String categoryId = appToAdd.getCategoryId();
		String name = appToAdd.getName();
		String version = appToAdd.getVersion();
		int priority = appToAdd.getPriority();
		String appManifest = appToAdd.getAppManifest();
		String description = appToAdd.getDescription();
		long dateCreated = appToAdd.getDateCreated();
		return insert(conn, appId, namespace, categoryId, name, version, priority, appManifest, description, dateCreated);
	}

	/**
	 * Insert an app.
	 * 
	 * @param conn
	 * @param appId
	 * @param namespace
	 * @param categoryId
	 * @param name
	 * @param version
	 * @param priority
	 * @param appManifest
	 * @param description
	 * @param dateCreated
	 * @return
	 * @throws SQLException
	 */
	public AppManifestRTO insert(Connection conn, String appId, String namespace, String categoryId, String name, String version, int priority, String appManifest, String description, long dateCreated) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (appId, namespace, categoryId, name, version, priority, appManifest, description, dateCreated) VALUES (?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { appId, namespace, categoryId, name, version, appManifest, dateCreated }, 1);
		if (succeed) {
			return getApp(conn, appId);
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
		String appId = updateAppRequestDTO.getAppId();
		assert (appId != null) : "appId is null";

		AppManifestRTO existingApp = getApp(conn, appId);
		if (existingApp == null) {
			return false;
		}

		Map<String, Object> columnToValueMap = new LinkedHashMap<String, Object>();

		String oldNamespace = existingApp.getNamespace();
		String oldCategoryId = existingApp.getCategoryId();
		String oldName = existingApp.getName();
		String oldVersion = existingApp.getVersion();
		int oldPriority = existingApp.getPriority();
		String oldManifest = existingApp.getAppManifest();
		String oldDescription = existingApp.getDescription();
		long oldDateCreated = existingApp.getDateCreated();
		long oldDateModified = existingApp.getDateModified();

		String newNamespace = updateAppRequestDTO.getNamespace();
		String newCategoryId = updateAppRequestDTO.getCategoryId();
		String newName = updateAppRequestDTO.getName();
		String newVersion = updateAppRequestDTO.getVersion();
		int newPriority = updateAppRequestDTO.getPriority();
		String newManifest = updateAppRequestDTO.getAppManifest();
		String newDescription = updateAppRequestDTO.getDescription();
		long newDateCreated = updateAppRequestDTO.getDateCreated();
		long newDateModified = updateAppRequestDTO.getDateModified();

		if (!StringUtil.equals(oldNamespace, newNamespace)) {
			columnToValueMap.put("namespace", newNamespace);
		}
		if (!StringUtil.equals(oldCategoryId, newCategoryId)) {
			columnToValueMap.put("categoryId", newCategoryId);
		}
		if (!StringUtil.equals(oldName, newName)) {
			columnToValueMap.put("name", newName);
		}
		if (!StringUtil.equals(oldVersion, newVersion)) {
			columnToValueMap.put("version", newVersion);
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

		return update(conn, appId, columnToValueMap);
	}

	/**
	 * Update an app.
	 * 
	 * @param conn
	 * @param appId
	 * @param columnToValueMap
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, String appId, Map<String, Object> columnToValueMap) throws SQLException {
		long newDateModified = 0;
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
		return DatabaseUtil.update(conn, getTableName(), "appId", appId, columnToValueMap);
	}

	/**
	 * Update namespace.
	 * 
	 * @param conn
	 * @param appId
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public boolean updateNamespace(Connection conn, String appId, String namespace) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET namespace=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { namespace, dateModified, appId }, 1);
	}

	/**
	 * Update categoryId.
	 * 
	 * @param conn
	 * @param appId
	 * @param categoryId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateCategoryId(Connection conn, String appId, String categoryId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET categoryId=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { categoryId, dateModified, appId }, 1);
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param appId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String appId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, dateModified, appId }, 1);
	}

	/**
	 * Update version.
	 * 
	 * @param conn
	 * @param appId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateVersion(Connection conn, String appId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET version=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, dateModified, appId }, 1);
	}

	/**
	 * Update priority.
	 * 
	 * @param conn
	 * @param appId
	 * @param priority
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePriority(Connection conn, String appId, int priority) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET priority=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { priority, dateModified, appId }, 1);
	}

	/**
	 * Update manifest.
	 * 
	 * @param conn
	 * @param appId
	 * @param manifest
	 * @return
	 * @throws SQLException
	 */
	public boolean updateManifest(Connection conn, String appId, String manifest) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET appManifest=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { manifest, dateModified, appId }, 1);
	}

	/**
	 * Update file name.
	 * 
	 * @param conn
	 * @param appId
	 * @param fileName
	 * @return
	 * @throws SQLException
	 */
	public boolean updateFileName(Connection conn, String appId, String fileName) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET fileName=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { fileName, dateModified, appId }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param appId
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, String appId, String description) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET description=?, dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, dateModified, appId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateModifiedDate(Connection conn, String appId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE appId=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, appId }, 1);
	}

	/**
	 * Get app content.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public byte[] getAppContent(Connection conn, String appId) throws SQLException {
		byte[] bytes = null;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			InputStream inputStream = null;
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);

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
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);

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
	 * @return
	 * @throws SQLException
	 */
	public InputStream getAppContentInputStream(Connection conn, String appId) throws SQLException {
		InputStream inputStream = null;

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(this.database)) {
			ResultSet rs = null;
			PreparedStatement pstmt = null;
			try {
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);

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
				String querySQL = "SELECT * FROM " + getTableName() + " WHERE appId=?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, appId);

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
	 * @param inputStream
	 * @return
	 * @throws SQLException
	 */
	public boolean setAppContent(Connection conn, String appId, InputStream inputStream) throws SQLException {
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
	 * Delete an app.
	 * 
	 * @param conn
	 * @param appId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String appId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE appId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { appId }, 1);
	}

}
