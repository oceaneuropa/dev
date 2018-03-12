package org.orbit.platform.sdk.extension.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.adapter.AdaptorSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ProgramExtensionImpl implements IProgramExtension {

	protected String name;
	protected String description;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistration;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * Register the program extension.
	 * 
	 * @param context
	 */
	public void register(BundleContext context) {
		// Register an IProgramExtension service with "typeId" and "programExtensionId" properties.
		String extensionTypeId = getTypeId();
		String extensionId = getId();

		extensionTypeId = checkTypeId(extensionTypeId);
		extensionId = checkId(extensionId);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(IProgramExtensionService.PROP_EXTENSION_TYPE_ID, extensionTypeId);
		props.put(IProgramExtensionService.PROP_EXTENSION_ID, extensionId);
		this.serviceRegistration = context.registerService(IProgramExtension.class, this, props);
	}

	protected String checkTypeId(String typeId) {
		if (typeId == null) {
			throw new IllegalArgumentException("extension type Id is null");
		}
		return typeId;
	}

	protected String checkId(String id) {
		if (id == null) {
			throw new IllegalArgumentException("extension Id is null");
		}
		return id;
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
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		String typeId = getTypeId();
		String id = getId();

		typeId = checkTypeId(typeId);
		id = checkId(id);

		result = prime * result + typeId.hashCode();
		result = prime * result + id.hashCode();

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof IProgramExtension)) {
			return false;
		}
		IProgramExtension other = (IProgramExtension) obj;

		String typeId = getTypeId();
		String id = getId();
		if (typeId == null) {
			throw new IllegalArgumentException("extension type Id is null");
		}
		if (id == null) {
			throw new IllegalArgumentException("extension Id is null");
		}

		String otherTypeId = other.getTypeId();
		String otherId = other.getId();

		if (typeId.equals(otherTypeId) && id.equals(otherId)) {
			return true;
		}
		return false;
	}

}

// @Override
// public IProgramLauncher getLauncher() {
// return getAdapter(IProgramLauncher.class);
// }

// @Override
// public IProgramExtensionFilter getFilter() {
// return getAdapter(IProgramExtensionFilter.class);
// }
