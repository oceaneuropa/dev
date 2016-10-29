package org.origin.mgm.client.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.mgm.client.api.impl.IndexProviderFactoryImpl;

public abstract class IndexProviderFactory {

	protected static List<IndexProviderFactoryProvider> factoryProviders = new ArrayList<IndexProviderFactoryProvider>();

	public static List<IndexProviderFactoryProvider> getFactoryProviders() {
		return factoryProviders;
	}

	public static void register(IndexProviderFactoryProvider factoryProvider) {
		if (factoryProvider != null && !factoryProviders.contains(factoryProvider)) {
			factoryProviders.add(factoryProvider);
		}
	}

	public static void unregister(String providerId) {
		if (providerId != null) {
			for (Iterator<IndexProviderFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
				IndexProviderFactoryProvider currProvider = providerItor.next();
				if (providerId.equals(currProvider)) {
					providerItor.remove();
				}
			}
		}
	}

	public static void unregister(IndexProviderFactoryProvider factoryProvider) {
		if (factoryProvider != null) {
			for (Iterator<IndexProviderFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
				IndexProviderFactoryProvider currProvider = providerItor.next();
				if (factoryProvider.equals(currProvider)) {
					providerItor.remove();
				}
			}
		}
	}

	protected static IndexProviderFactory INSTANCE = new IndexProviderFactoryImpl();

	public static IndexProviderFactory getInstance() {
		for (Iterator<IndexProviderFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
			IndexProviderFactoryProvider currProvider = providerItor.next();
			if (currProvider != null) {
				IndexProviderFactory factory = currProvider.getInstance();
				if (factory != null) {
					return factory;
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public abstract IndexProvider createIndexProvider(IndexServiceConfiguration config);

}
