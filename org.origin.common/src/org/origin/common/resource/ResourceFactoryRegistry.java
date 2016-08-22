package org.origin.common.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.origin.common.runtime.ActivationAware;

public class ResourceFactoryRegistry {

	public static ResourceFactoryRegistry INSTANCE = new ResourceFactoryRegistry();

	private Map<String, ResourceFactory<?>> factoriesMap = new LinkedHashMap<String, ResourceFactory<?>>();

	private ResourceFactoryRegistry() {
	}

	/**
	 * Register a factory in the registry.
	 * 
	 * @param name
	 * @param factory
	 */
	public void register(String name, ResourceFactory<?> factory) {
		checkName(name);
		checkFactory(factory);

		unregister(name);

		if (factory instanceof ActivationAware) {
			((ActivationAware) factory).activate();
		}

		this.factoriesMap.put(name, factory);
	}

	/**
	 * Unregister a factory from the registry.
	 * 
	 * @param name
	 */
	public void unregister(String name) {
		checkName(name);

		ResourceFactory<?> factory = this.factoriesMap.get(name);
		if (factory != null) {
			this.factoriesMap.remove(name);

			if (factory instanceof ActivationAware) {
				((ActivationAware) factory).deactivate();
			}
		}
	}

	/**
	 * Get cache factory.
	 * 
	 * @param name
	 * @return
	 */
	public ResourceFactory<?> getFactory(String name) {
		checkName(name);
		return this.factoriesMap.get(name);
	}

	/**
	 * Get cache factories.
	 * 
	 * @return
	 */
	public ResourceFactory<?>[] getFactories() {
		return this.factoriesMap.values().toArray(new ResourceFactory[factoriesMap.size()]);
	}

	/**
	 * 
	 * @param name
	 */
	protected void checkName(String name) {
		assert (name != null) : "name is null.";
	}

	/**
	 * 
	 * @param factory
	 */
	protected void checkFactory(ResourceFactory<?> factory) {
		assert (factory != null) : "factory is null.";
	}

}
