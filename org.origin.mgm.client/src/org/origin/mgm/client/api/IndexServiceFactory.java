package org.origin.mgm.client.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.origin.mgm.client.api.impl.IndexServiceFactoryImpl;

public abstract class IndexServiceFactory {

	protected static List<IndexServiceFactoryProvider> factoryProviders = new ArrayList<IndexServiceFactoryProvider>();

	public static List<IndexServiceFactoryProvider> getFactoryProviders() {
		return factoryProviders;
	}

	public static void register(IndexServiceFactoryProvider factoryProvider) {
		if (factoryProvider != null && !factoryProviders.contains(factoryProvider)) {
			factoryProviders.add(factoryProvider);
		}
	}

	public static void unregister(String providerId) {
		if (providerId != null) {
			for (Iterator<IndexServiceFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
				IndexServiceFactoryProvider currProvider = providerItor.next();
				if (providerId.equals(currProvider)) {
					providerItor.remove();
				}
			}
		}
	}

	public static void unregister(IndexServiceFactoryProvider factoryProvider) {
		if (factoryProvider != null) {
			for (Iterator<IndexServiceFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
				IndexServiceFactoryProvider currProvider = providerItor.next();
				if (factoryProvider.equals(currProvider)) {
					providerItor.remove();
				}
			}
		}
	}

	protected static IndexServiceFactory INSTANCE = new IndexServiceFactoryImpl();

	public static IndexServiceFactory getInstance() {
		for (Iterator<IndexServiceFactoryProvider> providerItor = factoryProviders.iterator(); providerItor.hasNext();) {
			IndexServiceFactoryProvider currProvider = providerItor.next();
			if (currProvider != null) {
				IndexServiceFactory factory = currProvider.getInstance();
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
	public abstract IndexService createIndexService(IndexServiceConfiguration config);

}
