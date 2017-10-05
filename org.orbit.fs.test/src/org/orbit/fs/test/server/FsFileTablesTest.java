package org.orbit.fs.test.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.orbit.fs.common.database.DatabaseFileSystemHelper;
import org.orbit.fs.common.database.FileContentTableHandler;
import org.orbit.fs.common.database.FileMetadataTableHandler;
import org.origin.common.jdbc.DatabaseUtil;

public class FsFileTablesTest {

	protected Properties properties;
	protected DatabaseFileSystemHelper helper;

	public FsFileTablesTest() {
		init();
	}

	public void setUp() {
		init();
	}

	protected void init() {
		this.helper = new DatabaseFileSystemHelper();
		this.properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// this.properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

	public FileMetadataTableHandler getFileMetadataHandler() {
		return this.helper.getFileMetadataHandler();
	}

	public FileContentTableHandler getFileContentHandler() {
		return this.helper.getFileContentHandler();
	}

	@Test
	public void test001_listTables() {
		System.out.println("--- --- --- test001_listTables() --- --- ---");

		Connection conn = getConnection();
		try {
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

	@Test
	public void test002_initializeTables() {
		System.out.println("--- --- --- test002_initializeTables() --- --- ---");

		Connection conn = DatabaseUtil.getConnection(properties);
		try {
			DatabaseUtil.initialize(conn, getFileMetadataHandler());
			DatabaseUtil.initialize(conn, getFileContentHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		System.out.println();
	}

	@Test
	public void test003_disposeTables() {
		System.out.println("--- --- --- test003_disposeTables() --- --- ---");

		Connection conn = DatabaseUtil.getConnection(properties);
		try {
			// DatabaseUtil.dispose(conn, metaHandler);
			// DatabaseUtil.dispose(conn, contentHandler);
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
		Result result = JUnitCore.runClasses(FsFileTablesTest.class);
		System.out.println("--- --- --- FsFileTablesTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
