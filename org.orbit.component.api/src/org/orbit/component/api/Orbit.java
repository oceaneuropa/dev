package org.orbit.component.api;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.api.tier1.session.OAuth2;
import org.orbit.component.api.tier1.session.OAuth2Connector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainMgmt;
import org.orbit.component.api.tier3.domain.DomainMgmtConnector;

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

	public OAuth2 getOAuth2() {
		OAuth2 oauth2 = null;
		OAuth2Connector connector = Activator.getInstance().getOAuth2Connector();
		if (connector != null) {
			oauth2 = connector.getService();
		}
		return oauth2;
	}

	public AppStore getAppStore() {
		AppStore appStore = null;
		AppStoreConnector connector = Activator.getInstance().getAppStoreConnector();
		if (connector != null) {
			appStore = connector.getService();
		}
		return appStore;
	}

	public DomainMgmt getDomainMamt() {
		DomainMgmt domainMgmt = null;
		DomainMgmtConnector connector = Activator.getInstance().getDomainMgmtConnector();
		if (connector != null) {
			domainMgmt = connector.getService();
		}
		return domainMgmt;
	}

}
