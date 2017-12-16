package org.orbit.infra.runtime.indexes.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class IndexServiceWSApplication extends AbstractJerseyWSApplication {

	public IndexServiceWSApplication(final IndexService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(IndexService.class, service);

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
	}

}
