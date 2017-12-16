package org.orbit.component.runtime.tier1.auth.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.auth.service.AuthService;

public class AuthWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public AuthWSApplication(final AuthService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(AuthService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(AuthService.class);
			}
		});
		register(AuthWSResource.class);
	}

}
