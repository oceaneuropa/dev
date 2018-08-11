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
import org.origin.common.jdbc.DatabaseUtil;

import com.osgi.example1.fs.common.vo.FileMetadataVO;
import com.osgi.example1.fs.server.service.database.FsFileMetadataTableHandler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsFileMetadataTableHandlerTestMac {

	protected Properties properties;
	protected FsFileMetadataTableHandler metaHandler = FsFileMetadataTableHandler.INSTANCE;

	public FsFileMetadataTableHandlerTestMac() {
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

	@Test
	public void test001_select() {
		System.out.println("--- --- --- test001_select() --- --- ---");
		doSelect();
		System.out.println();
	}

	protected void doSelect() {
		Connection conn = null;
		try {
			conn = getConnection();
			List<FileMetadataVO> vos = this.metaHandler.getAll(conn);
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

	@Ignore
	@Test
	public void test002_insert() {
		System.out.println("--- --- --- test002_insert() --- --- ---");
		Connection conn = null;
		try {
			conn = getConnection();
			this.metaHandler.insert(conn, -1, "root1.txt", false, 10);
			this.metaHandler.insert(conn, -1, "root2.txt", false, 11);
			this.metaHandler.insert(conn, -1, "root3.txt", false, 12);
			this.metaHandler.insert(conn, -1, "dir1", true, 0);
			this.metaHandler.insert(conn, -1, "dir2", true, 0);
			this.metaHandler.insert(conn, -1, "dir3", true, 0);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test003_update() {
		System.out.println("--- --- --- test003_update() --- --- ---");
		Connection conn = null;
		try {
			conn = getConnection();
			// this.handler.updateParentId(conn, 1, 2);
			this.metaHandler.updateName(conn, 1, "root1a.text");
			this.metaHandler.updateIsDirectory(conn, 1, true);
			this.metaHandler.updateIsHidden(conn, 1, true);
			this.metaHandler.updateCanExecute(conn, 1, false);
			this.metaHandler.updateCanRead(conn, 1, false);
			this.metaHandler.updateCanWrite(conn, 1, false);
			this.metaHandler.updateLength(conn, 1, 200);
			this.metaHandler.updateLastModified(conn, 1, 2000);
			this.metaHandler.updateInTrash(conn, 1, true);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test004_delete() {
		System.out.println("--- --- --- test004_delete() --- --- ---");
		Connection conn = null;
		try {
			conn = getConnection();
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

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FsFileMetadataTableHandlerTestMac.class);
		System.out.println("--- --- --- FsFileMetadataTableHandlerTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
