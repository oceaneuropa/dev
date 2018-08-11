package com.osgi.example1.fs.server.service.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;

import com.osgi.example1.fs.server.service.FileSystemConfiguration;

public class DatabaseFileSystemConfiguration extends FileSystemConfiguration {

	protected Properties properties;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseFileSystemConfiguration(Properties properties) {
		this.properties = properties;
	}

	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.properties);
	}

}
