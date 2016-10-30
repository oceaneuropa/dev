package org.orbit.component.api.appstore;

import java.util.Iterator;

import org.origin.common.loadbalance.LoadBalancePolicy;

public interface AppStoreManager {

	/**
	 * Get one app store.
	 * 
	 * @return
	 */
	public AppStore getAppStore();

	/**
	 * Get all app stores.
	 * 
	 * @return
	 */
	public Iterator<AppStore> getAppStores();

	/**
	 * Get one app store.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public AppStore getAppStore(String url, String username, String password);

	/**
	 * 
	 * @param loadBalancePolicy
	 */
	public void setLoadBalancePolicy(LoadBalancePolicy<AppStore> loadBalancePolicy);

	/**
	 * 
	 * @return
	 */
	public LoadBalancePolicy<AppStore> getLoadBalancePolicy();

}
