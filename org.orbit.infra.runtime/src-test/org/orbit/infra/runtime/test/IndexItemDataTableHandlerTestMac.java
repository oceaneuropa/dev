package org.orbit.infra.runtime.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.infra.model.indexes.IndexItemVO;
import org.orbit.infra.runtime.indexes.service.IndexItemDataTableHandler;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexItemDataTableHandlerTestMac {

	protected Properties properties;
	protected IndexItemDataTableHandler dataTableHandler = IndexItemDataTableHandler.INSTANCE;

	public IndexItemDataTableHandlerTestMac() {
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
			List<IndexItemVO> vos = this.dataTableHandler.getIndexItems(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemVO vo : vos) {
				System.out.println(vo.toString());

				String propertiesString = vo.getPropertiesString();
				Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);
				System.out.println(properties);
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
			List<IndexItemVO> vos = this.dataTableHandler.getIndexItems(conn);
			System.out.println("vos.size()=" + vos.size());
			for (IndexItemVO vo : vos) {
				Integer indexItemId = vo.getIndexItemId();
				boolean succeed = this.dataTableHandler.delete(conn, indexItemId);

				System.out.println("IndexItemData with indexItemId='" + indexItemId + "' is deleted " + (succeed ? "successfully" : "unsuccessfully") + ".");
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
			Map<String, Object> properties1 = new HashMap<String, Object>();
			properties1.put("indexProviderId", indexProviderId);
			properties1.put("type", "tns1");
			properties1.put("name", "name1");
			properties1.put("p1", "v1");
			properties1.put("p2", "v2");
			properties1.put("L1", new Long(10000));
			properties1.put("F2", new Float(10.01));
			String propertiess1String = JSONUtil.toJsonString(properties1);
			IndexItemVO vo1 = this.dataTableHandler.insert(conn, indexProviderId, "tns1", "name1", propertiess1String, new Date(), null);

			Map<String, Object> properties2 = new HashMap<String, Object>();
			properties2.put("indexProviderId", indexProviderId);
			properties2.put("type", "tns1");
			properties2.put("name", "name1");
			properties2.put("p3", "v3");
			properties2.put("p4", "v4");
			properties2.put("L3", new Long(20000));
			properties2.put("F4", new Float(20.01));
			String propertiess2String = JSONUtil.toJsonString(properties2);
			IndexItemVO vo2 = this.dataTableHandler.insert(conn, indexProviderId, "tns1", "name2", propertiess2String, new Date(), null);

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
		Result result = JUnitCore.runClasses(IndexItemDataTableHandlerTestMac.class);
		System.out.println("--- --- --- IndexItemDataTableHandlerTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

	public static void _main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/origin");
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, "root");
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		Connection conn = DatabaseUtil.getConnection(properties);
		IndexItemDataTableHandler handler = new IndexItemDataTableHandler();
		try {
			// DatabaseUtil.dropTable(conn, dataTableHandler);

			DatabaseUtil.initialize(conn, handler);

			Map<String, Object> props1 = new LinkedHashMap<String, Object>();
			props1.put("url", "http://127.0.0.1:9090/indexservice/v1");
			props1.put("description", "IndexService1");
			props1.put("mydate1", new Date());
			props1.put("myinteger1", new Integer(10));
			props1.put("mylong1", new Long(2000000));
			props1.put("myfloat1", new Float(2.1));
			props1.put("myboolean1", new Boolean(false));
			String contentString1 = JSONUtil.toJsonString(props1);

			Map<String, Object> props2 = new LinkedHashMap<String, Object>();
			props2.put("url", "http://127.0.0.1:9091/indexservice/v1");
			props2.put("description", "IndexService2");
			props2.put("mydate2", new Date());
			props2.put("myinteger2", new Integer(10));
			props2.put("mylong2", new Long(2000000));
			props2.put("myfloat2", new Float(2.1));
			props2.put("myboolean2", new Boolean(false));
			String contentString2 = JSONUtil.toJsonString(props2);

			Map<String, Object> props3 = new LinkedHashMap<String, Object>();
			props3.put("url", "http://127.0.0.1:9093/indexservice/v1");
			props3.put("description", "IndexService2");
			props3.put("mydate3", new Date());
			props3.put("myinteger3", new Integer(10));
			props3.put("mylong3", new Long(2000000));
			props3.put("myfloat3", new Float(2.1));
			props3.put("myboolean3", new Boolean(false));
			String contentString3 = JSONUtil.toJsonString(props3);

			Map<String, Object> props4 = new LinkedHashMap<String, Object>();
			props4.put("url", "http://127.0.0.1:9094/indexservice/v1");
			props4.put("description", "IndexService4");
			props4.put("mydate4", new Date());
			props4.put("myinteger4", new Integer(10));
			props4.put("mylong4", new Long(2000000));
			props4.put("myfloat4", new Float(2.1));
			props4.put("myboolean4", new Boolean(false));
			String contentString4 = JSONUtil.toJsonString(props4);

			Map<String, Object> props5 = new LinkedHashMap<String, Object>();
			props5.put("url", "http://127.0.0.1:9095/indexservice/v1");
			props5.put("description", "IndexService5");
			props5.put("mydate5", new Date());
			props5.put("myinteger5", new Integer(10));
			props5.put("mylong5", new Long(2000000));
			props5.put("myfloat5", new Float(2.1));
			props5.put("myboolean5", new Boolean(false));
			String contentString5 = JSONUtil.toJsonString(props5);

			Map<String, Object> props6 = new LinkedHashMap<String, Object>();
			props6.put("url", "http://127.0.0.1:9096/indexservice/v1");
			props6.put("description", "IndexService6");
			props6.put("mydate6", new Date());
			props6.put("myinteger6", new Integer(10));
			props6.put("mylong6", new Long(2000000));
			props6.put("myfloat6", new Float(2.1));
			props6.put("myboolean6", new Boolean(false));
			String contentString6 = JSONUtil.toJsonString(props6);

			// dataTableHandler.insert(conn, "indexservice", "node1", contentString1);
			// dataTableHandler.insert(conn, "indexservice", "node2", contentString2);
			// dataTableHandler.insert(conn, "indexservice", "node3", contentString3);
			// dataTableHandler.insert(conn, "indexservice", "node4", contentString4);
			// dataTableHandler.insert(conn, "indexservice", "node5", contentString5);
			// dataTableHandler.insert(conn, "indexservice", "node6", contentString6);

			// handler.updateProperties(conn, "indexservice", "node1", contentString1);
			// handler.updateProperties(conn, "indexservice", "node2", contentString2);
			// handler.updateProperties(conn, "indexservice", "node3", contentString3);
			// handler.updateProperties(conn, "indexservice", "node4", contentString4);
			// handler.updateProperties(conn, "indexservice", "node5", contentString5);
			// handler.updateProperties(conn, "indexservice", "node6", contentString6);

			List<IndexItemVO> indexItemVOs = handler.getIndexItems(conn);
			for (IndexItemVO indexItemVO : indexItemVOs) {
				System.out.println("=============================================================================================");
				System.out.println(indexItemVO);

				System.out.println("---------------------------------------------------------------------------------------------");
				String jsonString = indexItemVO.getPropertiesString();
				Map<String, Object> props = JSONUtil.toProperties(jsonString, true);
				for (Iterator<Entry<String, Object>> entryItor = props.entrySet().iterator(); entryItor.hasNext();) {
					Entry<String, Object> entry = entryItor.next();
					String propName = entry.getKey();
					Object propValue = entry.getValue();
					System.out.println(propName + " = " + propValue + " (" + propValue.getClass().getName() + ")");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
