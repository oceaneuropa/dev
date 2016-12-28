package org.orbit.component.connector.appstore;

import java.util.Map;

import org.orbit.component.api.appstore.AppStore;
import org.origin.common.loadbalance.AbstractLoadBalanceService;

public class AppStoreLoadBalanceService extends AbstractLoadBalanceService<AppStore> {

	/**
	 * 
	 * @param appStore
	 * @param properties
	 */
	public AppStoreLoadBalanceService(AppStore appStore, Map<String, Object> properties) {
		super(appStore, properties);
	}

}
