package org.orbit.component.runtime.tier1.account.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.tier1.account.service.UserAccountPersistence;
import org.origin.common.jdbc.DatabaseUtil;

public class UserAccountPersistenceImpl implements UserAccountPersistence {

	protected Properties databaseProperties;
	protected UserAccountTableHandler userAccountTableHandler;

	public UserAccountPersistenceImpl(Properties databaseProperties) throws IOException {
		this.databaseProperties = databaseProperties;
		this.userAccountTableHandler = UserAccountTableHandler.INSTANCE;

		init();
	}

	/**
	 * Initialize database tables.
	 */
	protected void init() throws IOException {
		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);
			DatabaseUtil.initialize(conn, this.userAccountTableHandler);

		} catch (Exception e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected void handleSQLException(Exception e) throws IOException {
		throw new IOException(e);
	}

	@Override
	public List<UserAccount> getUserAccounts() throws IOException {
		List<UserAccount> userAccounts = null;
		Connection conn = null;
		try {
			conn = getConnection();
			userAccounts = this.userAccountTableHandler.getList(conn);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (userAccounts == null) {
			userAccounts = new ArrayList<UserAccount>();
		}
		return userAccounts;
	}

	@Override
	public boolean accountIdExists(String accountId) throws IOException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.userAccountTableHandler.accountIdExists(conn, accountId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean usernameExists(String username) throws IOException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.userAccountTableHandler.usernameExists(conn, username);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean emailExists(String email) throws IOException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.userAccountTableHandler.emailExists(conn, email);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public UserAccount getUserAccountByAccountId(String accountId) throws IOException {
		UserAccount userAccount = null;
		Connection conn = null;
		try {
			conn = getConnection();
			return this.userAccountTableHandler.getByAccountId(conn, accountId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public UserAccount getUserAccountByUsername(String username) throws IOException {
		UserAccount userAccount = null;
		Connection conn = null;
		try {
			conn = getConnection();
			return this.userAccountTableHandler.getByUsername(conn, username);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public UserAccount getUserAccountByEmail(String email) throws IOException {
		UserAccount userAccount = null;
		Connection conn = null;
		try {
			conn = getConnection();
			return this.userAccountTableHandler.getByEmail(conn, email);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public UserAccount createUserAccount(String accountId, String username, String password, String email, String firstName, String lastName, String phone) throws IOException {
		UserAccount userAccount = null;
		Connection conn = null;
		try {
			conn = getConnection();

			userAccount = this.userAccountTableHandler.create(conn, accountId, username, password, email, firstName, lastName, phone, null, null);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public boolean setActivated(String accountId, boolean activated) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.setActivated(conn, accountId, activated);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updatePassword(String accountId, String password) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.updatePassword(conn, accountId, password, null);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateName(String accountId, String firstName, String lastName) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.updateName(conn, accountId, firstName, lastName, null);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateEmail(String accountId, String email) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.updateEmail(conn, accountId, email, null);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updatePhone(String accountId, String phone) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.updatePhone(conn, accountId, phone, null);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteUserAccount(String accountId) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.userAccountTableHandler.delete(conn, accountId);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

}
