package org.origin.common.loadbalance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.common.loadbalance.listener.LoadBalanceServiceListener;
import org.origin.common.loadbalance.listener.LoadBalanceServiceListenerProvider;
import org.origin.common.loadbalance.listener.LoadBalanceServiceListenerSupport;
import org.origin.common.loadbalance.policy.LoadBalancePolicy;

/**
 * https://devcentral.f5.com/articles/intro-to-load-balancing-for-developers-ndash-the-algorithms
 * 
 * https://f5.com/resources/white-papers/load-balancing-101-nuts-and-bolts
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * @param <S>
 *
 */
public class LoadBalancer<S> implements LoadBalanceServiceListenerProvider<S> {

	protected List<LoadBalanceResource<S>> resources;
	protected LoadBalancePolicy<S> policy;
	protected LoadBalanceServiceListenerSupport<S> listenerSupport = new LoadBalanceServiceListenerSupport<S>();

	public LoadBalancer() {
		this.resources = new ArrayList<LoadBalanceResource<S>>();
	}

	/**
	 * 
	 * @param resources
	 */
	public LoadBalancer(List<LoadBalanceResource<S>> resources) {
		this.resources = resources;
		if (this.resources == null) {
			this.resources = new ArrayList<LoadBalanceResource<S>>();
		}
	}

	public List<LoadBalanceResource<S>> getResources() {
		return this.resources;
	}

	/**
	 * Get a resource.
	 * 
	 * @param resourceId
	 * @return
	 */
	public LoadBalanceResource<S> getResource(String resourceId) {
		if (resourceId == null) {
			throw new IllegalArgumentException("resourceId is null");
		}
		LoadBalanceResource<S> result = null;
		for (Iterator<LoadBalanceResource<S>> itor = this.resources.iterator(); itor.hasNext();) {
			LoadBalanceResource<S> currResource = itor.next();
			if (resourceId.equals(currResource.getId())) {
				result = currResource;
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (this.resources.isEmpty()) ? true : false;
	}

	/**
	 * Add a resource to the LoadBalancer
	 * 
	 * @param resource
	 * @return
	 */
	public synchronized boolean addResource(LoadBalanceResource<S> resource) {
		boolean succeed = false;
		if (resource != null) {
			// remove existing lb service with same id, if found.
			LoadBalanceResource<S> existingResource = getResource(resource.getId());
			if (existingResource != null) {
				this.resources.remove(existingResource);
			}

			// add the new lb service to the list
			if (!this.resources.contains(resource)) {
				succeed = this.resources.add(resource);
			}
		}
		return succeed;
	}

	/**
	 * Remove a resource from the LoadBalancer.
	 * 
	 * @param resource
	 * @return
	 */
	public synchronized boolean removeResource(LoadBalanceResource<S> resource) {
		boolean succeed = false;
		if (resource != null) {
			String serviceId = resource.getId();

			for (Iterator<LoadBalanceResource<S>> itor = this.resources.iterator(); itor.hasNext();) {
				LoadBalanceResource<S> currService = itor.next();

				// remove lb service of same instance or same id
				if (currService == resource || currService.getId().equals(serviceId)) {
					itor.remove();
					succeed = true;
				}
			}
		}
		return succeed;
	}

	/**
	 * Set a load balance policy to the load balancer.
	 * 
	 * @param policy
	 */
	public void setPolicy(LoadBalancePolicy<S> policy) {
		if (this.policy == policy) {
			return;
		}

		LoadBalancePolicy<S> oldPolicy = this.policy;
		// initialize new policy
		if (policy != null) {
			policy.init(this);
		}

		this.policy = policy;

		// dispose old policy
		if (oldPolicy != null) {
			oldPolicy.dispose(this);
		}
	}

	/**
	 * Get the load balance policy of the load balancer.
	 * 
	 * @return
	 */
	public LoadBalancePolicy<S> getPolicy() {
		return policy;
	}

	/**
	 * Return the next service.
	 * 
	 * @return
	 */
	public synchronized LoadBalanceResource<S> getNext() {
		if (this.policy == null) {
			throw new IllegalStateException("Load balance policy is not set.");
		}
		if (this.resources.isEmpty()) {
			return null;
		}
		if (this.resources.size() == 1) {
			return this.resources.get(0);
		}
		return this.policy.next();
	}

	/** LoadBalanceServiceListener support */
	@Override
	public LoadBalanceServiceListener<S>[] getServiceListeners() {
		return this.listenerSupport.getServiceListeners();
	}

	@Override
	public boolean addServiceListener(LoadBalanceServiceListener<S> listener) {
		return this.listenerSupport.addServiceListener(listener);
	}

	@Override
	public boolean removeServiceListener(LoadBalanceServiceListener<S> listener) {
		return this.listenerSupport.removeServiceListener(listener);
	}

	protected void notifyServiceAdded(LoadBalanceResource<S> service) {
		this.listenerSupport.notifyServiceAdded(service);
	}

	protected void notifyServiceRemoved(LoadBalanceResource<S> service) {
		this.listenerSupport.notifyServiceRemoved(service);
	}

}
