package org.orbit.component.server.tier3.transferagent.editpolicy;

import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.agent.AbstractWSEditPolicy;
import org.origin.common.rest.model.Request;

/**
 * @see HomeAgentEditPolicy
 *
 */
public class TransferAgentEditPolicy extends AbstractWSEditPolicy {

	@Override
	public ICommand getCommand(Request request) {
		TransferAgentService service = super.getService(TransferAgentService.class);
		if (service == null) {
			return null;
		}

		// String requestName = request.getRequestName();

		return null;
	}

}
