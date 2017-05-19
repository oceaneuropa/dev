package org.origin.common.loadbalance.listener;

import org.origin.common.loadbalance.LoadBalanceResource;

public interface LoadBalanceServiceListener<S> {

	/**
	 * 
	 * @param service
	 */
	void serviceAdded(LoadBalanceResource<S> service);

	/**
	 * 
	 * @param service
	 */
	void serviceRemoved(LoadBalanceResource<S> service);

}
