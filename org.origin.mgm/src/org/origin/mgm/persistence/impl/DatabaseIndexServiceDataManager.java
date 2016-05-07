package org.origin.mgm.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.mgm.persistence.IndexServiceDataManager;

public class DatabaseIndexServiceDataManager implements IndexServiceDataManager {

	protected Properties properties;
	protected IndexItemDataTableHandler dataTableHandler;
	protected IndexItemLogTableHandler logTableHandler;

	/**
	 * 
	 * @param properties
	 */
	public DatabaseIndexServiceDataManager(Properties properties) {
		this.properties = properties;

		Connection conn = getConnection();
		this.dataTableHandler = new IndexItemDataTableHandler();
		this.logTableHandler = new IndexItemLogTableHandler();
		try {
			DatabaseUtil.initialize(conn, dataTableHandler);
			DatabaseUtil.initialize(conn, logTableHandler);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

}
