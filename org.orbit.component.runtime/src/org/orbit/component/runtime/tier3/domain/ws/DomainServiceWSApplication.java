package org.orbit.component.runtime.tier3.domain.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.domain.service.DomainService;

public class DomainServiceWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public DomainServiceWSApplication(final DomainService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(DomainService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(DomainService.class);
			}
		});
		register(DomainServiceWSMachinesResource.class);
		register(DomainServiceWSTransferAgentsResource.class);
		register(DomainServiceWSNodesResource.class);
	}

}
