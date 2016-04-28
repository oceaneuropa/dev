package org.nb.mgm.ws;

import javax.ws.rs.ext.ContextResolver;

import org.nb.mgm.Activator;
import org.nb.mgm.service.MgmService;

public class MgmServiceResolver implements ContextResolver<MgmService> {

	@Override
	public MgmService getContext(Class<?> clazz) {
		return Activator.getMgmService();
	}

}
