package org.orbit.infra.runtime.subscription.ws.command.sourcetype;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsType;
import org.orbit.infra.model.subs.dto.SubsTypeDTO;
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
public class CreateSubsSourceTypeWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.CreateSubsSourceTypeWSCommand";

	public CreateSubsSourceTypeWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__CREATE_SOURCE_TYPE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String type = request.getStringParameter("type"); // required
		String name = request.getStringParameter("name"); // optional. if empty, use instanceId

		if (type == null || type.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'type' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		if (service.sourceTypeExists(type)) {
			ErrorDTO error = new ErrorDTO("Source type already exists.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsType typeObj = service.createSourceType(type, name);
		if (typeObj == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Source type cannot be created.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(typeObj);
		return Response.ok().entity(typeDTO).build();
	}

}
