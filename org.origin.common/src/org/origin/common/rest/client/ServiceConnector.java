package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ServiceConnector<SERVICE> {

	public static final String ORBIT_URL = "orbit.url";
	public static final String ORBIT_TOKEN = "orbit.token";
	public static final String CONNECTOR_SERVICE_ID = "connector.service.id";
	protected static final String SEPARATOR = "::";

	protected Class<SERVICE> serviceClass;
	protected String serviceId;
	protected Map<String, SERVICE> serviceMap;
	protected ServiceRegistration<?> serviceRegistration;

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
		this.serviceMap = new HashMap<String, SERVICE>();
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
		this.serviceMap.clear();
	}

	protected void checkProperties(Map<Object, Object> properties) throws ClientException {
		String fullUrl = (String) properties.get(ORBIT_URL);
		if (fullUrl == null || fullUrl.isEmpty()) {
			throw new RuntimeException("'" + ORBIT_URL + "' property is not set.");
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	protected String getServiceKey(Map<Object, Object> properties) {
		String fullUrl = (String) properties.get(ORBIT_URL);
		String token = (String) properties.get(ORBIT_TOKEN);
		if (fullUrl == null) {
			fullUrl = "n/a";
		}
		if (token == null) {
			token = "n/a";
		}
		String key = fullUrl + SEPARATOR + token;
		return key;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public synchronized SERVICE getService(Map<Object, Object> properties) throws ClientException {
		checkProperties(properties);

		String key = getServiceKey(properties);

		SERVICE service = this.serviceMap.get(key);
		if (service == null) {
			service = create(properties);
			this.serviceMap.put(key, service);
		}

		return service;
	}

	protected abstract SERVICE create(Map<Object, Object> properties);

	/**
	 * 
	 * @param service
	 * @param properties
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean update(SERVICE service, Map<Object, Object> properties) throws ClientException {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
		checkProperties(properties);

		if (this.serviceMap.containsValue(service)) {
			for (Iterator<String> itor = this.serviceMap.keySet().iterator(); itor.hasNext();) {
				String currKey = itor.next();
				SERVICE currClient = this.serviceMap.get(currKey);

				if (service.equals(currClient)) {
					String newKey = getServiceKey(properties);

					if (!newKey.equals(currKey)) {
						this.serviceMap.remove(currKey);
						this.serviceMap.put(newKey, currClient);
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

		if (this.serviceMap.containsValue(service)) {
			String keyToRemove = null;
			for (Iterator<String> itor = this.serviceMap.keySet().iterator(); itor.hasNext();) {
				String currKey = itor.next();
				SERVICE currAgent = this.serviceMap.get(currKey);

				if (service.equals(currAgent)) {
					keyToRemove = currKey;
					break;
				}
			}
			if (keyToRemove != null) {
				this.serviceMap.remove(keyToRemove);
				return true;
			}
		}
		return false;
	}

}

// /**
// *
// * @param serviceId
// */
// public ServiceConnector(String serviceId) {
// this.serviceId = serviceId;
// this.serviceMap = new HashMap<String, SERVICE>();
// }
