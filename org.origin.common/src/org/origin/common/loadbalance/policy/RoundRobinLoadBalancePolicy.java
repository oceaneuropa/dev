package org.origin.common.loadbalance.policy;

import java.util.List;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;

public class RoundRobinLoadBalancePolicy<S> extends AbstractLoadBalancePolicy<S> {

	protected int index = 0;

	public RoundRobinLoadBalancePolicy() {
	}

	@Override
	public synchronized LoadBalanceResource<S> next() {
		LoadBalancer<S> lb = checkLoadBalancer();
		List<LoadBalanceResource<S>> resources = lb.getResources();
		if (resources == null || resources.isEmpty()) {
			return null;
		}
		if (resources.size() == 1) {
			return resources.get(0);
		}

		if (this.index >= resources.size()) {
			this.index = 0;
		}

		int beginIndex = this.index;
		int currIndex = beginIndex;

		LoadBalanceResource<S> resource = resources.get(currIndex);
		boolean isHeartBeatExpired = ResourcePropertyHelper.INSTANCE.isHeartBeatExpired(resource);
		while (isHeartBeatExpired) {
			resource = null;

			currIndex++;
			if (currIndex >= resources.size()) {
				currIndex = 0;
			}
			if (currIndex == beginIndex) {
				// already encountered the expired resource
				break;

			} else {
				resource = resources.get(currIndex);
				isHeartBeatExpired = ResourcePropertyHelper.INSTANCE.isHeartBeatExpired(resource);
			}
		}

		this.index = currIndex + 1;
		if (this.index >= resources.size()) {
			this.index = 0;
		}

		return resource;
	}

}
