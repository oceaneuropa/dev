package org.origin.common.loadbalance.listener;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.loadbalance.LoadBalanceResource;

public class LoadBalanceServiceListenerSupport<S> implements LoadBalanceServiceListenerProvider<S> {

	protected List<LoadBalanceServiceListener<S>> serviceListeners = new ArrayList<LoadBalanceServiceListener<S>>();

	@Override
	@SuppressWarnings("unchecked")
	public LoadBalanceServiceListener<S>[] getServiceListeners() {
		return this.serviceListeners.toArray(new LoadBalanceServiceListener[this.serviceListeners.size()]);
	}

	@Override
	public boolean addServiceListener(LoadBalanceServiceListener<S> listener) {
		if (listener != null && !this.serviceListeners.contains(listener)) {
			return this.serviceListeners.add(listener);
		}
		return false;
	}

	@Override
	public boolean removeServiceListener(LoadBalanceServiceListener<S> listener) {
		if (listener != null && this.serviceListeners.remove(listener)) {
			return this.serviceListeners.add(listener);
		}
		return false;
	}

	/**
	 * 
	 * @param service
	 */
	public void notifyServiceAdded(LoadBalanceResource<S> service) {
		for (LoadBalanceServiceListener<S> listener : this.serviceListeners) {
			listener.serviceAdded(service);
		}
	}

	/**
	 * 
	 * @param service
	 */
	public void notifyServiceRemoved(LoadBalanceResource<S> service) {
		for (LoadBalanceServiceListener<S> listener : this.serviceListeners) {
			listener.serviceRemoved(service);
		}
	}

}
