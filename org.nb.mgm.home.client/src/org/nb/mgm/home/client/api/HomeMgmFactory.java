package org.nb.mgm.home.client.api;

import org.nb.mgm.home.client.api.impl.HomeManagementImpl;

public class HomeMgmFactory {

	/**
	 * Create Management client.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static HomeManagement createHomeManagement(String url, String username, String password) {
		return new HomeManagementImpl(url, "home/v1", username, password);
	}

}
