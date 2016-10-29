package org.origin.mgm.client.api.impl;

import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.api.IndexProviderFactory;
import org.origin.mgm.client.api.IndexServiceConfiguration;

public class IndexProviderFactoryImpl extends IndexProviderFactory {

	/**
	 * 
	 * @param config
	 * @return
	 */
	public IndexProvider createIndexProvider(IndexServiceConfiguration config) {
		return new IndexProviderImpl(config);
	}

}
