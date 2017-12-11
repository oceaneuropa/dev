package org.orbit.component.runtime.tier3.transferagent.ws.editpolicy.other;

import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

/**
 * @see HomeAgentEditPolicy
 *
 */
public class TransferAgentEditPolicyV1 extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		// TransferAgentService service = super.getService(TransferAgentService.class);
		// if (service == null) {
		// return null;
		// }
		// String requestName = request.getRequestName();
		TransferAgentService service = resource.getService(TransferAgentService.class);

		return null;
	}

}
