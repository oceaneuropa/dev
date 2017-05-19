package org.orbit.component.server.tier2.appstore.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.server.Activator;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;

public class AppStoreServiceResolver implements ContextResolver<AppStoreService> {

	@Override
	public AppStoreService getContext(Class<?> clazz) {
		return Activator.getAppStoreService();
	}

}
