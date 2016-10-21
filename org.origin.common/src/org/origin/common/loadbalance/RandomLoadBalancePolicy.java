package org.origin.common.loadbalance;

public class RandomLoadBalancePolicy<RES extends LoadBalanceableResource> extends AbstractLoadBalancePolicy<RES> {

	@Override
	public RES next() {
		return null;
	}

}
