package org.orbit.infra.runtime.indexes.service.other;

import java.sql.Connection;
import java.util.Properties;

import org.orbit.infra.runtime.OrbitConstants;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.PropertiesAware;

public class IndexServiceDatabaseConfigurationV1 extends IndexServiceConfigurationV1 implements PropertiesAware, ConnectionAware {

	protected Properties props;

	/**
	 * 
	 * @param properties
	 */
	public IndexServiceDatabaseConfigurationV1(Properties properties) {
		this.props = properties;
	}

	@Override
	public Properties getProperties() {
		return this.props;
	}

	@Override
	public Connection getConnection() {
		String driver = (String) this.props.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		String url = (String) this.props.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		String username = (String) this.props.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		String password = (String) this.props.get(OrbitConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		Properties properties = DatabaseUtil.getProperties(driver, url, username, password);
		return DatabaseUtil.getConnection(properties);
	}

}

// String driver = PropertyUtil.getString(this.properties, "index.service.jdbc.driver", "org.postgresql.DriverTmp");
// String url = PropertyUtil.getString(this.properties, "index.service.jdbc.url", "jdbc:postgresql://127.0.0.1:5432/originTmp");
// String username = PropertyUtil.getString(this.properties, "index.service.jdbc.username", "postgresTmp");
// String password = PropertyUtil.getString(this.properties, "index.service.jdbc.password", "adminTmp");

// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
// Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres",
// "admin");
