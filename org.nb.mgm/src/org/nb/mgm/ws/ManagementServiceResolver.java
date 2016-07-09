package org.nb.mgm.ws;

import javax.ws.rs.ext.ContextResolver;

import org.nb.mgm.Activator;
import org.nb.mgm.service.ManagementService;

public class ManagementServiceResolver implements ContextResolver<ManagementService> {

	@Override
	public ManagementService getContext(Class<?> clazz) {
		return Activator.getMgmService();
	}

}
