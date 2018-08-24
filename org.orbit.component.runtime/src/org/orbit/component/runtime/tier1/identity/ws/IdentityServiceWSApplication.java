package org.orbit.component.runtime.tier1.identity.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;

public class IdentityServiceWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public IdentityServiceWSApplication(final IdentityService service, int feature) {
		super(service, feature);
		// adapt(IdentityService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(IdentityService.class);
			}
		});
		register(IdentityServiceWSResource.class);
	}

}
