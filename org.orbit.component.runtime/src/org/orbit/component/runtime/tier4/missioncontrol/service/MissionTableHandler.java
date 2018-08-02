package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.model.missioncontrol.MissionVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.util.DateUtil;

public class MissionTableHandler implements DatabaseTableAware {

	public static Map<String, MissionTableHandler> tableHandlerMap = new HashMap<String, MissionTableHandler>();

	/**
	 * 
	 * @param conn
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized MissionTableHandler getInstance(Connection conn, String typeId) throws SQLException {
		MissionTableHandler tableHandler = tableHandlerMap.get(typeId);
		if (tableHandler == null) {
			MissionTableHandler newTableHandler = new MissionTableHandler(typeId);

			String tableName = newTableHandler.getTableName();
			if (!DatabaseUtil.tableExist(conn, tableName)) {
				boolean initialized = DatabaseUtil.initialize(conn, newTableHandler);
				if (!initialized) {
					System.err.println("Table '" + tableName + "' cannot be initialized.");
					throw new SQLException("Table '" + tableName + "' cannot be initialized.");
				}
			}

			tableHandlerMap.put(typeId, newTableHandler);
			tableHandler = newTableHandler;
		}
		return tableHandler;
	}

	/**
	 * 
	 * @param conn
	 * @param typeId
	 * @return
	 * @throws SQLException
	 */
	public static synchronized boolean dispose(Connection conn, String typeId) throws SQLException {
		MissionTableHandler tableHandler = tableHandlerMap.get(typeId);
		if (tableHandler != null) {
			String tableName = tableHandler.getTableName();
			if (DatabaseUtil.tableExist(conn, tableHandler)) {
				boolean disposed = DatabaseUtil.dispose(conn, tableHandler);
				if (disposed) {
					tableHandlerMap.remove(typeId);
				} else {
					System.err.println("Table '" + tableName + "' cannot be disposed.");
					throw new SQLException("Table '" + tableName + "' cannot be disposed.");
				}
				return disposed;
			}
		}
		return false;
	}

	protected String typeId;
	protected String tableName;

	protected ResultSetListHandler<MissionVO> rsListHandler;
	protected AbstractResultSetHandler<MissionVO> rsSingleHandler;

	/**
	 * 
	 * @param typeId
	 */
	public MissionTableHandler(String typeId) {
		this.typeId = typeId;
		this.tableName = getTableName(typeId);

		this.rsListHandler = new ResultSetListHandler<MissionVO>() {
			@Override
			protected MissionVO handleRow(ResultSet rs) throws SQLException {
				return createVO(rs);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<MissionVO>() {
			@Override
			public MissionVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return createVO(rs);
				}
				return null;
			}
		};
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public static String getTableName(String typeId) {
		String name = typeId;
		if (name.contains(".")) {
			name = name.replaceAll("\\.", "_");
		}
		String tableName = "Mission_" + name;
		return tableName;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	protected String getPKName() {
		return "id";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	id int NOT NULL AUTO_INCREMENT,";
			sql += "	name varchar(500) NOT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (indexItemId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	id serial NOT NULL,";
			sql += "	name varchar(500) NOT NULL,";
			sql += "	createTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (indexItemId)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * Create a MissionVO from a ResultSet.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected MissionVO createVO(ResultSet rs) throws SQLException {
		Integer id = rs.getInt("id");
		String name = rs.getString("name");
		String createTimeString = rs.getString("createTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");

		Date createTime = createTimeString != null ? DateUtil.toDate(createTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new MissionVO(id, this.typeId, name, createTime, lastUpdateTime);
	}

	/**
	 * Get a list of missions.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<MissionVO> getMissions(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " ASC", null, this.rsListHandler);
	}

	/**
	 * Get a mission by id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public MissionVO getMission(Connection conn, Integer id) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { id }, this.rsSingleHandler);
	}

	/**
	 * Get a mission by name.
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public MissionVO getMission(Connection conn, String name) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE name=?", new Object[] { name }, this.rsSingleHandler);
	}

	/**
	 * Check whether mission name exists.
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean missionNameExists(Connection conn, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { name }, handler);
	}

	/**
	 * Insert an mission.
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public MissionVO insert(Connection conn, String name) throws SQLException {
		return insert(conn, name, null, null);
	}

	/**
	 * Insert an mission.
	 * 
	 * @param conn
	 * @param name
	 * @param createTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public MissionVO insert(Connection conn, String name, Date createTime, Date lastUpdateTime) throws SQLException {
		MissionVO newMissionVO = null;
		if (createTime == null) {
			createTime = new Date();
		}
		if (lastUpdateTime == null) {
			lastUpdateTime = createTime;
		}
		String createTimeString = DateUtil.toString(createTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		Integer id = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (name, createTime, lastUpdateTime) VALUES (?, ?, ?)", new Object[] { name, createTimeString, lastUpdateTimeString });
		if (id > 0) {
			newMissionVO = new MissionVO(id, this.typeId, name, createTime, lastUpdateTime);
		}
		return newMissionVO;
	}

	/**
	 * Delete a mission by id.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, Integer id) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE id=?", new Object[] { id }, 1);
	}

	/**
	 * Delete a mission by name.
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String name) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE name=?", new Object[] { name }, 1);
	}

}
