package org.orbit.component.runtime.tier1.session.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.origin.common.rest.util.WebServiceAware;

public class OAuth2WSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public OAuth2WSApplication(final OAuth2Service service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(OAuth2Service.class, service);
		adapt(WebServiceAware.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(OAuth2Service.class);
			}
		});
		// register(OAuth2ServiceWSResource.class);
	}

}
