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

	public String checkRealm(String realm) {
		if (isRealmUnset(realm)) {
			realm = getCurrentRealm();
		}
		return realm;
	}

	public String checkUsername(String realm, String username) {
		if (isRealmUnset(realm)) {
			realm = getCurrentRealm();
		}
		if (isUsernameUnset(username)) {
			username = getCurrentUsername(realm);
		}
		return username;
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

	public abstract String getCurrentRealm();

	public abstract String getCurrentUsername(String realm);

	public abstract void setCurrentUser(String realm, String username, String password);

}
