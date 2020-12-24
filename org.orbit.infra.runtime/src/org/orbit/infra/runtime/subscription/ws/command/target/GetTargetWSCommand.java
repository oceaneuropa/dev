package org.orbit.infra.runtime.subscription.ws.command.target;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.dto.SubsTargetDTO;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class GetTargetWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.GetTargetWSCommand";

	public GetTargetWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__GET_TARGET.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasTypeParam = request.hasParameter("type");
		boolean hasInstanceIdParam = request.hasParameter("instanceId");
		boolean hasTypeParams = hasTypeParam && hasInstanceIdParam;

		if (!hasIdParam && !hasTypeParams) {
			ErrorDTO error = new ErrorDTO("'id' parameter OR 'type' and 'instanceId' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		SubsTarget target = null;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			target = service.getTarget(id);

		} else if (hasTypeParams) {
			String type = request.getStringParameter("type");
			String instanceId = request.getStringParameter("instanceId");
			target = service.getTarget(type, instanceId);
		}

		if (target != null) {
			SubsTargetDTO targetDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(target);
			return Response.ok().entity(targetDTO).build();
		} else {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Target is not found.");
			// return Response.status(Status.NOT_FOUND).entity(error).build();
			return Response.ok().build();
		}
	}

}
