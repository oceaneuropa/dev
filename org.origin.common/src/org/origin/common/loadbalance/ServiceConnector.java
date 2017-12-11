package org.origin.common.loadbalance;

public interface ServiceConnector<S> {

	public LoadBalancer<S> getLoadBalancer();

	public S getService();

}
