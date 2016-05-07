package com.osgi.example1.fs.server.test;

import java.io.File;
import java.io.FileInputStream;
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

import com.osgi.example1.fs.server.service.database.FsFileContentTableHandler;
import com.osgi.example1.fs.server.service.database.FsFileMetadataTableHandler;
import com.osgi.example1.fs.server.service.database.FsTableUtil;
import com.osgi.example1.util.FileUtil;
import com.osgi.example1.util.IOUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsFileContentTableHandlerTest {

	protected String database;
	protected Properties properties;
	protected FsFileMetadataTableHandler metaHandler;
	protected FsFileContentTableHandler contentHandler;

	public FsFileContentTableHandlerTest() {
		this.database = DatabaseTableAware.POSTGRE;
		this.properties = getProperties();
		this.metaHandler = new FsFileMetadataTableHandler();
		this.contentHandler = new FsFileContentTableHandler();
	}

	public void setUp() {
		this.database = DatabaseTableAware.POSTGRE;
		this.properties = getProperties();
		this.metaHandler = new FsFileMetadataTableHandler();
		this.contentHandler = new FsFileContentTableHandler();
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

	// @Ignore
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
			if (DatabaseUtil.tableExist(conn, this.contentHandler)) {
				System.out.println("Table " + this.contentHandler.getTableName() + " exists.");
				DatabaseUtil.dropTable(conn, this.contentHandler);

				if (DatabaseUtil.tableExist(conn, this.contentHandler)) {
					System.out.println("Table failed to be dropped.");
				} else {
					System.out.println("Table is dropped.");
				}
			} else {
				System.out.println("Table " + this.contentHandler.getTableName() + " does not exist.");
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
			if (!DatabaseUtil.tableExist(conn, this.contentHandler)) {
				System.out.println("Table " + this.contentHandler.getTableName() + " does not exist.");

				if (DatabaseUtil.initialize(conn, this.contentHandler, this.database)) {
					System.out.println("Table is created.");
				} else {
					System.out.println("Table failed to be created.");
				}
			} else {
				System.out.println("Table " + this.contentHandler.getTableName() + " already exists.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	/**
	 * Need to create bytes array input stream and specify the length of the bytes array to do the file content update.
	 * 
	 * @see http://stackoverflow.com/questions/4299765/saving-java-object-to-postgresql-problem
	 * 
	 */
	@Ignore
	@Test
	public void test004_writeFileContent_Postgres_Mac() {
		System.out.println("--- --- --- test004_writeFileContent_Postgres_Mac() --- --- ---");

		Connection conn = getConnection();
		FileInputStream is1 = null;
		FileInputStream is2 = null;
		FileInputStream is3 = null;
		FileInputStream is4 = null;
		FileInputStream is5 = null;
		FileInputStream is6 = null;
		try {
			File file1 = new File("/Users/yayang/Downloads/test/source/readme1.txt");
			File file2 = new File("/Users/yayang/Downloads/test/source/readme2.txt");
			File file3 = new File("/Users/yayang/Downloads/test/source/readme3.txt");
			File file4 = new File("/Users/yayang/Downloads/test/source/apache-tomcat-8.0.30.tar.gz");
			File file5 = new File("/Users/yayang/Downloads/test/source/BW6 Refactoring.docx");
			File file6 = new File("/Users/yayang/Downloads/test/source/commons-io-2.5-src.zip");

			long length1 = file1.length();
			long length2 = file2.length();
			long length3 = file3.length();
			long length4 = file4.length();
			long length5 = file5.length();
			long length6 = file6.length();

			is1 = new FileInputStream(file1);
			boolean succeed1 = FsTableUtil.writeFileContentPostgres(conn, 1, is1, length1);

			is2 = new FileInputStream(file2);
			boolean succeed2 = FsTableUtil.writeFileContentPostgres(conn, 2, is2, length2);

			is3 = new FileInputStream(file3);
			boolean succeed3 = FsTableUtil.writeFileContentPostgres(conn, 3, is3, length3);

			is4 = new FileInputStream(file4);
			boolean succeed4 = FsTableUtil.writeFileContentPostgres(conn, 4, is4, length4);

			is5 = new FileInputStream(file5);
			boolean succeed5 = FsTableUtil.writeFileContentPostgres(conn, 5, is5, length5);

			is6 = new FileInputStream(file6);
			boolean succeed6 = FsTableUtil.writeFileContentPostgres(conn, 6, is6, length6);

			System.out.println("succeed1 = " + succeed1);
			System.out.println("succeed2 = " + succeed2);
			System.out.println("succeed3 = " + succeed3);
			System.out.println("succeed4 = " + succeed4);
			System.out.println("succeed5 = " + succeed5);
			System.out.println("succeed6 = " + succeed6);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(is1, true);
			IOUtil.closeQuietly(is2, true);
			IOUtil.closeQuietly(is3, true);
			IOUtil.closeQuietly(is4, true);
			IOUtil.closeQuietly(is5, true);
			IOUtil.closeQuietly(is6, true);
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test005_readFileContent_Postgres_Mac() {
		System.out.println("--- --- --- test005_readFileContent_Postgres_Mac() --- --- ---");

		Connection conn = getConnection();
		try {
			File file1 = new File("/Users/yayang/Downloads/test/target/readme1.txt");
			File file2 = new File("/Users/yayang/Downloads/test/target/readme2.txt");
			File file3 = new File("/Users/yayang/Downloads/test/target/readme3.txt");
			File file4 = new File("/Users/yayang/Downloads/test/target/apache-tomcat-8.0.30.tar.gz");
			File file5 = new File("/Users/yayang/Downloads/test/target/BW6 Refactoring.docx");
			File file6 = new File("/Users/yayang/Downloads/test/target/commons-io-2.5-src.zip");

			byte[] bytes1 = FsTableUtil.readFileContentPostgres(conn, 1);
			byte[] bytes2 = FsTableUtil.readFileContentPostgres(conn, 2);
			byte[] bytes3 = FsTableUtil.readFileContentPostgres(conn, 3);
			byte[] bytes4 = FsTableUtil.readFileContentPostgres(conn, 4);
			byte[] bytes5 = FsTableUtil.readFileContentPostgres(conn, 5);
			byte[] bytes6 = FsTableUtil.readFileContentPostgres(conn, 6);

			FileUtil.copyBytesToFile(bytes1, file1);
			FileUtil.copyBytesToFile(bytes2, file2);
			FileUtil.copyBytesToFile(bytes3, file3);
			FileUtil.copyBytesToFile(bytes4, file4);
			FileUtil.copyBytesToFile(bytes5, file5);
			FileUtil.copyBytesToFile(bytes6, file6);

			System.out.println(file1.getAbsolutePath() + " (exists=" + (file1.exists() ? "true" : "false") + ") (length=" + file1.length() + ")");
			System.out.println(file2.getAbsolutePath() + " (exists=" + (file2.exists() ? "true" : "false") + ") (length=" + file2.length() + ")");
			System.out.println(file3.getAbsolutePath() + " (exists=" + (file3.exists() ? "true" : "false") + ") (length=" + file3.length() + ")");
			System.out.println(file4.getAbsolutePath() + " (exists=" + (file4.exists() ? "true" : "false") + ") (length=" + file4.length() + ")");
			System.out.println(file5.getAbsolutePath() + " (exists=" + (file5.exists() ? "true" : "false") + ") (length=" + file5.length() + ")");
			System.out.println(file6.getAbsolutePath() + " (exists=" + (file6.exists() ? "true" : "false") + ") (length=" + file6.length() + ")");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	/**
	 * Need to create bytes array input stream and specify the length of the bytes array to do the file content update.
	 * 
	 * @see http://stackoverflow.com/questions/4299765/saving-java-object-to-postgresql-problem
	 * 
	 */
	@Test
	public void test004_writeFileContent_Postgres_win() {
		System.out.println("--- --- --- test004_writeFileContent_Postgres_win() --- --- ---");

		Connection conn = getConnection();
		FileInputStream is1 = null;
		FileInputStream is2 = null;
		FileInputStream is3 = null;
		FileInputStream is4 = null;
		FileInputStream is5 = null;
		FileInputStream is6 = null;
		try {
			File file1 = new File("C:/downloads/test_source/DownloadAllNumbers.txt");
			File file2 = new File("C:/downloads/test_source/ldiag.log");
			File file3 = new File("C:/downloads/test_source/leaftexture.png");
			File file4 = new File("C:/downloads/test_source/Monkey_Tower_Level_158.png");
			File file5 = new File("C:/downloads/test_source/Song For The Sun.mp3");
			File file6 = new File("C:/downloads/test_source/swagger_v01.rar");

			long length1 = file1.length();
			long length2 = file2.length();
			long length3 = file3.length();
			long length4 = file4.length();
			long length5 = file5.length();
			long length6 = file6.length();

			is1 = new FileInputStream(file1);
			boolean succeed1 = FsTableUtil.writeFileContentPostgres(conn, 1, is1, length1);

			is2 = new FileInputStream(file2);
			boolean succeed2 = FsTableUtil.writeFileContentPostgres(conn, 2, is2, length2);

			is3 = new FileInputStream(file3);
			boolean succeed3 = FsTableUtil.writeFileContentPostgres(conn, 3, is3, length3);

			is4 = new FileInputStream(file4);
			boolean succeed4 = FsTableUtil.writeFileContentPostgres(conn, 4, is4, length4);

			is5 = new FileInputStream(file5);
			boolean succeed5 = FsTableUtil.writeFileContentPostgres(conn, 5, is5, length5);

			is6 = new FileInputStream(file6);
			boolean succeed6 = FsTableUtil.writeFileContentPostgres(conn, 6, is6, length6);

			System.out.println("succeed1 = " + succeed1);
			System.out.println("succeed2 = " + succeed2);
			System.out.println("succeed3 = " + succeed3);
			System.out.println("succeed4 = " + succeed4);
			System.out.println("succeed5 = " + succeed5);
			System.out.println("succeed6 = " + succeed6);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(is1, true);
			IOUtil.closeQuietly(is2, true);
			IOUtil.closeQuietly(is3, true);
			IOUtil.closeQuietly(is4, true);
			IOUtil.closeQuietly(is5, true);
			IOUtil.closeQuietly(is6, true);
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test005_readFileContent_Postgres_win() {
		System.out.println("--- --- --- test005_readFileContent_Postgres_win() --- --- ---");

		Connection conn = getConnection();
		try {
			File file1 = new File("C:/downloads/test_target/DownloadAllNumbers.txt");
			File file2 = new File("C:/downloads/test_target/ldiag.log");
			File file3 = new File("C:/downloads/test_target/leaftexture.png");
			File file4 = new File("C:/downloads/test_target/Monkey_Tower_Level_158.png");
			File file5 = new File("C:/downloads/test_target/Song For The Sun.mp3");
			File file6 = new File("C:/downloads/test_target/swagger_v01.rar");

			byte[] bytes1 = FsTableUtil.readFileContentPostgres(conn, 1);
			byte[] bytes2 = FsTableUtil.readFileContentPostgres(conn, 2);
			byte[] bytes3 = FsTableUtil.readFileContentPostgres(conn, 3);
			byte[] bytes4 = FsTableUtil.readFileContentPostgres(conn, 4);
			byte[] bytes5 = FsTableUtil.readFileContentPostgres(conn, 5);
			byte[] bytes6 = FsTableUtil.readFileContentPostgres(conn, 6);

			FileUtil.copyBytesToFile(bytes1, file1);
			FileUtil.copyBytesToFile(bytes2, file2);
			FileUtil.copyBytesToFile(bytes3, file3);
			FileUtil.copyBytesToFile(bytes4, file4);
			FileUtil.copyBytesToFile(bytes5, file5);
			FileUtil.copyBytesToFile(bytes6, file6);

			System.out.println(file1.getAbsolutePath() + " (exists=" + (file1.exists() ? "true" : "false") + ") (length=" + file1.length() + ")");
			System.out.println(file2.getAbsolutePath() + " (exists=" + (file2.exists() ? "true" : "false") + ") (length=" + file2.length() + ")");
			System.out.println(file3.getAbsolutePath() + " (exists=" + (file3.exists() ? "true" : "false") + ") (length=" + file3.length() + ")");
			System.out.println(file4.getAbsolutePath() + " (exists=" + (file4.exists() ? "true" : "false") + ") (length=" + file4.length() + ")");
			System.out.println(file5.getAbsolutePath() + " (exists=" + (file5.exists() ? "true" : "false") + ") (length=" + file5.length() + ")");
			System.out.println(file6.getAbsolutePath() + " (exists=" + (file6.exists() ? "true" : "false") + ") (length=" + file6.length() + ")");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		Result result = JUnitCore.runClasses(FsFileContentTableHandlerTest.class);
		System.out.println("--- --- --- FsFileContentTableHandlerTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
