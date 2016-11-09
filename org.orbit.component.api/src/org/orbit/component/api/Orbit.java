package org.orbit.component.api;

import org.orbit.component.api.appstore.AppStore;
import org.orbit.component.api.appstore.AppStoreManager;
import org.orbit.component.api.configregistry.ConfigRegistry;
import org.orbit.component.api.configregistry.ConfigRegistryManager;

public class Orbit {

	protected static Orbit INSTANCE = new Orbit();

	public static Orbit getInstance() {
		return INSTANCE;
	}

	public AppStore getAppStore() {
		AppStore appStore = null;
		AppStoreManager appStoreManager = Activator.getInstance().getAppStoreManager();
		if (appStoreManager != null) {
			appStore = appStoreManager.getAppStore();
		}
		return appStore;
	}

	public ConfigRegistry getConfigRegistry() {
		ConfigRegistry configRegistry = null;
		ConfigRegistryManager configRegistryManager = Activator.getInstance().getConfigRegistryManager();
		if (configRegistryManager != null) {
			configRegistry = configRegistryManager.getConfigRegistryService();
		}
		return configRegistry;
	}

}
