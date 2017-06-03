package org.orbit.os.server.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.os.server.Activator;
import org.orbit.os.server.service.NodeOS;

public class NodeOSResolver implements ContextResolver<NodeOS> {

	@Override
	public NodeOS getContext(Class<?> clazz) {
		return Activator.getNodeOS();
	}

}
