package org.origin.common.loadbalance;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.origin.common.thread.ThreadPoolTimer;

public abstract class AbstractLoadBalanceService<S> implements LoadBalanceService<S> {

	protected String id;
	protected S service;
	protected Map<String, Object> properties;

	protected ThreadPoolTimer loadBalanceServiceMonitor;
	protected long monitoringInterval = -1;

	/**
	 * 
	 * @param service
	 */
	public AbstractLoadBalanceService(S service) {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
		this.id = UUID.randomUUID().toString();
		this.service = service;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setService(S service) {
		this.service = service;
	}

	@Override
	public S getService() {
		return this.service;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

	@Override
	public synchronized boolean hasProperty(String key) {
		return getProperties().containsKey(key);
	}

	@Override
	public synchronized void setProperty(String key, Object value) {
		getProperties().put(key, value);
	}

	@Override
	public synchronized Object getProperty(String key) {
		return getProperties().get(key);
	}

	public long getMonitoringInterval() {
		return this.monitoringInterval;
	}

	public void setMonitoringInterval(long monitorInterval) {
		this.monitoringInterval = monitorInterval;
	}

	/**
	 * Start monitoring the status of the service.
	 */
	@Override
	public void start() {
		if (this.loadBalanceServiceMonitor != null) {
			this.loadBalanceServiceMonitor.stop();
		}

		this.loadBalanceServiceMonitor = new ThreadPoolTimer("LoadBalanceService(" + (getService().getClass().getSimpleName()) + ") Monitor", new Runnable() {
			@Override
			public void run() {
				S service = getService();
				if (service == null) {
					throw new RuntimeException("service is null");
				}
				monitor(service);
			}
		});
		if (this.monitoringInterval > 0) {
			this.loadBalanceServiceMonitor.setInterval(this.monitoringInterval);
		}
		this.loadBalanceServiceMonitor.start();
	}

	/**
	 * Stop monitoring the status of the service.
	 */
	@Override
	public void stop() {
		if (this.loadBalanceServiceMonitor != null) {
			this.loadBalanceServiceMonitor.stop();
			this.loadBalanceServiceMonitor = null;
		}
	}

	/**
	 * Monitor the service.
	 * 
	 * @param service
	 */
	protected void monitor(S service) {
	}

}
