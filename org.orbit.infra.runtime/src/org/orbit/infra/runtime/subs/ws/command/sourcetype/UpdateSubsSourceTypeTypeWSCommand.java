package org.orbit.infra.runtime.subs.ws.command.sourcetype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSourceType;
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
public class UpdateSubsSourceTypeTypeWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.UpdateSubsSourceTypeWSCommand";

	public UpdateSubsSourceTypeTypeWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__UPDATE_SOURCE_TYPE_TYPE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasTypeParam = request.hasParameter("type");
		if (!hasIdParam) {
			ErrorDTO error = new ErrorDTO("'id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (!hasTypeParam) {
			ErrorDTO error = new ErrorDTO("'type' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Integer id = request.getIntegerParameter("id");
		String type = request.getStringParameter("type");

		SubsServerService service = getService();

		SubsSourceType typeObj = service.getSourceType(id);
		if (typeObj == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Source type is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (hasTypeParam) {
			boolean typeExists = false;
			List<SubsSourceType> types = service.getSourceTypes();
			for (SubsSourceType currTypeObj : types) {
				Integer currId = currTypeObj.getId();
				String currType = currTypeObj.getType();
				if (!currId.equals(typeObj.getId()) && currType != null && currType.equals(type)) {
					typeExists = true;
					break;
				}
			}
			if (typeExists) {
				ErrorDTO error = new ErrorDTO("Source type already exists.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		boolean succeed = service.updateSourceTypeType(id, type);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
