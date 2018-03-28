package org.orbit.component.runtime.tier3.domainmanagement.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.orbit.component.model.tier3.domain.PlatformConfigRTO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class PlatformConfigTableHandler implements DatabaseTableAware {

	public static PlatformConfigTableHandler INSTANCE = new PlatformConfigTableHandler();

	@Override
	public String getTableName() {
		return "PlatformConfig";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    machineId varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    home varchar(500),");
			sb.append("    hostURL varchar(500),");
			sb.append("    contextRoot varchar(500),");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    machineId varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    home varchar(500),");
			sb.append("    hostURL varchar(500),");
			sb.append("    contextRoot varchar(500),");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a PlatformConfig object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static PlatformConfigRTO toRTO(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String name = rs.getString("name");
		String home = rs.getString("home");
		String hostURL = rs.getString("hostURL");
		String contextRoot = rs.getString("contextRoot");

		return new PlatformConfigRTO(id, name, home, hostURL, contextRoot);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @return
	 * @throws SQLException
	 */
	public List<PlatformConfigRTO> getPlatformConfigs(Connection conn, String machineId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? ORDER BY id ASC";
		ResultSetListHandler<PlatformConfigRTO> handler = new ResultSetListHandler<PlatformConfigRTO>() {
			@Override
			protected PlatformConfigRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public PlatformConfigRTO getPlatformConfig(Connection conn, String machineId, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? AND id=?";
		ResultSetSingleHandler<PlatformConfigRTO> handler = new ResultSetSingleHandler<PlatformConfigRTO>() {
			@Override
			protected PlatformConfigRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId, id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String machineId, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? AND id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId, id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 * @return
	 * @throws SQLException
	 */
	public PlatformConfigRTO add(Connection conn, String machineId, String id, String name, String home, String hostURL, String contextRoot) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (id, machineId, name, home, hostURL, contextRoot) VALUES (?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, machineId, name, home, hostURL, contextRoot }, 1);
		if (succeed) {
			return getPlatformConfig(conn, machineId, id);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String machineId, String id, String name) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET name=? WHERE machineId=? AND id=?", new Object[] { name, machineId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @param home
	 * @return
	 * @throws SQLException
	 */
	public boolean updateHome(Connection conn, String machineId, String id, String home) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET home=? WHERE machineId=? AND id=?", new Object[] { home, machineId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @param hostURL
	 * @return
	 * @throws SQLException
	 */
	public boolean updateHostURL(Connection conn, String machineId, String id, String hostURL) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET hostURL=? WHERE machineId=? AND id=?", new Object[] { hostURL, machineId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @param contextRoot
	 * @return
	 * @throws SQLException
	 */
	public boolean updateContextRoot(Connection conn, String machineId, String id, String contextRoot) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET contextRoot=? WHERE machineId=? AND id=?", new Object[] { contextRoot, machineId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String machineId, String id) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE machineId=? AND id=?", new Object[] { machineId, id }, 1);
	}

}
