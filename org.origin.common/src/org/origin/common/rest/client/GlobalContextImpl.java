package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Parameter;

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

	// protected ThreadLocal<String> currentRealm;
	// protected ThreadLocal<Map<String, UserInfo>> realmUserInfo;
	protected String currentRealm;
	protected Map<String, UserInfo> realmUserInfo;

	public GlobalContextImpl() {
		this.currentRealm = DEFAULT_REALM;
		this.realmUserInfo = new HashMap<String, UserInfo>();
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
		// return this.currentRealm.get();
		return this.currentRealm;
	}

	public String getCurrentUsername(String realm) {
		if (isRealmUnset(realm)) {
			realm = DEFAULT_REALM;
		}

		String currUsername = null;
		// Map<String, UserInfo> realmUserInfo = this.realmUserInfo.get();
		UserInfo currUserInfo = realmUserInfo.get(realm);
		if (currUserInfo != null) {
			currUsername = currUserInfo.getUsername();
		}
		if (currUsername == null) {
			currUsername = DEFAULT_USERNAME;
		}
		return currUsername;
	}

	public void setCurrentUser(String realm, String username, String password) {
		if (isRealmUnset(realm)) {
			realm = DEFAULT_REALM;
		}
		if (isUsernameUnset(username)) {
			username = DEFAULT_USERNAME;
		}

		// set current realm
		// this.currentRealm.set(realm);
		this.currentRealm = realm;

		// set current user
		// Map<String, UserInfo> realmUserInfo = this.realmUserInfo.get();

		UserInfo currentUserInfo = realmUserInfo.get(realm);
		if (currentUserInfo == null) {
			currentUserInfo = new UserInfo();
			realmUserInfo.put(realm, currentUserInfo);
		}
		currentUserInfo.setRealm(realm);
		currentUserInfo.setUsername(username);
		currentUserInfo.setPassword(password);

		// this.realmUserInfo.set(realmUserInfo);
	}

}
