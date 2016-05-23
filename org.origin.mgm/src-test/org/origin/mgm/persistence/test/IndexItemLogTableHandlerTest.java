package org.origin.mgm.persistence.test;

import java.sql.Connection;
import java.util.Date;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.DateUtil;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;

public class IndexItemLogTableHandlerTest {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/origin");
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, "root");
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		Connection conn = DatabaseUtil.getConnection(properties);
		IndexItemRevisionTableHandler logTableHandler = new IndexItemRevisionTableHandler();
		try {
			// DatabaseUtil.dropTable(conn, logTableHandler);
			// DatabaseUtil.initialize(conn, logTableHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String updateTimeString = DateUtil.toString(new Date(), DateUtil.getJdbcDateFormat());
			// logTableHandler.insert(conn, "create_index_item", "{type=}", "create_index_item", "{type=}", updateTimeString);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
