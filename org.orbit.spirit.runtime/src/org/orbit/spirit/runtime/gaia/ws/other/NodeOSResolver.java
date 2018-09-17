package org.orbit.spirit.runtime.gaia.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.spirit.runtime.gaia.service.GaiaService;

public class NodeOSResolver implements ContextResolver<GaiaService> {

	@Override
	public GaiaService getContext(Class<?> clazz) {
		// return Activator.getInstance().getGAIA();
		return null;
	}

}
