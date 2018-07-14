package org.orbit.platform.runtime.command.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.runtime.command.service.CommandService;

public class CommandServiceIndexTimerFactory implements ServiceIndexTimerFactory<CommandService> {

	@Override
	public CommandServiceIndexTimer create(IndexProvider indexProvider, CommandService service) {
		return new CommandServiceIndexTimer(indexProvider, service);
	}

}
