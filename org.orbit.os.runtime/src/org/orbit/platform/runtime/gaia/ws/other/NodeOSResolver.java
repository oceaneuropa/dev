package org.orbit.platform.runtime.gaia.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.platform.runtime.Activator;
import org.orbit.platform.runtime.gaia.service.GAIA;

public class NodeOSResolver implements ContextResolver<GAIA> {

	@Override
	public GAIA getContext(Class<?> clazz) {
		return Activator.getInstance().getGAIA();
	}

}
