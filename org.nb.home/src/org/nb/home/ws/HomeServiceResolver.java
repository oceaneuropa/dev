package org.nb.home.ws;

import javax.ws.rs.ext.ContextResolver;

import org.nb.home.Activator;
import org.nb.home.service.HomeService;

public class HomeServiceResolver implements ContextResolver<HomeService> {

	@Override
	public HomeService getContext(Class<?> clazz) {
		return Activator.getHomeService();
	}

}
