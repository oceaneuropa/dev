package org.origin.common.loadbalance.policy;

import java.util.List;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;

public class RoundRobinLoadBalancePolicy<S> extends AbstractLoadBalancePolicy<S> {

	protected int index = 0;

	public RoundRobinLoadBalancePolicy() {
	}

	@Override
	public LoadBalanceResource<S> next() {
		LoadBalancer<S> lb = checkLoadBalancer();
		List<LoadBalanceResource<S>> services = lb.getResources();
		if (services == null || services.isEmpty()) {
			return null;
		}
		if (services.size() == 1) {
			return services.get(0);
		}

		if (this.index >= services.size()) {
			this.index = 0;
		}
		return services.get(index++);
	}

}
