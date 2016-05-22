package org.origin.mgm.service.impl;

import java.sql.Connection;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.mgm.service.IndexServiceConfiguration;

public class DatabaseIndexServiceConfiguration extends IndexServiceConfiguration {

	protected Properties properties;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseIndexServiceConfiguration(Properties properties) {
		this.properties = properties;
	}

	public Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

}
