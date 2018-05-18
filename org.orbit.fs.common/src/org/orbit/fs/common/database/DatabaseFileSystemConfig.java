package org.orbit.fs.common.database;

import java.sql.Connection;
import java.util.Properties;

import org.orbit.fs.common.Constants;
import org.orbit.fs.common.FileSystemConfiguration;
import org.origin.common.jdbc.DatabaseUtil;

public class DatabaseFileSystemConfig extends FileSystemConfiguration {

	protected Properties properties;
	protected DatabaseFileSystemHelper helper;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseFileSystemConfig(Properties properties) {
		this.properties = properties;

		String metadataTableName = properties.getProperty(Constants.METADATA_TABLE_NAME);
		if (metadataTableName == null) {
			metadataTableName = Constants.METADATA_TABLE_NAME_DEFAULT_VALUE;
		}
		String contentTableName = properties.getProperty(Constants.CONTENT_TABLE_NAME);
		if (contentTableName == null) {
			contentTableName = Constants.CONTENT_TABLE_NAME_DEFAULT_VALUE;
		}
		this.helper = new DatabaseFileSystemHelper(metadataTableName, contentTableName);
	}

	public Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

	public DatabaseFileSystemHelper getDatabaseFileSystemHelper() {
		return this.helper;
	}

}
