package org.orbit.platform.runtime.command.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.rest.server.AbstractJerseyWSApplication;

public class CommandServiceWSApplication extends AbstractJerseyWSApplication {

	/**
	 * 
	 * @param commandService
	 * @param feature
	 */
	public CommandServiceWSApplication(final CommandService commandService, int feature) {
		super(commandService.getContextRoot(), feature);
		adapt(CommandService.class, commandService);

		AbstractBinder serviceBinder = new AbstractBinder() {
			@Override
			protected void configure() {
				bind(commandService).to(CommandService.class);
			}
		};
		register(serviceBinder);
		register(CommandWSResource.class);
	}

}
