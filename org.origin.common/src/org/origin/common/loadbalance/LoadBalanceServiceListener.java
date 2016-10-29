package org.origin.common.loadbalance;

public interface LoadBalanceServiceListener<S> {

	/**
	 * 
	 * @param service
	 */
	void serviceAdded(LoadBalanceService<S> service);

	/**
	 * 
	 * @param service
	 */
	void serviceRemoved(LoadBalanceService<S> service);

}
