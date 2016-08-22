package org.origin.common.rest.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPolicyFactoryRegistry {

	public static EditPolicyFactoryRegistry INSTANCE = new EditPolicyFactoryRegistry();

	protected Map<String, List<EditPolicyFactory>> factoryMap = new HashMap<String, List<EditPolicyFactory>>();
	protected static EditPolicyFactory[] EMPTY_FACTORIES = new EditPolicyFactory[0];

	private EditPolicyFactoryRegistry() {
	}

	/**
	 * Check whether the registry contains a factory for given namespace.
	 * 
	 * @param namespace
	 * @param factory
	 * @return
	 */
	public synchronized boolean contains(String namespace, EditPolicyFactory factory) {
		assert namespace != null : "namespace is null";
		assert namespace != null : "factory is null";

		List<EditPolicyFactory> factories = this.factoryMap.get(namespace);
		if (factories != null && factories.contains(factory)) {
			return true;
		}
		return false;
	}

	/**
	 * Register a factory under a namespace to the registry.
	 * 
	 * @param namespace
	 * @param factory
	 * @return
	 */
	public synchronized boolean register(String namespace, EditPolicyFactory factory) {
		assert namespace != null : "namespace is null";
		assert namespace != null : "factory is null";

		List<EditPolicyFactory> factories = this.factoryMap.get(namespace);
		if (factories == null) {
			factories = new ArrayList<EditPolicyFactory>();
			this.factoryMap.put(namespace, factories);
		}
		if (!factories.contains(factory)) {
			return factories.add(factory);
		}
		return false;
	}

	/**
	 * Unregister a factory under a namespace from the registry.
	 * 
	 * @param namespace
	 * @param factory
	 * @return
	 */
	public synchronized boolean unregister(String namespace, EditPolicyFactory factory) {
		assert namespace != null : "namespace is null";
		assert namespace != null : "factory is null";

		List<EditPolicyFactory> factories = this.factoryMap.get(namespace);
		if (factories != null && factories.contains(factory)) {
			return factories.remove(factory);
		}
		return false;
	}

	/**
	 * Get registered factories under a namespace from the registry.
	 * 
	 * @param namespace
	 * @return
	 */
	public synchronized EditPolicyFactory[] getFactories(String namespace) {
		assert namespace != null : "namespace is null";

		List<EditPolicyFactory> factories = this.factoryMap.get(namespace);
		if (factories == null || factories.isEmpty()) {
			return EMPTY_FACTORIES;
		}
		return factories.toArray(new EditPolicyFactory[factories.size()]);
	}

}
