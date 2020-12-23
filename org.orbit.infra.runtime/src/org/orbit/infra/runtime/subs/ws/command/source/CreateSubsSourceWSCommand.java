package org.orbit.infra.runtime.subs.ws.command.source;

import java.util.Map;

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
public class CreateSubsSourceWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.CreateSubsSourceWSCommand";

	public CreateSubsSourceWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__CREATE_SOURCE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String type = request.getStringParameter("type"); // required
		String instanceId = request.getStringParameter("instanceId"); // required
		String name = request.getStringParameter("name"); // optional. if empty, use instanceId
		Map<String, Object> properties = null; // optional
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}

		if (type == null || type.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'type' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (instanceId == null || instanceId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'instanceId' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (name == null || name.isEmpty()) {
			name = instanceId;
		}

		SubsServerService service = getService();

		if (service.sourceExists(type, instanceId)) {
			ErrorDTO error = new ErrorDTO("Source with type '" + type + "' and instanceId '" + instanceId + "' already exists.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsSource source = service.createSource(type, instanceId, name, properties, true);
		if (source == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Source cannot be created.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsSourceDTO sourceDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(source);
		return Response.ok().entity(sourceDTO).build();
	}

}
