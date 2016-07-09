package org.nb.home.client.api;

import org.nb.home.client.api.impl.HomeControlImpl;

public class HomeFactory {

	/**
	 * Create Home client.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static IHomeControl createHomeControl(String url, String username, String password) {
		return new HomeControlImpl(url, "home/v1", username, password);
	}

}
