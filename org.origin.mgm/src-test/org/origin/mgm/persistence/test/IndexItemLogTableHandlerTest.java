package org.origin.mgm.persistence.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.mgm.persistence.impl.IndexItemLogTableHandler;

public class IndexItemLogTableHandlerTest {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/origin");
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, "root");
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		Connection conn = DatabaseUtil.getConnection(properties);
		IndexItemLogTableHandler logTableHandler = new IndexItemLogTableHandler();
		try {
			// DatabaseUtil.dropTable(conn, logTableHandler);

			DatabaseUtil.initialize(conn, logTableHandler);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			logTableHandler.insert(conn, "add_item", "{type=}");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
