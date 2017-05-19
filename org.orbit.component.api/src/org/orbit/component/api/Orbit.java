package org.orbit.component.api;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier1.session.OAuth2;
import org.orbit.component.api.tier1.session.OAuth2Connector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;

public class Orbit {

	protected static Orbit INSTANCE = new Orbit();

	public static Orbit getInstance() {
		return INSTANCE;
	}

	public AppStore getAppStore() {
		AppStore appStore = null;
		AppStoreConnector appStoreConnector = Activator.getInstance().getAppStoreConnector();
		if (appStoreConnector != null) {
			appStore = appStoreConnector.getService();
		}
		return appStore;
	}

	public ConfigRegistry getConfigRegistry() {
		ConfigRegistry configRegistry = null;
		ConfigRegistryConnector configRegistryConnector = Activator.getInstance().getConfigRegistryConnector();
		if (configRegistryConnector != null) {
			configRegistry = configRegistryConnector.getService();
		}
		return configRegistry;
	}

	public UserRegistry getUserRegistry() {
		UserRegistry userRegistry = null;
		UserRegistryConnector userRegistryConnector = Activator.getInstance().getUserRegistryConnector();
		if (userRegistryConnector != null) {
			userRegistry = userRegistryConnector.getService();
		}
		return userRegistry;
	}

	public OAuth2 getOAuth2() {
		OAuth2 oauth2 = null;
		OAuth2Connector oauth2Connector = Activator.getInstance().getOAuth2Connector();
		if (oauth2Connector != null) {
			oauth2 = oauth2Connector.getService();
		}
		return oauth2;
	}

}
