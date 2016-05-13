package org.origin.common.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseUtil {

	public static final String JDBC_DRIVER = "jdbc.driver";
	public static final String JDBC_URL = "jdbc.url";
	public static final String JDBC_USERNAME = "jdbc.username";
	public static final String JDBC_PASSWORD = "jdbc.password";

	/**
	 * Create database connection properties.
	 * 
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Properties getProperties(String driver, String url, String username, String password) {
		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, driver);
		properties.setProperty(DatabaseUtil.JDBC_URL, url);
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, username);
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, password);
		return properties;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static Connection getConnection(Properties properties) {
		String jdbcDriver = properties.getProperty(JDBC_DRIVER);
		String jdbcUrl = properties.getProperty(JDBC_URL);
		String jdbcUsername = properties.getProperty(JDBC_USERNAME);
		String jdbcPassword = properties.getProperty(JDBC_PASSWORD);
		return getConnection(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
	}

	/**
	 * 
	 * @param jdbcDriver
	 * @param jdbcUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getConnection(String jdbcDriver, String jdbcUrl, String username, String password) {
		Connection conn = null;
		try {
			Class.forName(jdbcDriver).newInstance();
			conn = DriverManager.getConnection(jdbcUrl, username, password);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static Connection getMySQLConnection(String host, int port, String schema, String username, String password) {
		return getConnection("com.mysql.jdbc.Driver", "jdbc:mysql://" + host + ":" + port + "/" + schema, username, password);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static Connection getPostgresConnection(String host, int port, String schema, String username, String password) {
		return getConnection("org.postgresql.Driver", "jdbc:postgresql://" + host + ":" + port + "/" + schema, username, password);
	}

	/**
	 * Initialize database table if not exists.
	 * 
	 * @param conn
	 * @param tableAware
	 * @return Return true if table exists or a table is created. Return false if table doesn't exist and cannot be created.
	 * @throws SQLException
	 */
	public static boolean initialize(Connection conn, DatabaseTableAware tableAware) throws SQLException {
		String database = null;
		DatabaseMetaData metaData = conn.getMetaData();
		if (metaData != null) {
			String productName = metaData.getDatabaseProductName();
			if (productName != null) {
				if (productName.toLowerCase().contains("mysql")) {
					database = DatabaseTableAware.MYSQL;
				} else if (productName.toLowerCase().contains("postgresql")) {
					database = DatabaseTableAware.POSTGRESQL;
				} else if (productName.toLowerCase().contains("oracle")) {
					database = DatabaseTableAware.ORACLE;
				} else if (productName.toLowerCase().contains("db2")) {
					database = DatabaseTableAware.DB2;
				} else if (productName.toLowerCase().contains("sqlserver")) {
					database = DatabaseTableAware.SQLSERVER;
				} else {
					System.out.println("### ### Unsupported database product name: '" + productName + "'.");
				}
			}
		}
		database = checkDB(database);

		if (tableExist(conn, tableAware.getTableName())) {
			System.out.println("DatabaseUtil.initialize() table '" + tableAware.getTableName() + "' already exists.");
			return true;
		}

		createTable(conn, tableAware.getCreateTableSQL(database));

		boolean exists = tableExist(conn, tableAware.getTableName());
		if (exists) {
			System.out.println("DatabaseUtil.initialize() table '" + tableAware.getTableName() + "' is created.");
		} else {
			System.out.println("DatabaseUtil.initialize() table '" + tableAware.getTableName() + "' failed to be created.");
		}

		// table exists --- table is created successfully
		// table doesn't exist --- failed to create the table
		return exists ? true : false;
	}

	/**
	 * Dispose database table if exists.
	 * 
	 * @param conn
	 * @param tableAware
	 * @return Return true if table not exists or the table is dropped. Return false if the table exists and cannot be dropped.
	 * @throws SQLException
	 */
	public static boolean dispose(Connection conn, DatabaseTableAware tableAware) throws SQLException {
		if (!tableExist(conn, tableAware.getTableName())) {
			System.out.println("DatabaseUtil.dispose() table '" + tableAware.getTableName() + "' does not exist.");
			return true;
		}

		dropTable(conn, tableAware);

		boolean exists = tableExist(conn, tableAware.getTableName());
		if (exists) {
			System.out.println("DatabaseUtil.dispose() table '" + tableAware.getTableName() + "' failed to be dropped.");
		} else {
			System.out.println("DatabaseUtil.dispose() table '" + tableAware.getTableName() + "' is dropped.");
		}

		// table still exists --- failed to drop the table
		// table doesn't exist --- table is dropped successfully
		return exists ? false : true;
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
				&& !DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(db) //
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
	 * @param tableAware
	 * @return
	 * @throws SQLException
	 */
	public static boolean tableExist(Connection conn, DatabaseTableAware tableAware) throws SQLException {
		return tableExist(conn, tableAware.getTableName());
	}

	/**
	 * Check whether database table exists.
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static boolean tableExist(Connection conn, String tableName) throws SQLException {
		ResultSet rs = null;
		try {
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				// System.out.println(" "
				// + rs.getString("TABLE_CAT") + ", "
				// + rs.getString("TABLE_SCHEM") + ", "
				// + rs.getString("TABLE_NAME") + ", "
				// + rs.getString("TABLE_TYPE") + ", "
				// + rs.getString("REMARKS")
				// );
				String currTableName = rs.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(currTableName)) {
					return true;
				}
			}
		} finally {
			closeQuietly(rs, true);
		}
		return false;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTableNames(Connection conn) throws SQLException {
		List<String> tableNames = new ArrayList<String>();
		ResultSet rs = null;
		try {
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				// System.out.println(" "
				// + rs.getString("TABLE_CAT") + ", "
				// + rs.getString("TABLE_SCHEM") + ", "
				// + rs.getString("TABLE_NAME") + ", "
				// + rs.getString("TABLE_TYPE") + ", "
				// + rs.getString("REMARKS")
				// );
				String tableName = rs.getString("TABLE_NAME");
				if (tableName != null) {
					tableNames.add(tableName);
				}
			}
		} finally {
			closeQuietly(rs, true);
		}
		return tableNames;
	}

	/**
	 * Create a database table.
	 * 
	 * http://dev.mysql.com/doc/refman/5.7/en/create-database.html
	 * 
	 * @param conn
	 * @param createTableSQL
	 * @throws SQLException
	 */
	public static void createTable(Connection conn, String createTableSQL) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(createTableSQL);
		} finally {
			closeQuietly(stmt, true);
		}
	}

	/**
	 * Drop a table.
	 * 
	 * @param conn
	 * @param tableAware
	 * @throws SQLException
	 */
	public static void dropTable(Connection conn, DatabaseTableAware tableAware) throws SQLException {
		dropTable(conn, tableAware.getTableName());
	}

	/**
	 * Drop a table.
	 * 
	 * http://www.tutorialspoint.com/jdbc/jdbc-drop-tables.htm
	 * 
	 * @param conn
	 * @param tableName
	 * @throws SQLException
	 */
	public static void dropTable(Connection conn, String tableName) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE " + tableName);
			// updatedCount is 0 for dropping a table.
			// System.out.println("updatedCount = " + updatedCount);
		} finally {
			closeQuietly(stmt, true);
		}
	}

	/**
	 * Execute SQL to query data from database.
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @param handler
	 * @return
	 * @throws SQLException
	 */
	public static <T> T query(Connection conn, String sql, Object[] params, AbstractResultSetHandler<T> handler) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		T result = null;
		try {
			stmt = conn.prepareStatement(sql);
			fillStatement(stmt, params);
			rs = stmt.executeQuery();
			result = handler.handle(rs);
		} finally {
			closeQuietly(rs, true);
			closeQuietly(stmt, true);
		}
		return result;
	}

	/**
	 * Execute SQL to insert/update/delete one item in database.
	 * 
	 * INSERT INTO table_name (column1,column2,column3,...) VALUES (value1,value2,value3,...);
	 * 
	 * @param conn
	 *            JDBC Connection
	 * @param sql
	 *            SQL script
	 * @param params
	 *            parameters of the SQL
	 * @param expectedRowCount
	 *            expected number of rows to be updated. if -1 is specified, it means 1 or more rows are expected to be updated.
	 * @return return true if number of rows are updated equals to the specified expectedRowCount.
	 * @throws SQLException
	 */
	public static boolean update(Connection conn, String sql, Object[] params, int expectedRowCount) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			fillStatement(stmt, params);
			int updatedRowCount = stmt.executeUpdate();
			if (expectedRowCount <= -1) {
				// if expectedRowCount is not specified, return true as long as there is at least one row updated.
				if (updatedRowCount > 0) {
					return true;
				}
			} else {
				// if expectedRowCount is specified, return true when the number of row updated equals to the expectedRowCount.
				if (updatedRowCount == expectedRowCount) {
					return true;
				}
			}
		} finally {
			closeQuietly(rs, true);
			closeQuietly(stmt, true);
		}
		return false;
	}

	/**
	 * Fill PreparedStatement replacement parameters with the given objects.
	 *
	 * @param stmt
	 *            PreparedStatement to fill
	 * @param params
	 *            Query replacement parameters; null is a valid value to pass in.
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected static void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException {
		// check the parameter count, if we can
		ParameterMetaData pmd = stmt.getParameterMetaData();

		int stmtCount = pmd.getParameterCount();
		int paramsCount = params == null ? 0 : params.length;
		if (stmtCount != paramsCount) {
			throw new SQLException("Wrong number of parameters: expected " + stmtCount + ", was given " + paramsCount);
		}

		// nothing to do here
		if (params == null) {
			return;
		}

		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				stmt.setObject(i + 1, params[i]);
			} else {
				// VARCHAR works with many drivers regardless of the actual column type.
				// Oddly, NULL and OTHER don't work with Oracle's drivers.
				int sqlType = Types.VARCHAR;
				try {
					sqlType = pmd.getParameterType(i + 1);
				} catch (SQLException e) {
					sqlType = Types.VARCHAR;
				}
				stmt.setNull(i + 1, sqlType);
			}
		}
	}

	/**
	 * Close a JDBC Connection quietly.
	 * 
	 * @param conn
	 * @param printStackTrace
	 */
	public static void closeQuietly(Connection conn, boolean printStackTrace) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				if (printStackTrace) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Close a JDBC Statement quietly.
	 * 
	 * @param stmt
	 * @param printStackTrace
	 */
	public static void closeQuietly(Statement stmt, boolean printStackTrace) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				if (printStackTrace) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Close a JDBC ResultSet quietly.
	 * 
	 * @param rs
	 * @param printStackTrace
	 */
	public static void closeQuietly(ResultSet rs, boolean printStackTrace) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				if (printStackTrace) {
					e.printStackTrace();
				}
			}
		}
	}

}
