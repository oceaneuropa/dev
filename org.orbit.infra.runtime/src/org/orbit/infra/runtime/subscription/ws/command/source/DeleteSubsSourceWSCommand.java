package org.orbit.infra.runtime.subscription.ws.command.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class DeleteSubsSourceWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.DeleteSubsSourceWSCommand";

	public DeleteSubsSourceWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__DELETE_SOURCE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasIdsParam = request.hasParameter("ids");

		if (!hasIdParam && !hasIdsParam) {
			ErrorDTO error = new ErrorDTO("'id' or 'ids' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsServerService service = getService();
		boolean force = request.getBooleanParameter("force");
		if (!force) {
			boolean mappingExists = false;
			if (hasIdParam) {
				Integer id = request.getIntegerParameter("id");
				List<SubsMapping> mappings = service.getMappingsOfSource(id);
				if (!mappings.isEmpty()) {
					mappingExists = true;
				}
			} else if (hasIdsParam) {
				Integer[] ids = request.getIntegerArrayParameter("ids");
				for (Integer currId : ids) {
					List<SubsMapping> mappings = service.getMappingsOfSource(currId);
					if (!mappings.isEmpty()) {
						mappingExists = true;
						break;
					}
				}
			}
			if (mappingExists) {
				ErrorDTO error = new ErrorDTO("Mappings of the source(s) exist. Delete the mappings first or use 'force' parameter to delete the source(s), which will also delete the mappings of the source(s).");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		boolean succeed = false;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			succeed = service.deleteSource(id);

		} else if (hasIdsParam) {
			Integer[] ids = request.getIntegerArrayParameter("ids");
			if (ids != null && ids.length > 0) {
				succeed = service.deleteSources(ids);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
