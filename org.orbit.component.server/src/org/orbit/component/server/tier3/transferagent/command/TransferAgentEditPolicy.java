package org.orbit.component.server.tier3.transferagent.command;

import org.orbit.component.model.tier3.transferagent.request.CreateNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.DeleteNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.StartNodeRequest;
import org.orbit.component.model.tier3.transferagent.request.StopNodeRequest;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.rest.agent.AbstractEditPolicy;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

/**
 * @see HomeAgentEditPolicy
 *
 */
public class TransferAgentEditPolicy extends AbstractEditPolicy {

	@Override
	public boolean understandsRequest(Request request) {
		TransferAgentService service = super.getService(TransferAgentService.class);
		if (service == null) {
			return false;
		}

		String requestName = request.getName();

		if (CreateNodeRequest.NAME.equals(requestName)) {
			return true;

		} else if (DeleteNodeRequest.NAME.equals(requestName)) {
			return true;

		} else if (StartNodeRequest.NAME.equals(requestName)) {
			return true;

		} else if (StopNodeRequest.NAME.equals(requestName)) {
			return true;
		}

		return false;
	}

	@Override
	public ICommand getCommand(Request request) {
		TransferAgentService service = super.getService(TransferAgentService.class);
		if (service == null) {
			return null;
		}

		String requestName = request.getName();

		if (CreateNodeRequest.NAME.equals(requestName)) {
			if (request instanceof CreateNodeRequest) {
				return new CreateNodeCommand((CreateNodeRequest) request);
			}

		} else if (DeleteNodeRequest.NAME.equals(requestName)) {
			if (request instanceof DeleteNodeRequest) {
				return new DeleteNodeCommand((DeleteNodeRequest) request);
			}

		} else if (StartNodeRequest.NAME.equals(requestName)) {
			if (request instanceof StartNodeRequest) {
				return new StartNodeCommand((StartNodeRequest) request);
			}

		} else if (StopNodeRequest.NAME.equals(requestName)) {
			if (request instanceof StopNodeRequest) {
				return new StopNodeCommand((StopNodeRequest) request);
			}
		}

		return null;
	}

	@Override
	public void createResponse(Request request, Responses responses, ICommandResult commandResult) {
		String requestName = request.getName();

	}

}
