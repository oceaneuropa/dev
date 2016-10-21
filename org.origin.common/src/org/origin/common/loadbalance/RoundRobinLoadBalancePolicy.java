package org.origin.common.loadbalance;

public class RoundRobinLoadBalancePolicy<RES extends LoadBalanceableResource> extends AbstractLoadBalancePolicy<RES> {

	@Override
	public RES next() {
		return null;
	}

}
