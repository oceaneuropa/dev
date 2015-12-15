package apache.derby.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * https://db.apache.org/derby/integrate/plugin_help/derby_app.html
 * 
 */
public class Restaurants2 {

	private static String tableName = "RESTAURANTS";
	private static Connection conn = null;
	private static Statement stmt = null;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		createConnection();
		selectRestaurants();

		deleteRestaurants();
		selectRestaurants();

		insertRestaurants(6, "Frys", "PaloAlto");
		selectRestaurants();

		shutdown();
	}

	private static void createConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			conn = DriverManager.getConnection("jdbc:derby:derbyDB1");
		} catch (Exception except) {
			except.printStackTrace();
		}
	}

	private static void deleteRestaurants() {
		try {
			stmt = conn.createStatement();
			stmt.execute("delete from " + tableName);
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 * @param restName
	 * @param cityName
	 */
	private static void insertRestaurants(int id, String restName, String cityName) {
		try {
			stmt = conn.createStatement();
			stmt.execute("insert into " + tableName + " values (" + id + ",'" + restName + "','" + cityName + "')");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private static void selectRestaurants() {
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out.println("\n-------------------------------------------------");

			while (results.next()) {
				int id = results.getInt(1);
				String restName = results.getString(2);
				String cityName = results.getString(3);
				System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private static void shutdown() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				// DriverManager.getConnection("jdbc:derby:derbyDB1;shutdown=true");
				conn.close();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

}
