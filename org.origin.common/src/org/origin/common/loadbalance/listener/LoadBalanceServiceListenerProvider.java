package org.origin.common.loadbalance.listener;

public interface LoadBalanceServiceListenerProvider<S> {

	LoadBalanceServiceListener<S>[] getServiceListeners();

	boolean addServiceListener(LoadBalanceServiceListener<S> listener);

	boolean removeServiceListener(LoadBalanceServiceListener<S> listener);

}
