package org.orbit.infra.runtime.indexes.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.infra.runtime.indexes.service.IndexService;

public class IndexServiceResolverV1 implements ContextResolver<IndexService> {

	@Override
	public IndexService getContext(Class<?> clazz) {
		// return Activator.getIndexService();
		return null;
	}

}