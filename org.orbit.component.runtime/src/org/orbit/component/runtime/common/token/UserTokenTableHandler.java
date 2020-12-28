package org.orbit.component.runtime.common.token;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableProvider;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;
import org.origin.common.util.DateUtil;

public class UserTokenTableHandler implements DatabaseTableProvider {

	public static UserTokenTableHandler INSTANCE = new UserTokenTableHandler();

	@Override
	public String getTableName() {
		return "UserToken";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableProvider.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    accountId varchar(50) NOT NULL,");
			sb.append("    username varchar(500) NOT NULL,");
			sb.append("    accessToken varchar(500),");
			sb.append("    refreshToken varchar(500),");
			sb.append("	   accessTokenExpireTime varchar(50) DEFAULT NULL,");
			sb.append("	   refreshTokenExpireTime varchar(50) DEFAULT NULL,");
			sb.append("    PRIMARY KEY (accountId)");
			sb.append(");");

		} else if (DatabaseTableProvider.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    accountId varchar(50) NOT NULL,");
			sb.append("    username varchar(500) NOT NULL,");
			sb.append("    accessToken varchar(500),");
			sb.append("    refreshToken varchar(500),");
			sb.append("	   accessTokenExpireTime varchar(50) DEFAULT NULL,");
			sb.append("	   refreshTokenExpireTime varchar(50) DEFAULT NULL,");
			sb.append("    PRIMARY KEY (accountId)");
			sb.append(");");
		}

		return sb.toString();
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	/**
	 * Convert a ResultSet record to a UserToken object.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected static UserToken toObject(ResultSet rs) throws SQLException {
		String accountId = rs.getString("accountId");
		String username = rs.getString("username");
		String accessToken = rs.getString("accessToken");
		String refreshToken = rs.getString("refreshToken");
		String accessTokenExpireTimeString = rs.getString("accessTokenExpireTime");
		String refreshTokenExpireTimeString = rs.getString("refreshTokenExpireTime");
		Date accessTokenExpireTime = accessTokenExpireTimeString != null ? DateUtil.toDate(accessTokenExpireTimeString, DateUtil.getCommonDateFormats()) : null;
		Date refreshTokenExpireTime = refreshTokenExpireTimeString != null ? DateUtil.toDate(refreshTokenExpireTimeString, DateUtil.getCommonDateFormats()) : null;

		UserToken userToken = new UserToken();
		userToken.setAccountId(accountId);
		userToken.setUsername(username);
		userToken.setAccessToken(accessToken);
		userToken.setRefreshToken(refreshToken);
		userToken.setAccessTokenExpireTime(accessTokenExpireTime);
		userToken.setRefreshTokenExpireTime(refreshTokenExpireTime);

		return userToken;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<UserToken> getLists(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY accessTokenExpireTime ASC";
		ResultSetListHandler<UserToken> handler = new ResultSetListHandler<UserToken>() {
			@Override
			protected UserToken handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] {}, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public UserToken get(Connection conn, String username) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE username=?";
		ResultSetSingleHandler<UserToken> handler = new ResultSetSingleHandler<UserToken>() {
			@Override
			protected UserToken handleRow(ResultSet rs) throws SQLException {
				return toObject(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { username }, handler);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String username) throws SQLException {
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
	 * @param username
	 * @param accessToken
	 * @param refreshToken
	 * @param accessTokenExpireTime
	 * @param refreshTokenExpireTime
	 * @return
	 * @throws SQLException
	 */
	public UserToken add(Connection conn, String username, String accessToken, String refreshToken, Date accessTokenExpireTime, Date refreshTokenExpireTime) throws SQLException {
		String accessTokenExpireTimeString = DateUtil.toString(accessTokenExpireTime, getDateFormat());
		String refreshTokenExpireTimeString = DateUtil.toString(refreshTokenExpireTime, getDateFormat());

		String insertSQL = "INSERT INTO " + getTableName() + " (username, accessToken, refreshToken, accessTokenExpireTime, refreshTokenExpireTime) VALUES (?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { username, accessToken, refreshToken, accessTokenExpireTimeString, refreshTokenExpireTimeString }, 1);
		if (succeed) {
			return get(conn, username);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @param accessToken
	 * @param refreshToken
	 * @param accessTokenExpireTime
	 * @param refreshTokenExpireTime
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, String username, String accessToken, String refreshToken, Date accessTokenExpireTime, Date refreshTokenExpireTime) throws SQLException {
		String accessTokenExpireTimeString = DateUtil.toString(accessTokenExpireTime, getDateFormat());
		String refreshTokenExpireTimeString = DateUtil.toString(refreshTokenExpireTime, getDateFormat());

		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET accessToken=?, refreshToken=?, accessTokenExpireTime=?, refreshTokenExpireTime=? WHERE username=?", new Object[] { accessToken, refreshToken, accessTokenExpireTimeString, refreshTokenExpireTimeString, username }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @param accessToken
	 * @param refreshToken
	 * @param accessTokenExpireTime
	 * @param refreshTokenExpireTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateAccessToken(Connection conn, String username, String accessToken, Date accessTokenExpireTime) throws SQLException {
		String accessTokenExpireTimeString = DateUtil.toString(accessTokenExpireTime, getDateFormat());

		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET accessToken=?, accessTokenExpireTime=? WHERE username=?", new Object[] { accessToken, accessTokenExpireTimeString, username }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param v
	 * @param refreshToken
	 * @param refreshTokenExpireTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateRefreshToken(Connection conn, String v, String refreshToken, Date refreshTokenExpireTime) throws SQLException {
		String refreshTokenExpireTimeString = DateUtil.toString(refreshTokenExpireTime, getDateFormat());

		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET refreshToken=?, refreshTokenExpireTime=? WHERE username=?", new Object[] { refreshToken, refreshTokenExpireTimeString, v }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String username) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE username=?", new Object[] { username }, 1);
	}

}
