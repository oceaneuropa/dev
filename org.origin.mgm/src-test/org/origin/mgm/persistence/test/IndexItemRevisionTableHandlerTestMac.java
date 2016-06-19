package org.origin.mgm.persistence.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.mgm.model.vo.IndexItemRevisionVO;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexItemRevisionTableHandlerTestMac {

	protected Properties properties;
	protected IndexItemRevisionTableHandler revisionTableHandler = IndexItemRevisionTableHandler.INSTANCE;

	public IndexItemRevisionTableHandlerTestMac() {
		this.properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// this.properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
	}

	public void setUp() {
		this.properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// this.properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.properties);
	}

	@Test
	public void test001_select() {
		System.out.println("--- --- --- test001_select() --- --- ---");

		Connection conn = getConnection();
		try {
			List<IndexItemRevisionVO> vos = this.revisionTableHandler.getRevisions(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemRevisionVO vo : vos) {
				System.out.println(vo.toString());

				String argumentsString = vo.getArgumentsString();
				Map<String, Object> arguments = JSONUtil.toProperties(argumentsString, true);
				System.out.println(arguments);

				String undoArgumentsString = vo.getUndoArgumentsString();
				Map<String, Object> undoArguments = JSONUtil.toProperties(undoArgumentsString, true);
				System.out.println(undoArguments);

				System.out.println();
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
	public void test002_delete() {
		System.out.println("--- --- --- test002_delete() --- --- ---");

		Connection conn = getConnection();
		try {
			List<IndexItemRevisionVO> vos = this.revisionTableHandler.getRevisions(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemRevisionVO vo : vos) {
				Integer revisionId = vo.getRevisionId();
				boolean succeed = this.revisionTableHandler.delete(conn, revisionId);

				System.out.println("IndexItemRevision with revisionId='" + revisionId + "' is deleted " + (succeed ? "successfully" : "unsuccessfully") + ".");
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
	public void test003_insert() {
		System.out.println("--- --- --- test003_insert() --- --- ---");

		String indexProviderId = "origin.index.provider";

		Connection conn = getConnection();
		try {
			// ---------------------------------------------------------------------------------------------------------------------------------------------------
			Map<String, Object> arguments1 = new HashMap<String, Object>();
			arguments1.put("indexProviderId", indexProviderId);
			arguments1.put("namespace", "tns1");
			arguments1.put("name", "name1");
			arguments1.put("p1", "v1");
			arguments1.put("p2", "v2");
			String arguments1String = JSONUtil.toJsonString(arguments1);

			Map<String, Object> undoArguments1 = new HashMap<String, Object>();
			undoArguments1.put("indexItemId", 1);
			String undoArguments1String = JSONUtil.toJsonString(undoArguments1);

			IndexItemRevisionVO vo1 = this.revisionTableHandler.insert(conn, indexProviderId, "create_index_item", arguments1String, "delete_index_item", undoArguments1String, new Date());

			// ---------------------------------------------------------------------------------------------------------------------------------------------------
			Map<String, Object> arguments2 = new HashMap<String, Object>();
			arguments2.put("indexProviderId", indexProviderId);
			arguments2.put("namespace", "tns1");
			arguments2.put("name", "name2");
			arguments2.put("p3", "v3");
			arguments2.put("p4", "v4");
			String arguments2String = JSONUtil.toJsonString(arguments2);

			Map<String, Object> undoArguments2 = new HashMap<String, Object>();
			undoArguments2.put("indexItemId", 2);
			String undoArguments2String = JSONUtil.toJsonString(undoArguments2);

			IndexItemRevisionVO vo2 = this.revisionTableHandler.insert(conn, indexProviderId, "create_index_item", arguments2String, "delete_index_item", undoArguments2String, new Date());

			// ---------------------------------------------------------------------------------------------------------------------------------------------------
			System.out.println("vo1 = " + vo1);
			System.out.println("vo2 = " + vo2);

		} catch (SQLException e) {
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
		Result result = JUnitCore.runClasses(IndexItemRevisionTableHandlerTestMac.class);
		System.out.println("--- --- --- IndexItemRevisionTableHandlerTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
