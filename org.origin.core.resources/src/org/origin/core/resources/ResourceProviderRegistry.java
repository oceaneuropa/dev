package org.origin.core.resources;

import java.util.ArrayList;
import java.util.List;

public class ResourceProviderRegistry {

	private static ResourceProviderRegistry INSTANCE = new ResourceProviderRegistry();

	public static ResourceProviderRegistry getInstance() {
		return INSTANCE;
	}

	protected List<ResourceProvider> providers = new ArrayList<ResourceProvider>();
	protected ResourceProvider[] providersArray = new ResourceProvider[0];

	/**
	 * 
	 * @param provider
	 */
	public synchronized boolean register(ResourceProvider provider) {
		if (provider != null && !this.providers.contains(provider)) {
			boolean succeed = this.providers.add(provider);
			update();
			return succeed;
		}
		return false;
	}

	/**
	 * 
	 * @param provider
	 */
	public synchronized boolean unregister(ResourceProvider provider) {
		if (provider != null && this.providers.contains(provider)) {
			boolean succeed = this.providers.remove(provider);
			update();
			return succeed;
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 */
	public synchronized ResourceProvider unregister(String id) {
		if (id != null) {
			ResourceProvider providerToRemove = null;
			for (ResourceProvider provider : this.providers) {
				if (id.equals(provider.getId())) {
					providerToRemove = provider;
					break;
				}
			}
			if (providerToRemove != null) {
				this.providers.remove(providerToRemove);
				update();
				return providerToRemove;
			}
		}
		return null;
	}

	protected void update() {
		this.providersArray = this.providers.toArray(new ResourceProvider[this.providers.size()]);
	}

	public synchronized ResourceProvider[] getResourceProviders() {
		return this.providersArray;
	}

}
