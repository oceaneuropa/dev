package org.orbit.infra.runtime.indexes.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceWSApplication.class);

	protected IndexService service;

	public IndexServiceWSApplication(final BundleContext bundleContext, final IndexService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

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

	public IndexService getService() {
		return this.service;
	}

}
