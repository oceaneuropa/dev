package org.origin.common.jdbc.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;

public class JDBCTest1 {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// jdbc.driver
		String jdbcDriver = "com.mysql.jdbc.Driver";
		// jdbc.url
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/origin";
		// jdbc.username
		String jdbcUsername = "root";
		// jdbc.password
		String jdbcPassword = "admin";

		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, jdbcDriver);
		properties.setProperty(DatabaseUtil.JDBC_URL, jdbcUrl);
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, jdbcUsername);
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, jdbcPassword);

		Connection conn = DatabaseUtil.getConnection(properties);

		// Connection conn = DatabaseUtil.getConnection(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		// Connection conn = DatabaseUtil.getMySQLConnection("127.0.0.1", 3306, "origin", "root", "admin");
		if (conn != null) {
			try {
				DatabaseMetaData metaData = conn.getMetaData();
				if (metaData != null) {
					String driverName = metaData.getDriverName();
					String driverVersion = metaData.getDriverVersion();
					String productName = metaData.getDatabaseProductName();
					String productVersion = metaData.getDatabaseProductVersion();
					System.out.println("driverName=" + driverName);
					System.out.println("driverVersion=" + driverVersion);
					System.out.println("productName=" + productName);
					System.out.println("productVersion=" + productVersion);
				}

				System.out.println("Table Names:");
				List<String> tableNames = DatabaseUtil.getTableNames(conn);
				for (String tableName : tableNames) {
					System.out.println(tableName);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DatabaseUtil.closeQuietly(conn, true);
			}
		}
	}

}
