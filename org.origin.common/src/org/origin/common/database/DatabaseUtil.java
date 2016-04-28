package org.origin.common.database;

import java.sql.Connection;
import java.util.Properties;

public class DatabaseUtil {

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static Connection getConnection(Properties properties) {
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param tableAware
	 * @param db
	 */
	public static void initialize(Connection conn, DatabaseTableAware tableAware, String db) {
		db = checkDB(db);
		if (!tableExist(conn, tableAware.getTableName())) {
			createTable(conn, tableAware.getCreateTableSQL(db));
		}
	}

	/**
	 * 
	 * @param db
	 * @return
	 */
	protected static String checkDB(String db) {
		if (db == null || db.trim().isEmpty()) {
			return DatabaseTableAware.MYSQL;
		}
		if (!DatabaseTableAware.MYSQL.equalsIgnoreCase(db) //
				&& !DatabaseTableAware.POSTGRE.equalsIgnoreCase(db) //
				&& !DatabaseTableAware.ORACLE.equalsIgnoreCase(db) //
				&& !DatabaseTableAware.DB2.equalsIgnoreCase(db) //
				&& !DatabaseTableAware.SQLSERVER.equalsIgnoreCase(db) //
		) {
			return DatabaseTableAware.MYSQL;
		}
		return db;
	}

	/**
	 * Check whether database table exists.
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static boolean tableExist(Connection conn, String tableName) {

		return false;
	}

	/**
	 * Create a database table.
	 * 
	 * @param conn
	 * @param createTableSQL
	 */
	public static void createTable(Connection conn, String createTableSQL) {

	}

}
