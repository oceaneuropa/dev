package org.orbit.infra.runtime.indexes.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.rest.auth.TestRequestFilter;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.WebServiceAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceWSApplication extends AbstractJerseyWSApplication {

	private Logger LOG = LoggerFactory.getLogger(IndexServiceWSApplication.class);

	public IndexServiceWSApplication(final IndexService service, int feature) {
		super(service, feature);
		LOG.debug("IndexServiceWSApplication()");

		adapt(IndexService.class, service);
		adapt(WebServiceAware.class, service);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(IndexService.class);
			}
		};
		register(serviceBinder);
		register(IndexServiceWSResource.class);
		register(IndexItemsWSResource.class);
		register(IndexItemWSResource.class);
		register(TestRequestFilter.class);
	}

}
