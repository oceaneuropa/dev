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
public class SubsSourceExistsWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.SubsSourceExistsWSCommand";

	public SubsSourceExistsWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__SOURCE_EXISTS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasTypeParam1 = request.hasParameter("type");
		boolean hasTypeIdParam2 = request.hasParameter("typeId");
		boolean hasTypeParams = hasTypeParam1 && hasTypeIdParam2;

		if (!hasIdParam && !hasTypeParams) {
			ErrorDTO error = new ErrorDTO("'id' parameter OR 'type' and 'typeId' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		boolean exists = false;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			exists = service.sourceExists(id);

		} else if (hasTypeParams) {
			String type = request.getStringParameter("type");
			String typeId = request.getStringParameter("typeId");
			exists = service.sourceExists(type, typeId);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
