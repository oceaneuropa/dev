package org.orbit.component.connector.appstore;

import java.util.List;

import org.orbit.component.api.appstore.AppStore;
import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.loadbalance.RoundRobinLoadBalancePolicy;

public class AppStoreLoadBalancer extends LoadBalancer<AppStore> {

	/**
	 * 
	 * @param services
	 */
	public AppStoreLoadBalancer(List<LoadBalanceService<AppStore>> services) {
		super(services);
		setPolicy(new RoundRobinLoadBalancePolicy<AppStore>());
	}

}
