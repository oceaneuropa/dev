package org.origin.common.loadbalance;

import java.util.Comparator;
import java.util.Date;
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

	// behaves like Controls with LayoutData (represented as properties in a LB service)
	protected List<LoadBalanceService<S>> services;
	// behaves like Layout
	protected LoadBalancePolicy<S> policy;

	protected LoadBalanceServiceListenerSupport<S> serviceListenerSupport = new LoadBalanceServiceListenerSupport<S>();

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

	/**
	 * Get services.
	 * 
	 * @return
	 */
	public List<LoadBalanceService<S>> getServices() {
		return services;
	}

	/**
	 * Add a service to the LoadBalancer
	 * 
	 * @param service
	 * @return
	 */
	public boolean addService(LoadBalanceService<S> service) {
		if (service != null && !this.services.contains(service)) {
			boolean succeed = this.services.add(service);
			if (succeed) {
				notifyServiceAdded(service);
			}
			return succeed;
		}
		return false;
	}

	/**
	 * Remove a service from the LoadBalancer.
	 * 
	 * @param service
	 * @return
	 */
	public boolean removeService(LoadBalanceService<S> service) {
		if (service != null && this.services.contains(service)) {
			boolean succeed = this.services.remove(service);
			if (succeed) {
				notifyServiceRemoved(service);
			}
			return succeed;
		}
		return false;
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
	 * Start monitoring on each LoadBalanceService.
	 * 
	 * Weights are based on continuous monitoring of the servers and are therefore continually changing
	 * 
	 */
	public void start() {
		// Each load balance service to start monitoring its referenced remote service.
		for (LoadBalanceService<S> service : this.services) {
			service.start();
		}
	}

	/**
	 * Stop monitoring on each LoadBalanceService.
	 * 
	 */
	public void stop() {
		// Each load balance service to stop monitoring its referenced remote service.
		for (LoadBalanceService<S> service : this.services) {
			service.stop();
		}
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
			System.out.println(getClass().getSimpleName() + ".getNext() services is empty.");
			return null;
		}
		if (this.services.size() == 1) {
			return this.services.get(0);
		}

		int size = this.services.size();

		return this.policy.next();
	}

	/** LoadBalanceServiceListener support */
	@Override
	public LoadBalanceServiceListener<S>[] getServiceListeners() {
		return this.serviceListenerSupport.getServiceListeners();
	}

	@Override
	public boolean addServiceListener(LoadBalanceServiceListener<S> listener) {
		return this.serviceListenerSupport.addServiceListener(listener);
	}

	@Override
	public boolean removeServiceListener(LoadBalanceServiceListener<S> listener) {
		return this.serviceListenerSupport.removeServiceListener(listener);
	}

	protected void notifyServiceAdded(LoadBalanceService<S> service) {
		this.serviceListenerSupport.notifyServiceAdded(service);
	}

	protected void notifyServiceRemoved(LoadBalanceService<S> service) {
		this.serviceListenerSupport.notifyServiceRemoved(service);
	}

	public static LoadBalanceServicePingComparator PING_COMPARATOR = new LoadBalanceServicePingComparator();

	public static class LoadBalanceServicePingComparator implements Comparator<LoadBalanceService<?>> {
		@Override
		public int compare(LoadBalanceService<?> lbService1, LoadBalanceService<?> lbService2) {
			Date pingDate1 = (Date) (lbService1.hasProperty("last_ping_time") ? lbService1.getProperty("last_ping_time") : null);
			Date pingDate2 = (Date) (lbService2.hasProperty("last_ping_time") ? lbService1.getProperty("last_ping_time") : null);
			if (pingDate1 == null && pingDate2 == null) {
				return 0;
			}
			if (pingDate1 != null && pingDate2 == null) {
				return -1;
			}
			if (pingDate1 == null && pingDate2 != null) {
				return 1;
			}
			return pingDate1.compareTo(pingDate2);
		}
	}

}

/// **
// *
// * @param timeout
// * @return
// */
// public S getService(int timeout) {
// S service = getNext();
// if (timeout <= 0) {
// return service;
// }
// long totalWaitingTime = 0;
// while (service == null) {
// try {
// Thread.sleep(500);
// } catch (InterruptedException e) {
// e.printStackTrace();
// }
// totalWaitingTime += 500;
// if (totalWaitingTime > timeout) {
// break;
// }
// service = getNext();
// }
// return service;
// }
