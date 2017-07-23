package org.orbit.component.server.tier3.transferagent.editpolicy;

import org.orbit.component.model.tier3.transferagent.request.CreateNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.DeleteNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.StartNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.StopNodeRequest;
import org.orbit.component.server.tier3.transferagent.command.CreateNodeCommand;
import org.orbit.component.server.tier3.transferagent.command.DeleteNodeCommand;
import org.orbit.component.server.tier3.transferagent.command.StartNodeCommand;
import org.orbit.component.server.tier3.transferagent.command.StopNodeCommand;
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

		String requestName = request.getRequestName();

		if (CreateNodeRequest.REQUEST_NAME.equals(requestName)) {
			if (request instanceof CreateNodeRequest) {
				return new CreateNodeCommand((CreateNodeRequest) request);
			}

		} else if (DeleteNodeRequest.REQUEST_NAME.equals(requestName)) {
			if (request instanceof DeleteNodeRequest) {
				return new DeleteNodeCommand((DeleteNodeRequest) request);
			}

		} else if (StartNodeRequest.REQUEST_NAME.equals(requestName)) {
			if (request instanceof StartNodeRequest) {
				return new StartNodeCommand((StartNodeRequest) request);
			}

		} else if (StopNodeRequest.REQUEST_NAME.equals(requestName)) {
			if (request instanceof StopNodeRequest) {
				return new StopNodeCommand((StopNodeRequest) request);
			}
		}

		return null;
	}

}
