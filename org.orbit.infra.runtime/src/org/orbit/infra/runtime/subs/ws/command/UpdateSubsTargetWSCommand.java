package org.orbit.infra.runtime.subs.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.subs.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class UpdateSubsTargetWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.UpdateSubsTargetWSCommand";

	public UpdateSubsTargetWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__UPDATE_TARGET.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		Integer id = request.getIntegerParameter("id");
		if (id == null) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasTypeParam = request.hasParameter("type");
		boolean hasTypeIdParam = request.hasParameter("typeId");
		boolean hasTypeParams = hasTypeParam && hasTypeIdParam;
		boolean hasNameParam = request.hasParameter("name");
		boolean hasPropertiesParam = request.hasParameter("properties");

		boolean hasServerIdParam = request.hasParameter("serverId");
		boolean hasServerURLParam = request.hasParameter("serverURL");
		boolean hasServerParams = hasServerIdParam && hasServerURLParam;

		if (!hasTypeParams && !hasNameParam && !hasPropertiesParam && !hasServerParams) {
			ErrorDTO error = new ErrorDTO("'type' and 'typeId' parameters OR 'name' parameter OR 'properties' parameter OR 'serverId' and 'serverURL' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		SubsServerService service = getService();

		boolean targetExists = service.targetExists(id);
		if (!targetExists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Target is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// Update type and typeId
		if (hasTypeParams) {
			String type = request.getStringParameter("type");
			String typeId = request.getStringParameter("typeId");
			boolean currSucceed = service.updateTargetType(id, type, typeId);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update name
		if (hasNameParam) {
			String name = request.getStringParameter("name");
			boolean currSucceed = service.updateTargetName(id, name);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update properties
		if (hasPropertiesParam) {
			Map<String, Object> properties = (Map<String, Object>) request.getMapParameter("properties");
			boolean currSucceed = service.updateTargetProperties(id, properties);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update serverId and serverURL
		if (hasServerParams) {
			String serverId = request.getStringParameter("serverId");
			String serverURL = request.getStringParameter("serverURL");
			boolean currSucceed = service.updateServerURL(id, serverId, serverURL);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		boolean succeed = false;
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
