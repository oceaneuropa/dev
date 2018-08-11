package org.orbit.component.runtime.common.token;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;

public class TokenManagerDatabaseImpl implements TokenManager {

	protected Properties databaseProperties;
	protected UserTokenTableHandler tableHandler;

	/**
	 * 
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 */
	public TokenManagerDatabaseImpl(String driver, String url, String username, String password) {
		this(DatabaseUtil.getProperties(driver, url, username, password));
	}

	/**
	 * 
	 * @param databaseProperties
	 */
	public TokenManagerDatabaseImpl(Properties databaseProperties) {
		this.databaseProperties = databaseProperties;
	}

	@Override
	public void activate() {
		this.tableHandler = UserTokenTableHandler.INSTANCE;
	}

	@Override
	public void deactivate() {
		this.tableHandler = null;
	}

	protected Connection getConnection() throws SQLException {
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		if (conn == null) {
			throw new RuntimeException("JDBC Connection is not available.");
		}
		return conn;
	}

	protected void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void checkTableHandler() {
		if (this.tableHandler == null) {
			throw new IllegalStateException("UserTokenTableHandler is not available.");
		}
	}

	@Override
	public UserToken[] getUserTokens() {
		checkTableHandler();

		List<UserToken> userTokens = null;
		Connection conn = null;
		try {
			conn = getConnection();
			userTokens = this.tableHandler.getLists(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn);
		}

		return userTokens.toArray(new UserToken[userTokens.size()]);
	}

	@Override
	public UserToken getUserToken(String username) {
		checkTableHandler();
		return null;
	}

	@Override
	public void setUserToken(String username, UserToken userToken) {
		checkTableHandler();
	}

}
