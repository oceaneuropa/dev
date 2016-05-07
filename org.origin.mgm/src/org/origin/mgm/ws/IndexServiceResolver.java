package org.origin.mgm.ws;

import javax.ws.rs.ext.ContextResolver;

import org.origin.mgm.Activator;
import org.origin.mgm.service.IndexService;

public class IndexServiceResolver implements ContextResolver<IndexService> {

	@Override
	public IndexService getContext(Class<?> clazz) {
		return Activator.getIndexService();
	}

}
