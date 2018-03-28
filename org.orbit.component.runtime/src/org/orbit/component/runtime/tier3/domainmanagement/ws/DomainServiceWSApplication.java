package org.orbit.component.runtime.tier3.domainmanagement.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.origin.common.service.WebServiceAware;

public class DomainServiceWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public DomainServiceWSApplication(final DomainManagementService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(DomainManagementService.class, service);
		adapt(WebServiceAware.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DomainManagementService.class);
			}
		});
		register(DomainServiceWSMachinesResource.class);
		register(DomainServiceWSPlatformsResource.class);
		register(DomainServiceWSNodesResource.class);
	}

}
