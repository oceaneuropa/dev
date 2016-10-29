package org.origin.mgm.client.api.impl;

import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.api.IndexServiceFactory;

public class IndexServiceFactoryImpl extends IndexServiceFactory {

	@Override
	public IndexService createIndexService(IndexServiceConfiguration config) {
		return new IndexServiceImpl(config);
	}

}
