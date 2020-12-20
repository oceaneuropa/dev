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
public class SubsMappingExistsWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.SubsMappingExistsWSCommand";

	public SubsMappingExistsWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__MAPPING_EXISTS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasSourceIdParam = request.hasParameter("sourceId");
		boolean hasTargetIdParam = request.hasParameter("targetId");
		boolean hasSourceIdAndTargetIdParams = hasSourceIdParam && hasTargetIdParam;

		boolean hasClientIdParam = request.hasParameter("clientId");
		boolean hasClientURLParam = request.hasParameter("clientURL");

		if (!hasIdParam && !hasSourceIdAndTargetIdParams) {
			ErrorDTO error = new ErrorDTO("'id' parameter OR 'sourceId' and 'targetId' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (hasSourceIdAndTargetIdParams) {
			if (!hasClientIdParam && !hasClientURLParam) {
				ErrorDTO error = new ErrorDTO("'clientId' parameter or 'clientURL' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		SubsServerService service = getService();

		boolean exists = false;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			exists = service.mappingExists(id);

		} else if (hasSourceIdAndTargetIdParams) {
			Integer sourceId = request.getIntegerParameter("sourceId");
			Integer targetId = request.getIntegerParameter("targetId");

			if (hasClientIdParam) {
				String clientId = request.getStringParameter("clientId");
				exists = service.mappingExistsByClientId(sourceId, targetId, clientId);

			} else if (hasClientURLParam) {
				String clientURL = request.getStringParameter("clientURL");
				exists = service.mappingExistsByClientURL(sourceId, targetId, clientURL);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
