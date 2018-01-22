package org.orbit.os.runtime.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.os.runtime.OSServices;
import org.orbit.os.runtime.gaia.GAIA;

public class NodeOSResolver implements ContextResolver<GAIA> {

	@Override
	public GAIA getContext(Class<?> clazz) {
		return OSServices.getInstance().getGAIA();
	}

}
