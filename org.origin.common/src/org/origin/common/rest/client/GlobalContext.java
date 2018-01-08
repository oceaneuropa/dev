package org.origin.common.rest.client;

import org.apache.felix.service.command.Parameter;

public abstract class GlobalContext {

	protected static String DEFAULT_REALM = ClientConfiguration.DEFAULT_REALM;
	protected static String DEFAULT_USERNAME = ClientConfiguration.UNKNOWN_USERNAME;

	protected static Object lock = new Object[0];
	protected static GlobalContext instance = null;

	public static GlobalContext getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new GlobalContextImpl();
				}
			}
		}
		return instance;
	}

	public boolean isRealmUnset(String realm) {
		if (realm == null || realm.isEmpty() || Parameter.UNSPECIFIED.equals(realm)) {
			return true;
		}
		return false;
	}

	public boolean isUsernameUnset(String username) {
		if (username == null || username.isEmpty() || Parameter.UNSPECIFIED.equals(username)) {
			return true;
		}
		return false;
	}

	public abstract String checkRealm(String realm);

	public abstract String checkUsername(String realm, String username);

	public abstract void setCurrentUser(String realm, String username, String password);

}
