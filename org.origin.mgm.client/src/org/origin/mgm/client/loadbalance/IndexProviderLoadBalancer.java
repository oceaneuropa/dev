package org.origin.mgm.client.loadbalance;

import java.util.List;

import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.mgm.client.api.IndexProvider;

public class IndexProviderLoadBalancer extends LoadBalancer<IndexProvider> {

	protected ThreadPoolTimer indexProvidersMonitor;
	protected long monitoringInterval = -1;

	public IndexProviderLoadBalancer(List<LoadBalanceService<IndexProvider>> services) {
		super(services);
	}

}
