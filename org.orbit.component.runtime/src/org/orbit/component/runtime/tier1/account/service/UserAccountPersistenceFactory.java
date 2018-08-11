package org.orbit.component.runtime.tier1.account.service;

import java.io.IOException;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;

public class UserAccountPersistenceFactory {

	protected static UserAccountPersistenceFactory INSTANCE = new UserAccountPersistenceFactory();

	public static UserAccountPersistenceFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	public UserAccountPersistence create(Properties properties) throws IOException {
		UserAccountPersistence persistence = null;
		if (properties != null) {
			if (properties.containsKey(DatabaseUtil.JDBC_DRIVER) //
					&& properties.containsKey(DatabaseUtil.JDBC_URL) //
					&& properties.containsKey(DatabaseUtil.JDBC_USERNAME) //
					&& properties.containsKey(DatabaseUtil.JDBC_PASSWORD) //
			) {
				persistence = new UserAccountPersistenceImpl(properties);
			}
		}
		return persistence;
	}

}
