package org.origin.common.loadbalance;

import java.util.List;

public class RoundRobinLoadBalancePolicy<S> extends AbstractLoadBalancePolicy<S> {

	protected int index = 0;

	public RoundRobinLoadBalancePolicy() {
	}

	@Override
	public LoadBalanceService<S> next() {
		LoadBalancer<S> lb = checkLoadBalancer();
		List<LoadBalanceService<S>> services = lb.getServices();
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
