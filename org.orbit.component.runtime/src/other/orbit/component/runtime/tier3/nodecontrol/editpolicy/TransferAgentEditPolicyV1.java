package other.orbit.component.runtime.tier3.nodecontrol.editpolicy;

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
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
		NodeControlService service = resource.getService(NodeControlService.class);

		return null;
	}

}
