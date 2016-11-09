package org.orbit.component.connector.appstore;

import org.orbit.component.api.appstore.AppStore;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.loadbalance.IndexItemAwareLoadBalanceService;

public class AppStoreLoadBalanceService extends IndexItemAwareLoadBalanceService<AppStore> {

	/**
	 * 
	 * @param id
	 * @param appStore
	 */
	public AppStoreLoadBalanceService(AppStore appStore, IndexItem indexItem) {
		super(appStore, indexItem);
	}

}
