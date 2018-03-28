package org.orbit.infra.runtime.channel.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.infra.runtime.channel.service.ChannelService;
import org.origin.common.rest.server.AbstractJerseyWSApplication;
import org.origin.common.service.WebServiceAware;

public class ChannelWSApplication extends AbstractJerseyWSApplication {

	public ChannelWSApplication(final ChannelService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(ChannelService.class, service);
		adapt(WebServiceAware.class, service);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ChannelService.class);
			}
		};
		register(serviceBinder);
		register(ChannelWSResource.class);
	}

}
