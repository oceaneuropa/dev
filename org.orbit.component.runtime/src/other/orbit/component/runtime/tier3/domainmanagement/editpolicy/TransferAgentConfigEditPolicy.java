package other.orbit.component.runtime.tier3.domainmanagement.editpolicy;

import org.orbit.component.runtime.common.ws.Requests;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.origin.common.command.ICommand;
import org.origin.common.rest.editpolicy.AbstractWSEditPolicyV1;
import org.origin.common.rest.editpolicy.EditpolicyWSApplicationResource;
import org.origin.common.rest.model.Request;

import other.orbit.component.runtime.tier3.domainmanagement.command.PlatformConfigAddCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.PlatformConfigGetCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.PlatformConfigRemoveCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.PlatformConfigUpdateCommand;
import other.orbit.component.runtime.tier3.domainmanagement.command.PlatformConfigsGetCommand;

public class TransferAgentConfigEditPolicy extends AbstractWSEditPolicyV1 {

	@Override
	public ICommand getCommand(EditpolicyWSApplicationResource resource, Request request) {
		DomainManagementService service = resource.getService(DomainManagementService.class);

		String requestName = request.getRequestName();

		if (Requests.GET_TA_CONFIGS.equals(requestName)) {
			return new PlatformConfigsGetCommand(service, request);

		} else if (Requests.GET_TA_CONFIG.equals(requestName)) {
			return new PlatformConfigGetCommand(service, request);

		} else if (Requests.ADD_TA_CONFIG.equals(requestName)) {
			return new PlatformConfigAddCommand(service, request);

		} else if (Requests.UPDATE_TA_CONFIG.equals(requestName)) {
			return new PlatformConfigUpdateCommand(service, request);

		} else if (Requests.REMOVE_TA_CONFIG.equals(requestName)) {
			return new PlatformConfigRemoveCommand(service, request);
		}

		return null;
	}

}
