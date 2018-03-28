package org.origin.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class ServiceRegistryImpl<S> extends ServiceRegistry<S> {

	protected Class<S> serviceClass;

	protected ServiceTracker<?, ?> serviceTracker;
	protected List<S> services = new ArrayList<S>();

	protected Map<S, ServiceRegistration<?>> serviceRegistrationMap = new HashMap<S, ServiceRegistration<?>>();

	public ServiceRegistryImpl(Class<S> serviceClass) {
		this.serviceClass = serviceClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(final BundleContext bundleContext) {
		// Start tracking WebServiceAware services
		this.serviceTracker = new ServiceTracker(bundleContext, this.serviceClass, new ServiceTrackerCustomizer() {
			@Override
			public Object addingService(ServiceReference reference) {
				Object service = bundleContext.getService(reference);
				if (service != null && serviceClass.isAssignableFrom(service.getClass())) {
					serviceAdded((S) service);
					return service;
				}
				return null;
			}

			@Override
			public void modifiedService(ServiceReference reference, Object service) {
				if (service != null && serviceClass.isAssignableFrom(service.getClass())) {
					serviceModified((S) service);
				}
			}

			@Override
			public void removedService(ServiceReference reference, Object service) {
				if (service != null && serviceClass.isAssignableFrom(service.getClass())) {
					serviceRemoved((S) service);
				}
			}
		});
		this.serviceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceTracker != null) {
			this.serviceTracker.close();
			this.serviceTracker = null;
		}
	}

	protected void serviceAdded(S service) {
		this.services.add(service);
	}

	protected void serviceModified(S service) {
	}

	protected void serviceRemoved(S service) {
		this.services.remove(service);
	}

	@Override
	public S[] getServices() {
		return (S[]) this.services.toArray(new Object[this.services.size()]);
	}

	@Override
	public void register(BundleContext bundleContext, S service) {
		if (service == null || this.serviceRegistrationMap.containsKey(service)) {
			return;
		}

		Hashtable<String, Object> properties = null;
		if (service instanceof PropertiesAware) {
			properties = ((PropertiesAware) service).getProperties();
		}
		if (properties == null) {
			properties = new Hashtable<String, Object>();
		}

		ServiceRegistration<?> serviceRegistration = bundleContext.registerService(this.serviceClass, service, properties);
		this.serviceRegistrationMap.put(service, serviceRegistration);
	}

	@Override
	public void unregister(BundleContext bundleContext, S service) {
		if (service == null) {
			return;
		}
		ServiceRegistration<?> serviceRegistration = this.serviceRegistrationMap.remove(service);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

}
