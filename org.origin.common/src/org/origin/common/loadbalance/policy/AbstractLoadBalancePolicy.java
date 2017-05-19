package org.origin.common.loadbalance.policy;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.loadbalance.listener.LoadBalanceServiceListener;

public abstract class AbstractLoadBalancePolicy<S> implements LoadBalancePolicy<S>, LoadBalanceServiceListener<S> {

	protected LoadBalancer<S> lb;

	public AbstractLoadBalancePolicy() {
	}

	protected LoadBalancer<S> checkLoadBalancer() {
		if (this.lb == null) {
			throw new IllegalStateException("LoadBalancer is not set.");
		}
		return this.lb;
	}

	/** LoadBalancePolicy */
	@Override
	public void init(LoadBalancer<S> lb) {
		if (lb == null) {
			throw new IllegalArgumentException("LoadBalancer is null.");
		}
		if (this.lb != null) {
			dispose(this.lb);
		}

		this.lb = lb;
		this.lb.addServiceListener(this);
	}

	@Override
	public LoadBalancer<S> getLoadBalancer() {
		return this.lb;
	}

	/**
	 * Get the next load balance service.
	 * 
	 * @return
	 */
	@Override
	public abstract LoadBalanceResource<S> next();

	@Override
	public void dispose(LoadBalancer<S> lb) {
		if (this.lb == lb) {
			this.lb.removeServiceListener(this);
		}
		this.lb = null;
	}

	/** LoadBalanceServiceListener */
	@Override
	public void serviceAdded(LoadBalanceResource<S> service) {

	}

	@Override
	public void serviceRemoved(LoadBalanceResource<S> service) {

	}

}
