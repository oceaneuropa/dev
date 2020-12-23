package org.orbit.infra.runtime.subs.ws.command.sourcetype;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSourceType;
import org.orbit.infra.model.subs.dto.SubsSourceTypeDTO;
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
public class GetSubsSourceTypeWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.GetSubsSourceTypeWSCommand";

	public GetSubsSourceTypeWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__GET_SOURCE_TYPE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasTypeParam = request.hasParameter("type");

		if (hasTypeParam) {
			ErrorDTO error = new ErrorDTO("'type' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		String type = request.getStringParameter("type");
		SubsSourceType typeObj = service.getSourceType(type);

		if (typeObj != null) {
			SubsSourceTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(typeObj);
			return Response.ok().entity(typeDTO).build();
		} else {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Source is not found.");
			// return Response.status(Status.NOT_FOUND).entity(error).build();
			return Response.ok().build();
		}
	}

}
