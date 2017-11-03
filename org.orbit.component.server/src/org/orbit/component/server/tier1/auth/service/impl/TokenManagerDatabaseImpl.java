package org.orbit.component.server.tier1.auth.service.impl;

import java.sql.Connection;
import java.util.Properties;

import org.orbit.component.server.tier1.auth.service.TokenManager;
import org.orbit.component.server.tier1.auth.service.UserToken;
import org.origin.common.jdbc.DatabaseUtil;

public class TokenManagerDatabaseImpl implements TokenManager {

	protected Properties databaseProperties;

	/**
	 * 
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 */
	public TokenManagerDatabaseImpl(String driver, String url, String username, String password) {
		this.databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);
	}

	protected Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public UserToken[] getUserTokens() {
		return null;
	}

	@Override
	public UserToken getUserToken(String username) {
		return null;
	}

	@Override
	public void setUserToken(String username, UserToken userToken) {

	}

}
