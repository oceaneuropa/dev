package org.orbit.infra.runtime.subscription.ws.command.mapping;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
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
public class GetMappingWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.GetMappingWSCommand";

	public GetMappingWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__GET_MAPPING.equalsIgnoreCase(requestName)) {
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

		SubsMapping mapping = null;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			mapping = service.getMapping(id);

		} else if (hasSourceIdAndTargetIdParams) {
			Integer sourceId = request.getIntegerParameter("sourceId");
			Integer targetId = request.getIntegerParameter("targetId");

			if (hasClientIdParam) {
				String clientId = request.getStringParameter("clientId");
				mapping = service.getMappingByClientId(sourceId, targetId, clientId);

			} else if (hasClientURLParam) {
				String clientURL = request.getStringParameter("clientURL");
				mapping = service.getMappingByClientURL(sourceId, targetId, clientURL);
			}
		}

		if (mapping != null) {
			SubsMappingDTO mappingDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(mapping);
			return Response.ok().entity(mappingDTO).build();
		} else {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Mapping is not found.");
			// return Response.status(Status.NOT_FOUND).entity(error).build();
			return Response.ok().build();
		}
	}

}
