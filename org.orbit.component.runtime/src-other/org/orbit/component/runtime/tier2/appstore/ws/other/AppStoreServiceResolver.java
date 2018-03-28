package org.orbit.component.runtime.tier2.appstore.ws.other;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;

public class AppStoreServiceResolver implements ContextResolver<AppStoreService> {

	@Override
	public AppStoreService getContext(Class<?> clazz) {
		return OrbitServices.getInstance().getAppStoreService();
	}

}
