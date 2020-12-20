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
public class UpdateSubsMappingWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.UpdateSubsMappingWSCommand";

	public UpdateSubsMappingWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__UPDATE_MAPPING.equalsIgnoreCase(requestName)) {
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

		boolean hasPropertiesParam = request.hasParameter("properties");
		boolean hasClientIdParam = request.hasParameter("clientId");
		boolean hasClientURLParam = request.hasParameter("clientURL");
		boolean hasClientHeartbeatParam = request.hasParameter("clientHeartbeat");

		if (!hasPropertiesParam && !hasClientIdParam && !hasClientURLParam && !hasClientHeartbeatParam) {
			ErrorDTO error = new ErrorDTO("'properties' or 'clientId' or 'clientURL' or 'clientHeartbeat' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		SubsServerService service = getService();

		boolean mappingExists = service.mappingExists(id);
		if (!mappingExists) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Mapping is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// Update properties
		if (hasPropertiesParam) {
			Map<String, Object> properties = (Map<String, Object>) request.getMapParameter("properties");
			boolean currSucceed = service.updateMappingProperties(id, properties);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update clientId
		if (hasClientIdParam) {
			String clientId = request.getStringParameter("clientId");
			boolean currSucceed = service.updateMappingClientId(id, clientId);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update clientURL
		if (hasClientURLParam) {
			String clientURL = request.getStringParameter("clientURL");
			boolean currSucceed = service.updateMappingClientURL(id, clientURL);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update clientHeartbeatTime
		if (hasClientHeartbeatParam) {
			boolean isClientHeartbeat = request.getBooleanParameter("clientHeartbeat");
			if (isClientHeartbeat) {
				boolean currSucceed = service.updateMappingClientHeartbeat(id);
				if (currSucceed) {
					hasSucceed = true;
				} else {
					hasFailed = true;
				}
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
