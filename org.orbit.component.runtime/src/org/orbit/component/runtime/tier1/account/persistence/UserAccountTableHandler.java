package org.orbit.component.runtime.tier1.account.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.orbit.component.model.tier1.account.rto.UserAccount;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.util.DateUtil;

/**
 * @see IndexItemRequestTableHandler
 *
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
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    password varchar(500) NOT NULL,");
			sb.append("    firstName varchar(500),");
			sb.append("    lastName varchar(500),");
			sb.append("    email varchar(500) NOT NULL,");
			sb.append("    phone varchar(50),");
			sb.append("	   creationTime varchar(50) DEFAULT NULL,");
			sb.append("	   lastUpdateTime varchar(50) DEFAULT NULL,");
			sb.append("    activated boolean NOT NULL DEFAULT false,");
			sb.append("    PRIMARY KEY (userId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    userId varchar(500) NOT NULL,");
			sb.append("    password varchar(500) NOT NULL,");
			sb.append("    firstName varchar(500),");
			sb.append("    lastName varchar(500),");
			sb.append("    email varchar(500) NOT NULL,");
			sb.append("    phone varchar(50),");
			sb.append("	   creationTime varchar(50) DEFAULT NULL,");
			sb.append("	   lastUpdateTime varchar(50) DEFAULT NULL,");
			sb.append("    activated boolean NOT NULL DEFAULT false,");
			sb.append("    PRIMARY KEY (userId)");
			sb.append(");");
		}

		return sb.toString();
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	/**
	 * Convert a ResultSet record to a UserAccountRTO object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static UserAccount toRTO(ResultSet rs) throws SQLException {
		String userId = rs.getString("userId");
		String password = rs.getString("password");
		String email = rs.getString("email");
		String firstName = rs.getString("firstName");
		String lastName = rs.getString("lastName");
		String phone = rs.getString("phone");
		String creationTimeString = rs.getString("creationTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");
		boolean activated = rs.getBoolean("activated");

		Date creationTime = creationTimeString != null ? DateUtil.toDate(creationTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new UserAccount(userId, password, email, firstName, lastName, phone, creationTime, lastUpdateTime, activated);
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<UserAccount> getUserAccounts(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY userId ASC";
		ResultSetListHandler<UserAccount> handler = new ResultSetListHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public UserAccount getUserAccount(Connection conn, String userId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=?";
		ResultSetSingleHandler<UserAccount> handler = new ResultSetSingleHandler<UserAccount>() {
			@Override
			protected UserAccount handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public boolean userAccountExists(Connection conn, String userId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE userId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { userId }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
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
	public UserAccount createUserAccount(Connection conn, String userId, String password, String email, String firstName, String lastName, String phone, Date creationTime, Date lastUpdateTime) throws SQLException {
		if (creationTime == null) {
			creationTime = new Date();
		}
		if (lastUpdateTime == null) {
			lastUpdateTime = creationTime;
		}
		String creationTimeString = DateUtil.toString(creationTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		String insertSQL = "INSERT INTO " + getTableName() + " (userId, password, email, firstName, lastName, phone, creationTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { userId, password, email, firstName, lastName, phone, creationTimeString, lastUpdateTimeString }, 1);
		if (succeed) {
			return getUserAccount(conn, userId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param password
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePassword(Connection conn, String userId, String password, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET password=?, lastUpdateTime=? WHERE userId=?", new Object[] { password, lastUpdateTimeString, userId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param firstName
	 * @param lastName
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String userId, String firstName, String lastName, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET firstName=?, lastName=?, lastUpdateTime=? WHERE userId=?", new Object[] { firstName, lastName, lastUpdateTimeString, userId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param email
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateEmail(Connection conn, String userId, String email, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET email=?, lastUpdateTime=? WHERE userId=?", new Object[] { email, lastUpdateTimeString, userId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param phone
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updatePhone(Connection conn, String userId, String phone, Date lastUpdateTime) throws SQLException {
		if (lastUpdateTime == null) {
			lastUpdateTime = new Date();
		}
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET phone=?, lastUpdateTime=? WHERE userId=?", new Object[] { phone, lastUpdateTimeString, userId }, 1);
	}

	/**
	 * Update last update time.
	 * 
	 * @param conn
	 * @param userId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateLastUpdateTime(Connection conn, String userId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET lastUpdateTime=? WHERE userId=?", new Object[] { lastUpdateTimeString, userId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @param activated
	 * @return
	 * @throws SQLException
	 */
	public boolean setUserAccountActivated(Connection conn, String userId, boolean activated) throws SQLException {
		UserAccount userAccount = getUserAccount(conn, userId);
		if (userAccount == null) {
			return false;
		}
		boolean oldActivated = userAccount.isActivated();
		if (oldActivated == activated) {
			return true;
		}
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET activated=? WHERE userId=?", new Object[] { activated, userId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteUserAccount(Connection conn, String userId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE userId=?", new Object[] { userId }, 1);
	}

}
