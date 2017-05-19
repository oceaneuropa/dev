package org.origin.common.loadbalance;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class LoadBalanceResourceImpl<S> implements LoadBalanceResource<S> {

	protected String id;
	protected S service;
	protected Map<String, Object> properties = new Hashtable<String, Object>();

	/**
	 * 
	 * @param service
	 */
	public LoadBalanceResourceImpl(S service) {
		this(service, UUID.randomUUID().toString());
	}

	/**
	 * 
	 * @param service
	 * @param id
	 */
	public LoadBalanceResourceImpl(S service, String id) {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		this.id = id;
		this.service = service;
	}

	/**
	 * 
	 * @param service
	 * @param properties
	 */
	public LoadBalanceResourceImpl(S service, Map<String, Object> properties) {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}
		this.id = UUID.randomUUID().toString();
		this.service = service;
		this.properties = properties;
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
		return this.properties;
	}

	@Override
	public synchronized boolean hasProperty(String key) {
		return this.properties.containsKey(key);
	}

	@Override
	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public synchronized void setProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	@Override
	public synchronized Object getProperty(String key) {
		return this.properties.get(key);
	}

}
