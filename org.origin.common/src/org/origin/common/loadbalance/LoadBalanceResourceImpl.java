package org.origin.common.loadbalance;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.origin.common.adapter.AdaptorSupport;

public class LoadBalanceResourceImpl<SERVICE> implements LoadBalanceResource<SERVICE> {

	protected String id;
	protected SERVICE service;
	protected Map<String, Object> properties = new Hashtable<String, Object>();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param service
	 */
	public LoadBalanceResourceImpl(SERVICE service) {
		this(service, UUID.randomUUID().toString());
	}

	/**
	 * 
	 * @param service
	 * @param id
	 */
	public LoadBalanceResourceImpl(SERVICE service, String id) {
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
	public LoadBalanceResourceImpl(SERVICE service, Map<String, Object> properties) {
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
	public void setService(SERVICE service) {
		this.service = service;
	}

	@Override
	public SERVICE getService() {
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
	public synchronized Object removeProperty(String key) {
		return this.properties.remove(key);
	}

	@Override
	public synchronized Object getProperty(String key) {
		return this.properties.get(key);
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
