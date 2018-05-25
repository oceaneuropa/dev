package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Parameter;
import org.origin.common.Activator;

public class GlobalContextImpl extends GlobalContext {

	public class UserInfo {
		private String realm;
		private String username;
		private String password;

		public UserInfo() {
		}

		public UserInfo(String realm, String username, String password) {
			this.realm = realm;
			this.username = username;
			this.password = password;
		}

		String getRealm() {
			return this.realm;
		}

		void setRealm(String realm) {
			this.realm = realm;
		}

		String getUsername() {
			return username;
		}

		void setUsername(String username) {
			this.username = username;
		}

		String getPassword() {
			return password;
		}

		void setPassword(String password) {
			this.password = password;
		}
	}

	protected String currentRealm;
	protected Map<String, UserInfo> realmUserInfo;

	public GlobalContextImpl() {
		this.currentRealm = getDefaultRealm();
		this.realmUserInfo = new HashMap<String, UserInfo>();
	}

	protected String getDefaultRealm() {
		String defaultRealm = null;
		if (Activator.getDefault() != null) {
			defaultRealm = Activator.getDefault().getRealm();
		}
		if (defaultRealm == null || defaultRealm.isEmpty()) {
			defaultRealm = DEFAULT_REALM;
		}
		return defaultRealm;
	}

	protected String getDefaultUsername() {
		String defaultUsername = null;
		if (defaultUsername == null || defaultUsername.isEmpty()) {
			defaultUsername = DEFAULT_USERNAME;
		}
		return defaultUsername;
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

	public String getCurrentRealm() {
		return this.currentRealm;
	}

	public String getCurrentUsername(String realm) {
		if (isRealmUnset(realm)) {
			realm = getDefaultRealm();
		}

		String currUsername = null;
		UserInfo currUserInfo = realmUserInfo.get(realm);
		if (currUserInfo != null) {
			currUsername = currUserInfo.getUsername();
		}
		if (currUsername == null || currUsername.isEmpty()) {
			currUsername = getDefaultUsername();
		}
		return currUsername;
	}

	public void setCurrentUser(String currentRealm, String username, String password) {
		if (isRealmUnset(currentRealm)) {
			currentRealm = getDefaultRealm();
		}
		if (isUsernameUnset(username)) {
			username = getDefaultUsername();
		}

		// set current realm
		this.currentRealm = currentRealm;

		// set current user
		UserInfo userInfo = realmUserInfo.get(currentRealm);
		if (userInfo == null) {
			userInfo = new UserInfo();
			realmUserInfo.put(currentRealm, userInfo);
		}
		userInfo.setRealm(currentRealm);
		userInfo.setUsername(username);
		userInfo.setPassword(password);
	}

}
