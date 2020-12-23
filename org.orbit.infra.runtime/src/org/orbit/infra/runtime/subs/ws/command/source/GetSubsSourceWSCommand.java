package org.orbit.infra.runtime.subs.ws.command.source;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.dto.SubsSourceDTO;
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
public class GetSubsSourceWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.GetSubsSourceWSCommand";

	public GetSubsSourceWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__GET_SOURCE.equalsIgnoreCase(requestName)) {
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

		SubsSource source = null;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			source = service.getSource(id);

		} else if (hasTypeParams) {
			String type = request.getStringParameter("type");
			String instanceId = request.getStringParameter("instanceId");
			source = service.getSource(type, instanceId);
		}

		if (source != null) {
			SubsSourceDTO sourceDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(source);
			return Response.ok().entity(sourceDTO).build();
		} else {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Source is not found.");
			// return Response.status(Status.NOT_FOUND).entity(error).build();
			return Response.ok().build();
		}
	}

}
