package org.orbit.service.program.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.service.program.IProgramExtension;
import org.orbit.service.program.IProgramExtensionFilter;
import org.orbit.service.program.IProgramExtensionService;
import org.orbit.service.program.IProgramLauncher;
import org.origin.common.adapter.AdaptorSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ProgramExtensionImpl implements IProgramExtension {

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * Register the program extension.
	 * 
	 * @param context
	 */
	public void register(BundleContext context) {
		// Register as ProgramExtesion service with "typeId" and "programExtensionId" properties.
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(IProgramExtensionService.PROP_EXTENSION_TYPE_ID, getTypeId());
		props.put(IProgramExtensionService.PROP_EXTENSION_ID, getId());
		this.serviceRegistration = context.registerService(IProgramExtension.class, this, props);
	}

	/**
	 * Unregister the program extension.
	 * 
	 * @param context
	 */
	public void unregister(BundleContext context) {
		// Unregister the ProgramExtesion service.
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	@Override
	public IProgramLauncher getLauncher() {
		return getAdapter(IProgramLauncher.class);
	}

	@Override
	public IProgramExtensionFilter getFilter() {
		return getAdapter(IProgramExtensionFilter.class);
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
