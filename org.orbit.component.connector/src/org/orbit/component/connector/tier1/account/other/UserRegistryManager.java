package org.orbit.component.connector.tier1.account.other;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.connector.tier1.account.UserRegistryImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * https://stackoverflow.com/questions/11832204/how-do-you-implement-a-managedservicefactory-in-osgi
 *
 */
public class UserRegistryManager implements ManagedServiceFactory {

	public static final String MANAGED_SERVICE_FACTORY_PID = "component.userregistry.manager"; //$NON-NLS-1$

	protected String name;
	protected Map<String, UserRegistry> pidToUserRegistryMap = new HashMap<String, UserRegistry>();
	protected Map<String, ServiceRegistration<?>> pidToUserRegistryRegistrationMap = new HashMap<String, ServiceRegistration<?>>();
	protected BundleContext bundleContext;
	protected ServiceRegistration<?> factoryRegistration;

	public UserRegistryManager() {
		this(MANAGED_SERVICE_FACTORY_PID);
	}

	/**
	 * 
	 * @param name
	 */
	public UserRegistryManager(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// register this UserRegistryManager as a ManagedServiceFactory service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, MANAGED_SERVICE_FACTORY_PID); // "service.pid" property is "component.userregistry.manager"
		this.factoryRegistration = bundleContext.registerService(ManagedServiceFactory.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// unregister this UserRegistryManager as a ManagedServiceFactory service
		if (factoryRegistration != null) {
			this.factoryRegistration.unregister();
			this.factoryRegistration = null;
		}

		// unregister all UserRegistry services
		for (Iterator<String> pidItor = this.pidToUserRegistryRegistrationMap.keySet().iterator(); pidItor.hasNext();) {
			String pid = pidItor.next();
			unregisterUserRegistry(pid);
		}
		this.pidToUserRegistryRegistrationMap.clear();
		this.pidToUserRegistryMap.clear();

		this.bundleContext = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updated(String pid, Dictionary<String, ?> dictionary) throws ConfigurationException {
		UserRegistry userRegistry = this.pidToUserRegistryMap.get(pid);
		if (userRegistry == null) {
			// Create new UserRegistry instance
			userRegistry = new UserRegistryImpl(null, (Map<String, Object>) dictionary);
			this.pidToUserRegistryMap.put(pid, userRegistry);

			registerUserRegistry(pid, userRegistry, dictionary);

		} else {
			// Update existing UserRegistry instance
			userRegistry.update((Map<String, Object>) dictionary);
		}
	}

	@Override
	public void deleted(String pid) {
		unregisterUserRegistry(pid);
		this.pidToUserRegistryRegistrationMap.remove(pid);
		this.pidToUserRegistryMap.remove(pid);
	}

	/**
	 * Register a UserRegistry service.
	 * 
	 * @param pid
	 * @param userRegistry
	 * @param dictionary
	 */
	protected void registerUserRegistry(String pid, UserRegistry userRegistry, Dictionary<String, ?> dictionary) {
		ServiceRegistration<?> registration = this.bundleContext.registerService(UserRegistry.class.getName(), userRegistry, dictionary);
		this.pidToUserRegistryRegistrationMap.put(pid, registration);
	}

	/**
	 * Unregister a UserRegistry service.
	 * 
	 * @param pid
	 */
	protected void unregisterUserRegistry(String pid) {
		ServiceRegistration<?> registration = this.pidToUserRegistryRegistrationMap.get(pid);
		if (registration != null) {
			registration.unregister();
		}

		UserRegistry userRegistry = this.pidToUserRegistryMap.get(pid);
		if (userRegistry != null) {
			// dispose the userRegistry, if it can be disposed
		}
	}

}
