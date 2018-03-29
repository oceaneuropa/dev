package org.origin.common.extensions.core.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionRegistry;
import org.origin.common.extensions.core.IExtensionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ExtensionRegistryImpl extends IExtensionRegistry {

	protected Map<IExtension, ServiceRegistration<?>> extensionToServiceRegistrationMap = new HashMap<IExtension, ServiceRegistration<?>>();

	@Override
	public void register(BundleContext context, IExtension extension) {
		if (extension == null || this.extensionToServiceRegistrationMap.containsKey(extension)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(IExtensionService.PROP_REALM, extension.getRealm());
		props.put(IExtensionService.PROP_EXTENSION_TYPE_ID, extension.getTypeId());
		props.put(IExtensionService.PROP_EXTENSION_ID, extension.getId());
		ServiceRegistration<?> serviceRegistration = context.registerService(IExtension.class, extension, props);
		this.extensionToServiceRegistrationMap.put(extension, serviceRegistration);
	}

	@Override
	public void unregister(BundleContext context, IExtension extension) {
		if (extension == null) {
			return;
		}
		ServiceRegistration<?> serviceRegistration = this.extensionToServiceRegistrationMap.remove(extension);
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

}
