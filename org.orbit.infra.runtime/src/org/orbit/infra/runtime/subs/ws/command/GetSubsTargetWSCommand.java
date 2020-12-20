package org.orbit.infra.runtime.subs.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.dto.SubsTargetDTO;
import org.orbit.infra.runtime.subs.SubsServerService;
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
public class GetSubsTargetWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.GetSubsTargetWSCommand";

	public GetSubsTargetWSCommand() {
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
		boolean hasTypeParam1 = request.hasParameter("type");
		boolean hasTypeIdParam2 = request.hasParameter("typeId");
		boolean hasTypeParams = hasTypeParam1 && hasTypeIdParam2;

		if (!hasIdParam && !hasTypeParams) {
			ErrorDTO error = new ErrorDTO("'id' parameter OR 'type' and 'typeId' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		SubsTarget target = null;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			target = service.getTarget(id);

		} else if (hasTypeParams) {
			String type = request.getStringParameter("type");
			String typeId = request.getStringParameter("typeId");
			target = service.getTarget(type, typeId);
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
