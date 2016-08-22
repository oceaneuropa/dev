package org.nb.home.ws;

import javax.ws.rs.ext.ContextResolver;

import org.nb.home.Activator;
import org.nb.home.service.HomeAgentService;

public class HomeServiceResolver implements ContextResolver<HomeAgentService> {

	@Override
	public HomeAgentService getContext(Class<?> clazz) {
		return Activator.getHomeService();
	}

}
