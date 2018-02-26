package org.orbit.platform.sdk.extension.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.orbit.platform.sdk.extension.util.ProgramExtensionRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ProgramExtensionRegistryImpl extends ProgramExtensionRegistry {

	Map<IProgramExtension, ServiceRegistration<?>> programExtensionToServiceRegistrationMap = new HashMap<IProgramExtension, ServiceRegistration<?>>();

	@Override
	public void register(BundleContext context, IProgramExtension programExtension) {
		if (programExtension == null || this.programExtensionToServiceRegistrationMap.containsKey(programExtension)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(IProgramExtensionService.PROP_EXTENSION_TYPE_ID, programExtension.getTypeId());
		props.put(IProgramExtensionService.PROP_EXTENSION_ID, programExtension.getId());
		ServiceRegistration<?> serviceRegistration = context.registerService(IProgramExtension.class, programExtension, props);
		this.programExtensionToServiceRegistrationMap.put(programExtension, serviceRegistration);
	}

	@Override
	public void unregister(BundleContext context, IProgramExtension programExtension) {
		if (programExtension == null) {
			return;
		}
		ServiceRegistration<?> serviceRegistration = this.programExtensionToServiceRegistrationMap.remove(programExtension);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

}
