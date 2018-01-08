package org.origin.common.rest.client.other;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Parameter;
import org.origin.common.Activator;
import org.origin.common.rest.client.GlobalContext;

public class GlobalContextImplV1 extends GlobalContext {

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

	public GlobalContextImplV1() {
		this.currentRealm = getDefaultRealm();
		this.realmUserInfo = new HashMap<String, UserInfo>();
	}

	protected String getDefaultRealm() {
		String defaultRealm = Activator.getDefault().getRealm();
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
			realm = getDefaultRealm();
		}

		String currUsername = null;
		// Map<String, UserInfo> realmUserInfo = this.realmUserInfo.get();
		UserInfo currUserInfo = realmUserInfo.get(realm);
		if (currUserInfo != null) {
			currUsername = currUserInfo.getUsername();
		}
		if (currUsername == null || currUsername.isEmpty()) {
			currUsername = getDefaultUsername();
		}
		return currUsername;
	}

	public void setCurrentUser(String realm, String username, String password) {
		if (isRealmUnset(realm)) {
			realm = getDefaultRealm();
		}
		if (isUsernameUnset(username)) {
			username = getDefaultUsername();
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

	@Override
	public String checkRealm(String realm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkUsername(String realm, String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
