package org.orbit.infra.runtime.subs.ws.command;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
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
public class CreateSubsMappingWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.CreateSubsMappingWSCommand";

	public CreateSubsMappingWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__CREATE_MAPPING.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		Integer sourceId = request.getIntegerParameter("sourceId"); // required
		Integer targetId = request.getIntegerParameter("targetId"); // required
		String clientId = request.getStringParameter("clientId"); // required
		String clientURL = request.getStringParameter("clientURL");
		Map<String, Object> properties = null; // optional
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}

		if (sourceId == null) {
			ErrorDTO error = new ErrorDTO("'sourceId' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (targetId == null) {
			ErrorDTO error = new ErrorDTO("'targetId' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (clientId == null) {
			ErrorDTO error = new ErrorDTO("'clientId' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		if (service.mappingExistsByClientId(sourceId, targetId, clientId)) {
			ErrorDTO error = new ErrorDTO("Mapping with sourceId '" + sourceId + "', targetId '" + targetId + "' and clientId '" + clientId + "' already exists.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsMapping mapping = service.createMapping(sourceId, targetId, clientId, clientURL, properties);
		if (mapping == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Mapping cannot be created.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsMappingDTO mappingDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(mapping);
		return Response.ok().entity(mappingDTO).build();
	}

}
