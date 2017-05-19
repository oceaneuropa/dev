package org.origin.mgm.client.connector;

import org.origin.common.loadbalance.LoadBalancer;

public interface ServiceConnector<S> {

	public LoadBalancer<S> getLoadBalancer();

	public S getService();

}
