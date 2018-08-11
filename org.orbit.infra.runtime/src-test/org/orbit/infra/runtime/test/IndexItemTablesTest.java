package org.orbit.infra.runtime.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.origin.common.jdbc.DatabaseUtil;

import other.orbit.infra.runtime.indexes.service.IndexItemDataTableHandler;
import other.orbit.infra.runtime.indexes.service.IndexItemRequestTableHandler;
import other.orbit.infra.runtime.indexes.service.IndexItemRevisionTableHandler;

public class IndexItemTablesTest {

	protected Properties properties;
	protected IndexItemRequestTableHandler requestHandler = IndexItemRequestTableHandler.INSTANCE;
	protected IndexItemDataTableHandler dataHandler = IndexItemDataTableHandler.INSTANCE;
	protected IndexItemRevisionTableHandler revisionHandler = IndexItemRevisionTableHandler.INSTANCE;

	public IndexItemTablesTest() {
		this.properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// this.properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
	}

	public void setUp() {
		this.properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// this.properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.properties);
	}

	@Ignore
	@Test
	public void test001_listTables() {
		System.out.println("--- --- --- test001_listTables() --- --- ---");

		Connection conn = null;
		try {
			conn = getConnection();
			List<String> tableNames = DatabaseUtil.getTableNames(conn);
			System.out.println("Num of tables: " + tableNames.size());
			for (String tableName : tableNames) {
				System.out.println(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test002_initializeTables() {
		System.out.println("--- --- --- test002_initializeTables() --- --- ---");

		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(properties);
			DatabaseUtil.initialize(conn, requestHandler);
			DatabaseUtil.initialize(conn, dataHandler);
			DatabaseUtil.initialize(conn, revisionHandler);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test003_disposeTables() {
		System.out.println("--- --- --- test003_disposeTables() --- --- ---");

		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(properties);
			DatabaseUtil.dispose(conn, requestHandler);
			DatabaseUtil.dispose(conn, dataHandler);
			DatabaseUtil.dispose(conn, revisionHandler);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		System.out.println();
	}

	@Test
	public void test004_clearTables() {
		System.out.println("--- --- --- test004_clearTables() --- --- ---");

		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(properties);
			DatabaseUtil.clear(conn, requestHandler);
			DatabaseUtil.clear(conn, dataHandler);
			DatabaseUtil.clear(conn, revisionHandler);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(IndexItemTablesTest.class);
		System.out.println("--- --- --- IndexItemTablesTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
