package org.orbit.infra.runtime.subscription.ws.command.targettype;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class UpdateSubsTargetTypeNameWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.UpdateSubsTargetTypeNameWSCommand";

	public UpdateSubsTargetTypeNameWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__UPDATE_TARGET_TYPE_NAME.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasTypeParam = request.hasParameter("type");
		boolean hasNameParam = request.hasParameter("name");
		if (!hasIdParam && !hasTypeParam) {
			ErrorDTO error = new ErrorDTO("'id' or 'type' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (!hasNameParam) {
			ErrorDTO error = new ErrorDTO("'name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String name = request.getStringParameter("name");

		boolean succeed = false;

		SubsServerService service = getService();
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			succeed = service.updateTargetTypeName(id, name);

		} else if (hasTypeParam) {
			String type = request.getStringParameter("type");
			succeed = service.updateTargetTypeName(type, name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
