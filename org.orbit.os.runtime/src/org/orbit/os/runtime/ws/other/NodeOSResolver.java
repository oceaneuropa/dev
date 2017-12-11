package org.orbit.os.runtime.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.os.runtime.Activator;
import org.orbit.os.runtime.service.GAIA;

public class NodeOSResolver implements ContextResolver<GAIA> {

	@Override
	public GAIA getContext(Class<?> clazz) {
		return Activator.getInstance().getGAIA();
	}

}
