package org.orbit.service.program.impl;

import java.util.Hashtable;

import org.orbit.service.program.ProgramProvider;
import org.orbit.service.program.ProgramService;
import org.origin.common.adapter.AdaptorSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ProgramProviderImpl implements ProgramProvider {

	protected ServiceRegistration<?> serviceRegistration;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * Start the service provider.
	 * 
	 * @param context
	 */
	public void start(BundleContext context) {
		// Register this provider instance as a ProgramProvider service with "programTypeId" and "programProviderId" properties.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(ProgramService.PROP_PROGRAM_TYPE_ID, getTypeId());
		props.put(ProgramService.PROP_PROGRAM_PROVIDER_ID, getId());
		this.serviceRegistration = context.registerService(ProgramProvider.class, this, props);
	}

	/**
	 * Stop the service provider.
	 * 
	 * @param context
	 */
	public void stop(BundleContext context) {
		// Unregister this provider instance as a ProgramProvider service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
