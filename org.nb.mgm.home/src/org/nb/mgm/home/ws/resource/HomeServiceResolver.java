package org.nb.mgm.home.ws.resource;

import javax.ws.rs.ext.ContextResolver;

import org.nb.mgm.home.Activator;
import org.nb.mgm.home.service.HomeService;

public class HomeServiceResolver implements ContextResolver<HomeService> {

	@Override
	public HomeService getContext(Class<?> clazz) {
		return Activator.getHomeService();
	}

}
