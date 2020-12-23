package org.orbit.infra.runtime.subs.ws.command.mapping;

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
public class DeleteSubsMappingWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.DeleteSubsMappingWSCommand";

	public DeleteSubsMappingWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__DELETE_MAPPING.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasId = request.hasParameter("id");
		boolean hasIds = request.hasParameter("ids");

		if (!hasId && !hasIds) {
			ErrorDTO error = new ErrorDTO("'id' parameter or 'ids' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();

		boolean succeed = false;
		if (hasId) {
			Integer id = request.getIntegerParameter("id");
			succeed = service.deleteMapping(id);

		} else if (hasIds) {
			Integer[] ids = request.getIntegerArrayParameter("ids");
			if (ids != null && ids.length > 0) {
				succeed = service.deleteMappings(ids);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
