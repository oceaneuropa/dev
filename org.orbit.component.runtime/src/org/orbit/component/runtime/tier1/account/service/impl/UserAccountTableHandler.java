package org.orbit.component.runtime.tier1.account.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.model.account.UserAccount;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

/*
 * Number of records: 10k - 1000k
 * 
 * @see IndexItemRequestTableHandler
 */
public class UserAccountTableHandler implements DatabaseTableAware {

	public static UserAccountTableHandler INSTANCE = new UserAccountTableHandler();

	@Override
	public String getTableName() {
		return "UserAccount";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    accountId varchar(50) NOT NULL,");
			sb.append("    username varchar(500) NOT NULL,");
			sb.append("    password varchar(500) NOT NULL,");
			sb.append("    firstName varchar(500),");
			sb.append("    lastName varchar(500),");
			sb.append("    email varchar(500) NOT NULL,");
			sb.append("    phone varchar(50),");
			sb.append("    properties varchar(20000) DEFAULT NULL,");
			sb.append("	   creationTime varchar(50) DEFAULT NULL,");
			sb.append("	   lastUpdateTime varchar(50) DEFAULT NULL,");
			sb.append("    activated boolean NOT NULL DEFAULT false,");
			sb.append("    PRIMARY KEY (accountId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    accountId varchar(50) NOT NULL,");
			sb.append("    username varchar(500) NOT NULL,");
			sb.append("    password varchar(500) NOT NULL,");
			sb.append("    firstName varchar(500),");
			sb.append("    lastName varchar(500),");
			sb.append("    email varchar(500) NOT NULL,");
			sb.append("    phone varchar(50),");
			sb.append("    properties varchar(20000) DEFAULT NULL,");
			sb.append("	   creationTime varchar(50) DEFAULT NULL,");
			sb.append("	   lastUpdateTime varchar(50) DEFAULT NULL,");
			sb.append("    activated boolean NOT NULL DEFAULT false,");
			sb.append("    PRIMARY KEY (accountId)");
			sb.append(");");
		}

		return sb.toString();
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	/**
	 * Convert a ResultSet record to a UserAccount object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static UserAccount toUserAccount(ResultSet rs) throws SQLException {
		String accountId = rs.getString("accountId");
		String username = rs.getString("username");
		String password = rs.getString("password");
		String email = rs.getString("email");
		String firstName = rs.getString("firstName");
		String lastName = rs.getString("lastName");
		String phone = rs.getString("phone");
		String creationTimeString = rs.getString("creationTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");
		boolean activated = rs.getBoolean("activated");
		String propertiesString = rs.getString("properties");

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		Date creationTime = creationTimeString != null ? DateUtil.toDate(creationTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new UserAccount(accountId, username, password, email, firstName, lastName, phone, activated, properties, creationTime, lastUpdateTime);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<UserAccount> getList(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY username ASC";
		ResultSetListHandler<UserAccount> handler = new ResultSetListHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toUserAccount(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public boolean accountIdExists(Connection conn, String accountId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE accountId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { accountId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public boolean usernameExists(Connection conn, String username) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE username=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { username }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean emailExists(Connection conn, String email) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE email=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { email }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public UserAccount getByAccountId(Connection conn, String accountId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE accountId=?";
		ResultSetSingleHandler<UserAccount> handler = new ResultSetSingleHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toUserAccount(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { accountId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public UserAccount getByUsername(Connection conn, String username) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE username=?";
		ResultSetSingleHandler<UserAccount> handler = new ResultSetSingleHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toUserAccount(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { username }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public UserAccount getByEmail(Connection conn, String email) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE email=?";
		ResultSetSingleHandler<UserAccount> handler = new ResultSetSingleHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toUserAccount(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { email }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param username
	 * @param password
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param creationTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public UserAccount create(Connection conn, String accountId, String username, String password, String email, String firstName, String lastName, String phone, Date creationTime, Date lastUpdateTime) throws SQLException {
		if (creationTime == null) {
			creationTime = new Date();
		}
		if (lastUpdateTime == null) {
			lastUpdateTime = creationTime;
		}
		String creationTimeString = DateUtil.toString(creationTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		String insertSQL = "INSERT INTO " + getTableName() + " (accountId, username, password, email, firstName, lastName, phone, creationTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { accountId, username, password, email, firstName, lastName, phone, creationTimeString, lastUpdateTimeString }, 1);
		if (succeed) {
			return getByAccountId(conn, accountId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param activated
	 * @return
	 * @throws SQLException
	 */
	public boolean setActivated(Connection conn, String accountId, boolean activated) throws SQLException {
		UserAccount userAccount = getByAccountId(conn, accountId);
		if (userAccount == null) {
			return false;
		}
		boolean oldActivated = userAccount.isActivated();
		if (oldActivated == activated) {
			return true;
		}
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET activated=? WHERE accountId=?", new Object[] { activated, accountId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param password
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePassword(Connection conn, String accountId, String password, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET password=?, lastUpdateTime=? WHERE accountId=?", new Object[] { password, lastUpdateTimeString, accountId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param firstName
	 * @param lastName
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String accountId, String firstName, String lastName, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET firstName=?, lastName=?, lastUpdateTime=? WHERE accountId=?", new Object[] { firstName, lastName, lastUpdateTimeString, accountId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param email
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateEmail(Connection conn, String accountId, String email, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET email=?, lastUpdateTime=? WHERE accountId=?", new Object[] { email, lastUpdateTimeString, accountId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @param phone
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePhone(Connection conn, String accountId, String phone, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET phone=?, lastUpdateTime=? WHERE accountId=?", new Object[] { phone, lastUpdateTimeString, accountId }, 1);
	}

	/**
	 * Update last update time.
	 * 
	 * @param conn
	 * @param accountId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateLastUpdateTime(Connection conn, String accountId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET lastUpdateTime=? WHERE accountId=?", new Object[] { lastUpdateTimeString, accountId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param accountId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String accountId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE accountId=?", new Object[] { accountId }, 1);
	}

}
