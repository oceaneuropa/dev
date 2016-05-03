package com.osgi.example1.fs.server.service.database;

import java.sql.Connection;
import java.util.Properties;

import com.osgi.example1.fs.common.Configuration;

public class DatabaseFileSystemConfiguration extends Configuration {

	protected Properties properties;

	public DatabaseFileSystemConfiguration(Properties properties) {
		this.properties = properties;
	}

	public Connection getConnection() {
		return null;
	}

}
