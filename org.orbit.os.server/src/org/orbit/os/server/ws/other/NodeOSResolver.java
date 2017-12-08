package org.orbit.os.server.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.os.server.Activator;
import org.orbit.os.server.service.GAIA;

public class NodeOSResolver implements ContextResolver<GAIA> {

	@Override
	public GAIA getContext(Class<?> clazz) {
		return Activator.getNodeOS();
	}

}
