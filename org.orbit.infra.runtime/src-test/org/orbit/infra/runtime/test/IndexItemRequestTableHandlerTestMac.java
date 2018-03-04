package org.orbit.infra.runtime.test;

import static org.orbit.infra.runtime.indexes.service.IndexItemRequestTableHandler.STATUS_PENDING;

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
import org.orbit.infra.model.indexes.IndexItemRequestVO;
import org.orbit.infra.runtime.indexes.service.IndexItemRequestTableHandler;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexItemRequestTableHandlerTestMac {

	protected Properties properties;
	protected IndexItemRequestTableHandler requestTableHandler = IndexItemRequestTableHandler.INSTANCE;

	public IndexItemRequestTableHandlerTestMac() {
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
			List<IndexItemRequestVO> vos = this.requestTableHandler.getRequests(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemRequestVO vo : vos) {
				System.out.println(vo.toString());

				String argumentsString = vo.getArguments();
				Map<String, Object> arguments = JSONUtil.toProperties(argumentsString, true);
				System.out.println(arguments);
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
			List<IndexItemRequestVO> vos = this.requestTableHandler.getRequests(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemRequestVO vo : vos) {
				Integer requestId = vo.getRequestId();
				boolean succeed = this.requestTableHandler.delete(conn, requestId);

				System.out.println("IndexItemRequest with requestId='" + requestId + "' is deleted " + (succeed ? "successfully" : "unsuccessfully") + ".");
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
			Map<String, Object> arguments1 = new HashMap<String, Object>();
			arguments1.put("indexProviderId", indexProviderId);
			arguments1.put("type", "tns1");
			arguments1.put("name", "name1");
			arguments1.put("p1", "v1");
			arguments1.put("p2", "v2");
			String arguments1String = JSONUtil.toJsonString(arguments1);
			IndexItemRequestVO vo1 = this.requestTableHandler.insert(conn, indexProviderId, "create_index_item", arguments1String, STATUS_PENDING, new Date(), null);

			Map<String, Object> arguments2 = new HashMap<String, Object>();
			arguments2.put("indexProviderId", indexProviderId);
			arguments2.put("type", "tns1");
			arguments2.put("name", "name2");
			arguments2.put("p3", "v3");
			arguments2.put("p4", "v4");
			String arguments2String = JSONUtil.toJsonString(arguments2);
			IndexItemRequestVO vo2 = this.requestTableHandler.insert(conn, indexProviderId, "create_index_item", arguments2String, STATUS_PENDING, new Date(), null);

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
		Result result = JUnitCore.runClasses(IndexItemRequestTableHandlerTestMac.class);
		System.out.println("--- --- --- IndexItemRequestTableHandlerTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
