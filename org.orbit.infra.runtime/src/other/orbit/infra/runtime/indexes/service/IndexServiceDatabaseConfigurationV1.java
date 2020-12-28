package other.orbit.infra.runtime.indexes.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.jdbc.DatabaseUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexServiceDatabaseConfigurationV1 extends IndexServiceConfigurationV1 implements ConnectionProvider {

	protected Properties props;

	/**
	 * 
	 * @param properties
	 */
	public IndexServiceDatabaseConfigurationV1(Properties properties) {
		this.props = properties;
	}

	/** ConnectionProvider */
	@Override
	public Connection getConnection() throws SQLException {
		String driver = (String) this.props.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
		String url = (String) this.props.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
		String username = (String) this.props.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
		String password = (String) this.props.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

		Properties properties = DatabaseUtil.getProperties(driver, url, username, password);
		return DatabaseUtil.getConnection(properties);
	}

}

// @Override
// public Properties getProperties() {
// return this.props;
// }

// String driver = PropertyUtil.getString(this.properties, "index.service.jdbc.driver", "org.postgresql.DriverTmp");
// String url = PropertyUtil.getString(this.properties, "index.service.jdbc.url", "jdbc:postgresql://127.0.0.1:5432/originTmp");
// String username = PropertyUtil.getString(this.properties, "index.service.jdbc.username", "postgresTmp");
// String password = PropertyUtil.getString(this.properties, "index.service.jdbc.password", "adminTmp");

// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
// Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres",
// "admin");
