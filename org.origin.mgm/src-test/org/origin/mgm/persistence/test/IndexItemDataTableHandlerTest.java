package org.origin.mgm.persistence.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.JSONUtil;
import org.origin.mgm.model.vo.IndexItemDataVO;
import org.origin.mgm.persistence.impl.IndexItemDataTableHandler;

public class IndexItemDataTableHandlerTest {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(DatabaseUtil.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		properties.setProperty(DatabaseUtil.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/origin");
		properties.setProperty(DatabaseUtil.JDBC_USERNAME, "root");
		properties.setProperty(DatabaseUtil.JDBC_PASSWORD, "admin");

		Connection conn = DatabaseUtil.getConnection(properties);
		IndexItemDataTableHandler dataTableHandler = new IndexItemDataTableHandler();
		try {
			// DatabaseUtil.dropTable(conn, dataTableHandler);

			DatabaseUtil.initialize(conn, dataTableHandler, DatabaseTableAware.MYSQL);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Map<String, Object> props1 = new LinkedHashMap<String, Object>();
			props1.put("url", "http://127.0.0.1:9090/indexservice/v1");
			props1.put("description", "IndexService1");
			props1.put("mydate1", new Date());
			props1.put("myinteger1", new Integer(10));
			props1.put("mylong1", new Long(2000000));
			props1.put("myfloat1", new Float(2.1));
			props1.put("myboolean1", new Boolean(false));
			String contentString1 = JSONUtil.toJsonObject(props1, true).toString();

			Map<String, Object> props2 = new LinkedHashMap<String, Object>();
			props2.put("url", "http://127.0.0.1:9091/indexservice/v1");
			props2.put("description", "IndexService2");
			props2.put("mydate2", new Date());
			props2.put("myinteger2", new Integer(10));
			props2.put("mylong2", new Long(2000000));
			props2.put("myfloat2", new Float(2.1));
			props2.put("myboolean2", new Boolean(false));
			String contentString2 = JSONUtil.toJsonObject(props2, true).toString();

			Map<String, Object> props3 = new LinkedHashMap<String, Object>();
			props3.put("url", "http://127.0.0.1:9093/indexservice/v1");
			props3.put("description", "IndexService2");
			props3.put("mydate3", new Date());
			props3.put("myinteger3", new Integer(10));
			props3.put("mylong3", new Long(2000000));
			props3.put("myfloat3", new Float(2.1));
			props3.put("myboolean3", new Boolean(false));
			String contentString3 = JSONUtil.toJsonObject(props3, true).toString();

			Map<String, Object> props4 = new LinkedHashMap<String, Object>();
			props4.put("url", "http://127.0.0.1:9094/indexservice/v1");
			props4.put("description", "IndexService4");
			props4.put("mydate4", new Date());
			props4.put("myinteger4", new Integer(10));
			props4.put("mylong4", new Long(2000000));
			props4.put("myfloat4", new Float(2.1));
			props4.put("myboolean4", new Boolean(false));
			String contentString4 = JSONUtil.toJsonObject(props4, true).toString();

			Map<String, Object> props5 = new LinkedHashMap<String, Object>();
			props5.put("url", "http://127.0.0.1:9095/indexservice/v1");
			props5.put("description", "IndexService5");
			props5.put("mydate5", new Date());
			props5.put("myinteger5", new Integer(10));
			props5.put("mylong5", new Long(2000000));
			props5.put("myfloat5", new Float(2.1));
			props5.put("myboolean5", new Boolean(false));
			String contentString5 = JSONUtil.toJsonObject(props5, true).toString();

			Map<String, Object> props6 = new LinkedHashMap<String, Object>();
			props6.put("url", "http://127.0.0.1:9096/indexservice/v1");
			props6.put("description", "IndexService6");
			props6.put("mydate6", new Date());
			props6.put("myinteger6", new Integer(10));
			props6.put("mylong6", new Long(2000000));
			props6.put("myfloat6", new Float(2.1));
			props6.put("myboolean6", new Boolean(false));
			String contentString6 = JSONUtil.toJsonObject(props6, true).toString();

			// dataTableHandler.insert(conn, "indexservice", "node1", contentString1);
			// dataTableHandler.insert(conn, "indexservice", "node2", contentString2);
			// dataTableHandler.insert(conn, "indexservice", "node3", contentString3);
			// dataTableHandler.insert(conn, "indexservice", "node4", contentString4);
			// dataTableHandler.insert(conn, "indexservice", "node5", contentString5);
			// dataTableHandler.insert(conn, "indexservice", "node6", contentString6);

			dataTableHandler.updateProperties(conn, "indexservice", "node1", contentString1);
			dataTableHandler.updateProperties(conn, "indexservice", "node2", contentString2);
			dataTableHandler.updateProperties(conn, "indexservice", "node3", contentString3);
			dataTableHandler.updateProperties(conn, "indexservice", "node4", contentString4);
			dataTableHandler.updateProperties(conn, "indexservice", "node5", contentString5);
			dataTableHandler.updateProperties(conn, "indexservice", "node6", contentString6);

			List<IndexItemDataVO> indexItemVOs = dataTableHandler.get(conn);
			for (IndexItemDataVO indexItemVO : indexItemVOs) {
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
