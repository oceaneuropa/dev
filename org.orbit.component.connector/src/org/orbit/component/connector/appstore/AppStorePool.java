package org.orbit.component.connector.appstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.api.appstore.AppStore;

public class AppStorePool {

	protected Properties properties;
	protected Map<String, AppStore> urlToAppStoreMap = new HashMap<String, AppStore>();

	/**
	 * 
	 * @param properties
	 */
	public AppStorePool(Properties properties) {
		this.properties = properties;
	}

	public void start() {

	}

	public void stop() {

	}

	/**
	 * Get all app stores in the pool.
	 * 
	 * @return
	 */
	public synchronized List<AppStore> getAppStores() {
		List<AppStore> appStores = new ArrayList<AppStore>();
		for (AppStore appStore : this.urlToAppStoreMap.values()) {
			appStores.add(appStore);
		}
		return appStores;
	}

	/**
	 * Get an app store from the pool with specified url.
	 * 
	 * @param url
	 * @return
	 */
	public synchronized AppStore getAppStore(String url) {
		AppStore appStore = null;
		if (url != null) {
			appStore = this.urlToAppStoreMap.get(url);
		}
		return appStore;
	}

	/**
	 * Add an app store to the pool.
	 * 
	 * @param url
	 * @param appStore
	 */
	public synchronized void addAppStore(String url, AppStore appStore) {
		assert (url != null) : "url is null";
		assert (appStore != null) : "appStore is null";

		this.urlToAppStoreMap.put(url, appStore);
	}

	/**
	 * Remove an app store from the pool.
	 * 
	 * @param url
	 */
	public synchronized AppStore removeAppStore(String url) {
		assert (url != null) : "url is null";

		return this.urlToAppStoreMap.remove(url);
	}

}
