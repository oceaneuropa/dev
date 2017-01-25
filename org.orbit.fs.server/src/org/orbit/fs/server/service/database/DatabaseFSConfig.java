package org.orbit.fs.server.service.database;

import java.sql.Connection;
import java.util.Properties;

import org.orbit.fs.server.service.FileSystemServiceConfiguration;
import org.origin.common.jdbc.DatabaseUtil;

public class DatabaseFSConfig extends FileSystemServiceConfiguration {

	protected Properties properties;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseFSConfig(Properties properties) {
		this.properties = properties;
	}

	public Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

}
