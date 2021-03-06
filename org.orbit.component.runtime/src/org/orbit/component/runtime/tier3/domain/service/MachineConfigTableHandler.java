package org.orbit.component.runtime.tier3.domain.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.orbit.component.runtime.model.domain.MachineConfig;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableProvider;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class MachineConfigTableHandler implements DatabaseTableProvider {

	public static MachineConfigTableHandler INSTANCE = new MachineConfigTableHandler();

	@Override
	public String getTableName() {
		return "MachineConfig";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableProvider.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    ipAddress varchar(500),");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableProvider.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    ipAddress varchar(500),");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Convert a ResultSet record to a MachineConfig object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static MachineConfig toMachineConfig(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String name = rs.getString("name");
		String ipAddress = rs.getString("ipAddress");

		return new MachineConfig(id, name, ipAddress);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<MachineConfig> getMachineConfigs(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<MachineConfig> handler = new ResultSetListHandler<MachineConfig>() {
			@Override
			protected MachineConfig handleRow(ResultSet rs) throws SQLException {
				return toMachineConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public MachineConfig getMachineConfig(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<MachineConfig> handler = new ResultSetSingleHandler<MachineConfig>() {
			@Override
			protected MachineConfig handleRow(ResultSet rs) throws SQLException {
				return toMachineConfig(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @param ipAddress
	 * @return
	 * @throws SQLException
	 */
	public MachineConfig addMachineConfig(Connection conn, String id, String name, String ipAddress) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (id, name, ipAddress) VALUES (?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, name, ipAddress }, 1);
		if (succeed) {
			return getMachineConfig(conn, id);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String id, String name) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET name=? WHERE id=?", new Object[] { name, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateIpAddress(Connection conn, String id, String ipAddress) throws SQLException {
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET ipAddress=? WHERE id=?", new Object[] { ipAddress, id }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String id) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE id=?", new Object[] { id }, 1);
	}

}
