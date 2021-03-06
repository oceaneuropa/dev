package org.orbit.component.runtime.tier3.domain.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;

public class DomainServiceWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public DomainServiceWSApplication(final DomainManagementService service, int feature) {
		super(service, feature);
		// adapt(DomainManagementService.class, service);

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
