package org.orbit.component.runtime.tier3.domainmanagement.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class NodeConfigTableHandler implements DatabaseTableAware {

	public static NodeConfigTableHandler INSTANCE = new NodeConfigTableHandler();

	@Override
	public String getTableName() {
		return "NodeConfig";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    machineId varchar(500) NOT NULL,");
			sb.append("    transferAgentId varchar(500) NOT NULL,");
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
			sb.append("    transferAgentId varchar(500) NOT NULL,");
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
	 * Convert a ResultSet record to a NodeConfigRTO object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static NodeConfigRTO toRTO(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String machineId = rs.getString("machineId");
		String transferAgentId = rs.getString("transferAgentId");
		String name = rs.getString("name");
		String home = rs.getString("home");
		String hostURL = rs.getString("hostURL");
		String contextRoot = rs.getString("contextRoot");

		return new NodeConfigRTO(id, machineId, transferAgentId, name, home, hostURL, contextRoot);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws SQLException
	 */
	public List<NodeConfigRTO> getNodeConfigs(Connection conn, String machineId, String transferAgentId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? AND transferAgentId=? ORDER BY id ASC";
		ResultSetListHandler<NodeConfigRTO> handler = new ResultSetListHandler<NodeConfigRTO>() {
			@Override
			protected NodeConfigRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId, transferAgentId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public NodeConfigRTO getNodeConfig(Connection conn, String machineId, String transferAgentId, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? AND transferAgentId=? AND id=?";
		ResultSetSingleHandler<NodeConfigRTO> handler = new ResultSetSingleHandler<NodeConfigRTO>() {
			@Override
			protected NodeConfigRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId, transferAgentId, id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean nodeConfigExists(Connection conn, String machineId, String transferAgentId, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE machineId=? AND transferAgentId=? AND id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { machineId, transferAgentId, id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 * @return
	 * @throws SQLException
	 */
	public NodeConfigRTO addNodeConfig(Connection conn, String machineId, String transferAgentId, String id, String name, String home, String hostURL, String contextRoot) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (id, machineId, transferAgentId, name, home, hostURL, contextRoot) VALUES (?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, machineId, transferAgentId, name, home, hostURL, contextRoot }, 1);
		if (succeed) {
			return getNodeConfig(conn, machineId, transferAgentId, id);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String machineId, String transferAgentId, String id, String name) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET name=? WHERE machineId=? AND transferAgentId=? AND id=?", new Object[] { name, machineId, transferAgentId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param home
	 * @return
	 * @throws SQLException
	 */
	public boolean updateHome(Connection conn, String machineId, String transferAgentId, String id, String home) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET home=? WHERE machineId=? AND transferAgentId=? AND id=?", new Object[] { home, machineId, transferAgentId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param hostURL
	 * @return
	 * @throws SQLException
	 */
	public boolean updateHostURL(Connection conn, String machineId, String transferAgentId, String id, String hostURL) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET hostURL=? WHERE machineId=? AND transferAgentId=? AND id=?", new Object[] { hostURL, machineId, transferAgentId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param contextRoot
	 * @return
	 * @throws SQLException
	 */
	public boolean updateContextRoot(Connection conn, String machineId, String transferAgentId, String id, String contextRoot) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET contextRoot=? WHERE machineId=? AND transferAgentId=? AND id=?", new Object[] { contextRoot, machineId, transferAgentId, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteNodeConfig(Connection conn, String machineId, String transferAgentId, String id) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE machineId=? AND transferAgentId=? AND id=?", new Object[] { machineId, transferAgentId, id }, 1);
	}

}
