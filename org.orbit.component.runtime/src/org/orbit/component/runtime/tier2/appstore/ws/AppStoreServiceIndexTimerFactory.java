package org.orbit.component.runtime.tier2.appstore.ws;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;

public class AppStoreServiceIndexTimerFactory implements ServiceIndexTimerFactory<AppStoreService> {

	@Override
	public AppStoreServiceIndexTimer create(AppStoreService service) {
		return new AppStoreServiceIndexTimer(service);
	}

}
