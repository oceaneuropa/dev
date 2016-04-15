package osgi.mgm.ws.mgm.resources;

import javax.ws.rs.ext.ContextResolver;

import osgi.mgm.Activator;
import osgi.mgm.service.MgmService;

public class MgmServiceResolver implements ContextResolver<MgmService> {

	@Override
	public MgmService getContext(Class<?> clazz) {
		return Activator.getMgmService();
	}

}
