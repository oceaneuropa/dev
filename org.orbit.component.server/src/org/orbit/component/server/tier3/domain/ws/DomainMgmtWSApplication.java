package org.orbit.component.server.tier3.domain.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainMgmtWSApplication extends AbstractResourceConfigApplication {

	protected static Logger LOG = LoggerFactory.getLogger(DomainMgmtWSApplication.class);

	protected DomainManagementService service;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public DomainMgmtWSApplication(final BundleContext bundleContext, final DomainManagementService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DomainManagementService.class);
			}
		});
		register(DomainMgmtWSServiceResource.class);
		register(DomainMgmtWSMachinesResource.class);
		register(DomainMgmtWSTransferAgentsResource.class);
		register(DomainMgmtWSNodesResource.class);
	}

	public DomainManagementService getDomainManagementService() {
		return this.service;
	}

}
