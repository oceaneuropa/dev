package com.osgi.example1.fs.server.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;

import com.osgi.example1.fs.common.vo.FileMetadataVO;
import com.osgi.example1.fs.server.service.database.FsFileMetadataTableHandler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsFileMetadataTableHandlerTest {

	protected String database;
	protected Properties properties;
	protected FsFileMetadataTableHandler handler;

	public FsFileMetadataTableHandlerTest() {
		this.database = DatabaseTableAware.POSTGRE;
		this.properties = getProperties();
		this.handler = new FsFileMetadataTableHandler();
	}

	public void setUp() {
		this.database = DatabaseTableAware.POSTGRE;
		this.properties = getProperties();
		this.handler = new FsFileMetadataTableHandler();
	}

	protected Properties getProperties() {
		Properties properties = new Properties();

		// MySQL
		// properties.setProperty(DatabaseUtil.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		// properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/origin");
		// properties.setProperty(DatabaseUtil.JDBC_USERNAME, "root");
		// properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		// Postgres
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, "org.postgresql.Driver");
		properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:postgresql://127.0.0.1:5432/origin");
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, "postgres");
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		return properties;
	}

	protected Connection getConnection() {
		// return DatabaseUtil.getPostgresConnection("127.0.0.1", 5432, "origin", "postgres", "admin");
		return DatabaseUtil.getConnection(this.properties);
	}

	@Test
	public void test001_getTables() {
		System.out.println("--- --- --- test001_getTables() --- --- ---");
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

	@Ignore
	@Test
	public void test002_dropTable() {
		System.out.println("--- --- --- test002_dropTable() --- --- ---");
		Connection conn = getConnection();
		try {
			if (DatabaseUtil.tableExist(conn, this.handler)) {
				System.out.println("Table " + this.handler.getTableName() + " exists.");
				DatabaseUtil.dropTable(conn, this.handler);

				if (DatabaseUtil.tableExist(conn, this.handler)) {
					System.out.println("Table failed to be dropped.");
				} else {
					System.out.println("Table is dropped.");
				}
			} else {
				System.out.println("Table " + this.handler.getTableName() + " does not exist.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test003_createTable() {
		System.out.println("--- --- --- test003_createTable() --- --- ---");
		Connection conn = getConnection();
		try {
			if (!DatabaseUtil.tableExist(conn, this.handler)) {
				System.out.println("Table " + this.handler.getTableName() + " does not exist.");

				if (DatabaseUtil.initialize(conn, this.handler, this.database)) {
					System.out.println("Table is created.");
				} else {
					System.out.println("Table failed to be created.");
				}
			} else {
				System.out.println("Table " + this.handler.getTableName() + " already exists.");
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
	public void test004_insert_mac() {
		System.out.println("--- --- --- test004_insert_mac() --- --- ---");
		Connection conn = getConnection();
		try {
			this.handler.insert(conn, -1, "root1.txt", false, 10);
			this.handler.insert(conn, -1, "root2.txt", false, 11);
			this.handler.insert(conn, -1, "root3.txt", false, 12);
			this.handler.insert(conn, -1, "dir1", true, 0);
			this.handler.insert(conn, -1, "dir2", true, 0);
			this.handler.insert(conn, -1, "dir3", true, 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test004_insert_win() {
		System.out.println("--- --- --- test004_insert_win() --- --- ---");
		Connection conn = getConnection();
		try {
			this.handler.insert(conn, -1, "DownloadAllNumbers.txt", false, 0);
			this.handler.insert(conn, -1, "ldiag.log", false, 0);
			this.handler.insert(conn, -1, "leaftexture.png", false, 0);
			this.handler.insert(conn, -1, "Monkey_Tower_Level_158.png", false, 0);
			this.handler.insert(conn, -1, "Song For The Sun.mp3", false, 0);
			this.handler.insert(conn, -1, "swagger_v01.rar", false, 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	@Test
	public void test005_select() {
		System.out.println("--- --- --- test005_select() --- --- ---");
		doSelect();
		System.out.println();
	}

	@Ignore
	@Test
	public void test006_update() {
		System.out.println("--- --- --- test006_update() --- --- ---");
		Connection conn = getConnection();
		try {
			// this.handler.updateParentId(conn, 1, 2);
			this.handler.updateName(conn, 1, "root1a.text");
			this.handler.updateIsDirectory(conn, 1, true);
			this.handler.updateIsHidden(conn, 1, true);
			this.handler.updateCanExecute(conn, 1, false);
			this.handler.updateCanRead(conn, 1, false);
			this.handler.updateCanWrite(conn, 1, false);
			this.handler.updateLength(conn, 1, 200);
			this.handler.updateLastModified(conn, 1, 2000);
			this.handler.updateInTrash(conn, 1, true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test007_select() {
		System.out.println("--- --- --- test007_select() --- --- ---");
		doSelect();
		System.out.println();
	}

	@Ignore
	@Test
	public void test008_delete() {
		System.out.println("--- --- --- test008_delete() --- --- ---");
		Connection conn = getConnection();
		try {
			// this.handler.delete(conn, 7);
			// this.handler.delete(conn, -1, "root1.txt");
			// this.handler.delete(conn, -1, "root2.txt");
			// this.handler.delete(conn, -1, "dir1");
			// this.handler.delete(conn, -1, "dir2");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test009_select() {
		System.out.println("--- --- --- test009_select() --- --- ---");
		doSelect();
		System.out.println();
	}

	protected void doSelect() {
		Connection conn = getConnection();
		try {
			List<FileMetadataVO> vos = this.handler.getAll(conn);
			System.out.println("vos.size()=" + vos.size());
			for (FileMetadataVO vo : vos) {
				System.out.println(vo.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FsFileMetadataTableHandlerTest.class);
		System.out.println("--- --- --- FsFileMetadataTableHandlerTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
