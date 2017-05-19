package org.origin.common.loadbalance.policy;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;

/**
 * 
 * 
 * https://devcentral.f5.com/articles/intro-to-load-balancing-for-developers-ndash-the-algorithms
 * 
 * https://f5.com/resources/white-papers/load-balancing-101-nuts-and-bolts
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <S>
 */
public interface LoadBalancePolicy<S> {

	/**
	 * Initialize the load balance policy.
	 * 
	 * @param lb
	 */
	void init(LoadBalancer<S> lb);

	/**
	 * Get the load balancer.
	 * 
	 * @return
	 */
	LoadBalancer<S> getLoadBalancer();

	/**
	 * Get the next load balance service.
	 * 
	 * @return
	 */
	LoadBalanceResource<S> next();

	/**
	 * Dispose the load balance policy.
	 * 
	 * @param lb
	 */
	void dispose(LoadBalancer<S> lb);

}
