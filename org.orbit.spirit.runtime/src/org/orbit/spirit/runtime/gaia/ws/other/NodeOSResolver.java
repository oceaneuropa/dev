package org.orbit.spirit.runtime.gaia.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.spirit.runtime.gaia.service.GAIA;

public class NodeOSResolver implements ContextResolver<GAIA> {

	@Override
	public GAIA getContext(Class<?> clazz) {
		// return Activator.getInstance().getGAIA();
		return null;
	}

}
