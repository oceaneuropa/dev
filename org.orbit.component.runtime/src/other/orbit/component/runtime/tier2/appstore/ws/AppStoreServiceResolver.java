package other.orbit.component.runtime.tier2.appstore.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;

public class AppStoreServiceResolver implements ContextResolver<AppStoreService> {

	@Override
	public AppStoreService getContext(Class<?> clazz) {
		return ComponentServices.getInstance().getAppStoreService();
	}

}
