package org.origin.common.loadbalance;

public interface LoadBalancedServiceConnector<SERVICE> {

	public LoadBalancer<SERVICE> getLoadBalancer();

	public SERVICE getService();

}
