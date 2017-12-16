package org.orbit.component.runtime.tier3.transferagent.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;

/**
 * https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class TransferAgentWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public TransferAgentWSApplication(final TransferAgentService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(TransferAgentService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(TransferAgentService.class);
			}
		});
		register(TransferAgentWSServiceResource.class);
	}

}
