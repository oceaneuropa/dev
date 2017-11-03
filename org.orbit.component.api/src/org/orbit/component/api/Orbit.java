package org.orbit.component.api;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;

public class Orbit {

	protected static Orbit INSTANCE = new Orbit();

	public static Orbit getInstance() {
		return INSTANCE;
	}

	public UserRegistry getUserRegistry() {
		UserRegistry userRegistry = null;
		UserRegistryConnector connector = Activator.getInstance().getUserRegistryConnector();
		if (connector != null) {
			userRegistry = connector.getService();
		}
		return userRegistry;
	}

	public ConfigRegistry getConfigRegistry() {
		ConfigRegistry configRegistry = null;
		ConfigRegistryConnector connector = Activator.getInstance().getConfigRegistryConnector();
		if (connector != null) {
			configRegistry = connector.getService();
		}
		return configRegistry;
	}

	public Auth getAuth() {
		Auth auth = null;
		AuthConnector connector = Activator.getInstance().getAuthConnector();
		if (connector != null) {
			auth = connector.getService();
		}
		return auth;
	}

	public AppStore getAppStore() {
		AppStore appStore = null;
		AppStoreConnector connector = Activator.getInstance().getAppStoreConnector();
		if (connector != null) {
			appStore = connector.getService();
		}
		return appStore;
	}

	public DomainManagement getDomainMamt() {
		DomainManagement domainMgmt = null;
		DomainManagementConnector connector = Activator.getInstance().getDomainMgmtConnector();
		if (connector != null) {
			domainMgmt = connector.getService();
		}
		return domainMgmt;
	}

}

// public OAuth2 getOAuth2() {
// OAuth2 oauth2 = null;
// OAuth2Connector connector = Activator.getInstance().getOAuth2Connector();
// if (connector != null) {
// oauth2 = connector.getService();
// }
// return oauth2;
// }
