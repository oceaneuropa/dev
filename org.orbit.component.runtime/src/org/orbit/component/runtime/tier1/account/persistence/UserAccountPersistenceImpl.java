package org.orbit.component.runtime.tier1.account.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.orbit.component.model.tier1.account.rto.UserAccount;
import org.origin.common.jdbc.DatabaseUtil;

public class UserAccountPersistenceImpl implements UserAccountPersistence {

	protected Properties databaseProperties;
	protected UserAccountTableHandler userAccountTableHandler;

	public UserAccountPersistenceImpl(Properties databaseProperties) {
		this.databaseProperties = databaseProperties;
		this.userAccountTableHandler = UserAccountTableHandler.INSTANCE;

		init();
	}

	/**
	 * Initialize database tables.
	 */
	protected void init() {
		Connection conn = DatabaseUtil.getConnection(this.databaseProperties);
		try {
			DatabaseUtil.initialize(conn, this.userAccountTableHandler);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	public Connection getConnection() {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected void handleSQLException(SQLException e) throws IOException {
		throw new IOException(e);
	}

	@Override
	public List<UserAccount> getUserAccounts() throws IOException {
		List<UserAccount> userAccounts = null;
		Connection conn = getConnection();
		try {
			userAccounts = this.userAccountTableHandler.getUserAccounts(conn);
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
	public UserAccount getUserAccount(String userId) throws IOException {
		UserAccount userAccount = null;
		Connection conn = getConnection();
		try {
			return this.userAccountTableHandler.getUserAccount(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public UserAccount createUserAccount(String userId, String password, String email, String firstName, String lastName, String phone) throws IOException {
		UserAccount userAccount = null;
		Connection conn = getConnection();
		try {
			userAccount = this.userAccountTableHandler.createUserAccount(conn, userId, password, email, firstName, lastName, phone, null, null);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return userAccount;
	}

	@Override
	public boolean setUserAccountActivated(String userId, boolean activated) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.setUserAccountActivated(conn, userId, activated);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean userAccountExists(String userId) throws IOException {
		boolean exists = false;
		Connection conn = getConnection();
		try {
			exists = this.userAccountTableHandler.userAccountExists(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean updatePassword(String userId, String password) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.updatePassword(conn, userId, password, null);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateName(String userId, String firstName, String lastName) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.updateName(conn, userId, firstName, lastName, null);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateEmail(String userId, String email) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.updateEmail(conn, userId, email, null);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updatePhone(String userId, String phone) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.updatePhone(conn, userId, phone, null);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteUserAccount(String userId) throws IOException {
		boolean succeed = false;
		Connection conn = getConnection();
		try {
			succeed = this.userAccountTableHandler.deleteUserAccount(conn, userId);
		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

}
