package org.orbit.infra.api.util;

import org.orbit.infra.api.subscription.SubsServerAPI;

public class SubsServerUtil {

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public static SubsServerAPI getAPI(String accessToken) {
		String url = InfraConfigPropertiesHandler.getInstance().getSubsServerURL();
		return getAPI(url, accessToken);
	}

	/**
	 * 
	 * @param url
	 * @param accessToken
	 * @return
	 */
	public static SubsServerAPI getAPI(String url, String accessToken) {
		SubsServerAPI api = InfraClients.getInstance().getSubsServerAPI(url, accessToken);
		return api;
	}

	/**
	 * 
	 * @param api
	 * @return
	 */
	public static boolean isOnline(SubsServerAPI api) {
		api = checkAPI(api);
		boolean isOnline = false;
		if (api.ping()) {
			isOnline = true;
		}
		return isOnline;
	}

	/**
	 * 
	 * @param api
	 * @return
	 */
	protected static SubsServerAPI checkAPI(SubsServerAPI api) {
		if (api == null) {
			throw new IllegalArgumentException("SubsServerAPI is null.");
		}
		if (api.isProxy()) {
			throw new IllegalStateException("SubsServerAPI is proxy.");
		}
		return api;
	}

}
