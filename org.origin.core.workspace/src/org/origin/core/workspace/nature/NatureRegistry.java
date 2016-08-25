package org.origin.core.workspace.nature;

import java.util.LinkedHashMap;
import java.util.Map;

public class NatureRegistry {

	public static NatureRegistry INSTANCE = new NatureRegistry();

	protected Map<String, ResourceNatureProvider<?, ?>> natureProviderMap = new LinkedHashMap<String, ResourceNatureProvider<?, ?>>();

	/**
	 * Register a nature provider.
	 * 
	 * @param natureId
	 * @param provider
	 */
	public void register(String natureId, ResourceNatureProvider<?, ?> provider) {
		this.natureProviderMap.put(natureId, provider);
	}

	/**
	 * Unregister a nature provider.
	 * 
	 * @param natureId
	 */
	public void unregister(String natureId) {
		this.natureProviderMap.remove(natureId);
	}

	/**
	 * Check whether the nature provider for a nature id is available.
	 * 
	 * @param natureId
	 * @return
	 */
	public boolean hasProvider(String natureId) {
		return this.natureProviderMap.get(natureId) != null ? true : false;
	}

	/**
	 * Get nature provider.
	 * 
	 * @param natureId
	 * @return
	 */
	public ResourceNatureProvider<?, ?> getProvider(String natureId) {
		return this.natureProviderMap.get(natureId);
	}

	/**
	 * Get the nature provider by nature id and create new instance of the nature.
	 * 
	 * @param natureId
	 * @return
	 */
	public ResourceNature<?> createNature(String natureId) {
		ResourceNature<?> newNature = null;
		ResourceNatureProvider<?, ?> provider = this.natureProviderMap.get(natureId);
		if (provider != null) {
			newNature = provider.newInstance();
		}
		return newNature;
	}

	/**
	 * Get the nature provider by nature id and create new instance of the nature.
	 * 
	 * @param natureId
	 * @param natureClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <NATURE extends ResourceNature<?>> NATURE createNature(String natureId, Class<NATURE> natureClass) {
		NATURE result = null;
		ResourceNatureProvider<?, ?> provider = this.natureProviderMap.get(natureId);
		if (provider != null) {
			ResourceNature<?> newNature = provider.newInstance();
			if (newNature != null && natureClass.isAssignableFrom(newNature.getClass())) {
				result = (NATURE) newNature;
			}
		}
		return result;
	}

}
