package org.origin.common.loadbalance;

import java.util.Iterator;
import java.util.List;

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

	protected List<LoadBalanceService<S>> services;
	protected LoadBalancePolicy<S> policy;

	protected LoadBalanceServiceListenerSupport<S> listenerSupport = new LoadBalanceServiceListenerSupport<S>();

	/**
	 * 
	 * @param services
	 */
	public LoadBalancer(List<LoadBalanceService<S>> services) {
		if (services == null) {
			throw new IllegalArgumentException("services is null");
		}
		this.services = services;
	}

	public List<LoadBalanceService<S>> getServices() {
		return services;
	}

	/**
	 * Get service by id.
	 * 
	 * @param serviceId
	 * @return
	 */
	public LoadBalanceService<S> getService(String serviceId) {
		if (serviceId == null) {
			throw new IllegalArgumentException("serviceId is null");
		}
		LoadBalanceService<S> result = null;
		for (Iterator<LoadBalanceService<S>> itor = this.services.iterator(); itor.hasNext();) {
			LoadBalanceService<S> currService = itor.next();
			if (serviceId.equals(currService.getId())) {
				result = currService;
				break;
			}
		}
		return result;
	}

	/**
	 * Add a service to the LoadBalancer
	 * 
	 * @param service
	 * @return
	 */
	public synchronized boolean addService(LoadBalanceService<S> service) {
		boolean succeed = false;
		if (service != null) {
			// remove existing lb service with same id, if found.
			LoadBalanceService<S> existingService = getService(service.getId());
			if (existingService != null) {
				this.services.remove(existingService);
			}

			// add the new lb service to the list
			if (!this.services.contains(service)) {
				succeed = this.services.add(service);
			}
		}
		return succeed;
	}

	/**
	 * Remove a service from the LoadBalancer.
	 * 
	 * @param service
	 * @return
	 */
	public synchronized boolean removeService(LoadBalanceService<S> service) {
		boolean succeed = false;
		if (service != null) {
			String serviceId = service.getId();

			for (Iterator<LoadBalanceService<S>> itor = this.services.iterator(); itor.hasNext();) {
				LoadBalanceService<S> currService = itor.next();

				// remove lb service of same instance or same id
				if (currService == service || currService.getId().equals(serviceId)) {
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
	public synchronized LoadBalanceService<S> getNext() {
		if (this.policy == null) {
			throw new IllegalStateException("Load balance policy is not set.");
		}
		if (this.services.isEmpty()) {
			return null;
		}
		if (this.services.size() == 1) {
			return this.services.get(0);
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

	protected void notifyServiceAdded(LoadBalanceService<S> service) {
		this.listenerSupport.notifyServiceAdded(service);
	}

	protected void notifyServiceRemoved(LoadBalanceService<S> service) {
		this.listenerSupport.notifyServiceRemoved(service);
	}

}
