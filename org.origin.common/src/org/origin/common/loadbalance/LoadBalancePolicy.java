package org.origin.common.loadbalance;

import java.util.List;

/**
 * https://devcentral.f5.com/articles/intro-to-load-balancing-for-developers-ndash-the-algorithms
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <RES>
 */
public interface LoadBalancePolicy<RES extends LoadBalanceableResource> {

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public boolean contains(RES resource);

	/**
	 * 
	 * @return
	 */
	public List<RES> getResources();

	/**
	 * 
	 * @param resources
	 */
	public void setResources(List<RES> resources);

	/**
	 * Add a resource.
	 * 
	 * @param resource
	 */
	public void addResource(RES resource);

	/**
	 * Remove a resource.
	 * 
	 * @param resource
	 */
	public void removeResource(RES resource);

	/**
	 * Get the next load balance resource.
	 * 
	 * @return
	 */
	public RES next();

}
