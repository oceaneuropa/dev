package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ServiceConnector<SERVICE> {

	public static final String CONNECTOR_SERVICE_ID = "connector.service.id";
	protected static final String SEPARATOR = "::";

	protected Class<SERVICE> serviceClass;
	protected String serviceId;
	protected Map<String, SERVICE> services;
	protected ServiceRegistration<?> serviceRegistration;

	// /**
	// *
	// * @param serviceId
	// */
	// public ServiceConnector(String serviceId) {
	// this.serviceId = serviceId;
	// this.serviceMap = new HashMap<String, SERVICE>();
	// }

	/**
	 * 
	 * @param serviceClass
	 */
	public ServiceConnector(Class<SERVICE> serviceClass) {
		if (serviceClass == null) {
			throw new IllegalArgumentException("serviceClass is null.");
		}
		this.serviceClass = serviceClass;
		this.serviceId = serviceClass.getName();
		this.services = new HashMap<String, SERVICE>();
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		String serviceId = getServiceId();
		if (serviceId != null) {
			props.put(CONNECTOR_SERVICE_ID, serviceId);
		}
		this.serviceRegistration = bundleContext.registerService(ServiceConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
		this.services.clear();
	}

	protected void checkProperties(Map<String, Object> properties) {
		String fullUrl = (String) properties.get(ClientConfiguration.URL);
		if (fullUrl == null || fullUrl.isEmpty()) {
			throw new IllegalArgumentException("'" + ClientConfiguration.URL + "' property is not found.");
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	protected String getServiceKey(Map<String, Object> properties) {
		String realm = (String) properties.get(ClientConfiguration.REALM);
		String username = (String) properties.get(ClientConfiguration.USERNAME);
		String url = (String) properties.get(ClientConfiguration.URL);

		if (realm == null) {
			realm = ClientConfiguration.DEFAULT_REALM;
		}
		if (username == null) {
			username = ClientConfiguration.UNKNOWN_USERNAME;
		}
		if (url == null) {
			url = "n/a";
		}

		String key = realm + SEPARATOR + username + SEPARATOR + url;
		return key;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public synchronized SERVICE getService(Map<String, Object> properties) {
		checkProperties(properties);

		String key = getServiceKey(properties);

		SERVICE service = this.services.get(key);
		if (service == null) {
			service = create(properties);
			this.services.put(key, service);
		}

		return service;
	}

	protected abstract SERVICE create(Map<String, Object> properties);

	/**
	 * 
	 * @param service
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean update(SERVICE service, Map<String, Object> properties) throws ClientException {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
		checkProperties(properties);

		if (this.services.containsValue(service)) {
			for (Iterator<String> itor = this.services.keySet().iterator(); itor.hasNext();) {
				String currKey = itor.next();
				SERVICE currClient = this.services.get(currKey);

				if (service.equals(currClient)) {
					String newKey = getServiceKey(properties);

					if (!newKey.equals(currKey)) {
						this.services.remove(currKey);
						this.services.put(newKey, currClient);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param service
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean close(SERVICE service) throws ClientException {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}

		if (this.services.containsValue(service)) {
			String keyToRemove = null;
			for (Iterator<String> itor = this.services.keySet().iterator(); itor.hasNext();) {
				String currKey = itor.next();
				SERVICE currAgent = this.services.get(currKey);

				if (service.equals(currAgent)) {
					keyToRemove = currKey;
					break;
				}
			}
			if (keyToRemove != null) {
				this.services.remove(keyToRemove);
				return true;
			}
		}
		return false;
	}

}
