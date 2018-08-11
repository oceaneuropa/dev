package org.orbit.infra.runtime.extensionregistry.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.origin.common.rest.auth.TestRequestFilter;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.WebServiceAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionRegistryWSApplication extends AbstractJerseyWSApplication {

	private Logger LOG = LoggerFactory.getLogger(ExtensionRegistryWSApplication.class);

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public ExtensionRegistryWSApplication(final ExtensionRegistryService service, int feature) {
		super(service, feature);
		LOG.debug("ExtensionRegistryServiceWSApplication()");

		adapt(ExtensionRegistryService.class, service);
		adapt(WebServiceAware.class, service);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ExtensionRegistryService.class);
			}
		};
		register(serviceBinder);
		register(ExtensionRegistryWSResource.class);
		register(ExtensionItemsWSResource.class);
		register(ExtensionItemWSResource.class);
		register(TestRequestFilter.class);
	}

}
