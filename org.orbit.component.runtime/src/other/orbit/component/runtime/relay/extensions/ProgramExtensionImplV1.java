package other.orbit.component.runtime.relay.extensions;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.extensions.core.impl.ExtensionImpl;
import org.origin.common.extensions.util.InterfacesSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ProgramExtensionImplV1 extends ExtensionImpl implements IExtension {

	protected String name;
	protected String description;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected InterfacesSupport interfacesSupport = new InterfacesSupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected ServiceRegistration<?> serviceRegistration;

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
		props.put(IExtensionService.PROP_EXTENSION_TYPE_ID, extensionTypeId);
		props.put(IExtensionService.PROP_EXTENSION_ID, extensionId);
		this.serviceRegistration = context.registerService(IExtension.class, this, props);
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

	// @Override
	// public String getRealm() {
	// return null;
	// }

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
	public Object getProperty(Object propName) {
		return this.properties.get(propName);
	}

	@Override
	public <T> T getProperty(Object propName, Class<T> clazz) {
		return null;
	}

	// public void setFilter(IProgramExtensionFilter filter) {
	// this.filter = filter;
	// }
	//
	// @Override
	// public IProgramExtensionFilter getFilter() {
	// return this.filter;
	// }

	@Override
	public Object[] createExecutableInstances() {
		return this.interfacesSupport.createExecutableInstances();
	}

	@Override
	public <T> T createExecutableInstance(Class<T> clazz) {
		return this.interfacesSupport.createExecutableInstance(clazz);
	}

	@Override
	public InterfaceDescription[] getInterfaceDescriptions() {
		return this.interfacesSupport.getInterfaceDescriptions();
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		return this.interfacesSupport.getInterfaceDescription(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		return this.interfacesSupport.getInterfaceDescription(object);
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T interfaceInstance) {
		this.interfacesSupport.addInterface(clazz, interfaceInstance);
	}

	@Override
	public void addInterface(Class<?> clazz, String interfaceClassName) {
		this.interfacesSupport.addInterface(clazz, interfaceClassName);
	}

	@Override
	public void addInterface(Class<?> clazz, Class<?> interfaceImplClass) {
		this.interfacesSupport.addInterface(clazz, interfaceImplClass);
	}

	@Override
	public void addInterface(InterfaceDescription description) {
		this.interfacesSupport.addInterface(description);
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		this.interfacesSupport.removeInterface(classes);
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
		if (!(obj instanceof IExtension)) {
			return false;
		}
		IExtension other = (IExtension) obj;

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

// @Override
// public <T> void adapt(Class<T> clazz, T object) {
// this.adaptorSupport.adapt(clazz, object);
// }
//
// @Override
// public <T> void adapt(Class<T>[] classes, T object) {
// this.adaptorSupport.adapt(classes, object);
// }
//
// @Override
// public <T> T getAdapter(Class<T> adapter) {
// return this.adaptorSupport.getAdapter(adapter);
// }

// @Override
// public <T> void addInterface(Class<T> clazz, T object, String name) {
// this.interfacesSupport.addInterface(clazz, object, name);
// }

// @Override
// public <T> void addInterface(Class<T>[] classes, T object, String name) {
// this.interfacesSupport.addInterface(classes, object, name);
// }

// @Override
// public <T> void addInterface(Class<?>[] classes, T object) {
// this.interfacesSupport.addInterface(classes, object);
// }

// @Override
// public <T> void addInterface(Class<?>[] classes, T object, InterfaceDescription description) {
// this.interfacesSupport.addInterface(classes, object, description);
// }
