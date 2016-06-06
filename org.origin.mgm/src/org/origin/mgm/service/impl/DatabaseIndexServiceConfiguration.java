package org.origin.mgm.service.impl;

import java.sql.Connection;
import java.util.Properties;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.PropertiesAware;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.service.IndexServiceConfiguration;

public class DatabaseIndexServiceConfiguration extends IndexServiceConfiguration implements PropertiesAware, ConnectionAware {

	protected Properties properties;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseIndexServiceConfiguration(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Properties getProperties() {
		return this.properties;
	}

	@Override
	public Connection getConnection() {
		String driver = PropertyUtil.getString(this.properties, "index.service.jdbc.driver", "org.postgresql.DriverTmp");
		String url = PropertyUtil.getString(this.properties, "index.service.jdbc.url", "jdbc:postgresql://127.0.0.1:5432/originTmp");
		String username = PropertyUtil.getString(this.properties, "index.service.jdbc.username", "postgresTmp");
		String password = PropertyUtil.getString(this.properties, "index.service.jdbc.password", "adminTmp");

		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		// Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres",
		// "admin");

		Properties properties = DatabaseUtil.getProperties(driver, url, username, password);
		return DatabaseUtil.getConnection(properties);
	}

}
