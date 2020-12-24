package org.orbit.infra.runtime.subscription.ws.command.targettype;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsType;
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
public class DeleteSubsTargetTypeWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.DeleteSubsTargetTypeWSCommand";

	public DeleteSubsTargetTypeWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__DELETE_TARGET_TYPE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasTypeParam = request.hasParameter("type");

		if (!hasIdParam && !hasTypeParam) {
			ErrorDTO error = new ErrorDTO("'id' or 'type' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();
		boolean force = request.getBooleanParameter("force");
		if (!force) {
			boolean typeIsUsed = false;

			String type = null;
			if (hasIdParam) {
				Integer id = request.getIntegerParameter("id");
				SubsType typeObj = service.getTargetType(id);
				if (typeObj == null) {
					ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Target type is not found.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();

				} else {
					type = typeObj.getType();
				}
			} else if (hasTypeParam) {
				type = request.getStringParameter("type");
			}
			if (type != null) {
				typeIsUsed = service.targetExists(type);
			}

			if (typeIsUsed) {
				ErrorDTO error = new ErrorDTO("Target with the type exist. Delete the targets first or use 'force' parameter to delete the type.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		boolean succeed = false;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			succeed = service.deleteSourceType(id);

		} else if (hasTypeParam) {
			String type = request.getStringParameter("type");
			service.deleteSourceType(type);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
