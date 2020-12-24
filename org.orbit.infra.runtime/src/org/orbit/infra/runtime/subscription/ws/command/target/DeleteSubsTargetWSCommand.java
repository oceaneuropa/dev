package org.orbit.infra.runtime.subscription.ws.command.target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsTarget;
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
public class DeleteSubsTargetWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.DeleteSubsTargetWSCommand";

	public DeleteSubsTargetWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__DELETE_TARGET.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasIdParam = request.hasParameter("id");
		boolean hasIdsParam = request.hasParameter("ids");

		boolean hasTypeParam = request.hasParameter("type");
		boolean hasInstanceIdParam = request.hasParameter("instanceId");
		boolean hasTypeParams = hasTypeParam && hasInstanceIdParam;

		if (!hasIdParam && !hasIdsParam && !hasTypeParams) {
			ErrorDTO error = new ErrorDTO("'id' OR 'ids' OR 'type' and 'instanceId' parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		SubsTarget target = null;

		SubsServerService service = getService();
		boolean force = request.getBooleanParameter("force");
		if (!force) {
			boolean mappingExists = false;
			if (hasIdParam) {
				Integer id = request.getIntegerParameter("id");
				List<SubsMapping> mappings = service.getMappingsOfTarget(id);
				if (!mappings.isEmpty()) {
					mappingExists = true;
				}

			} else if (hasTypeParams) {
				String type = request.getStringParameter("type");
				String instanceId = request.getStringParameter("instanceId");
				target = service.getTarget(type, instanceId);

				if (target != null) {
					Integer id = target.getId();
					List<SubsMapping> mappings = service.getMappingsOfTarget(id);
					if (!mappings.isEmpty()) {
						mappingExists = true;
					}
				}

			} else if (hasIdsParam) {
				Integer[] ids = request.getIntegerArrayParameter("ids");
				for (Integer currId : ids) {
					List<SubsMapping> mappings = service.getMappingsOfTarget(currId);
					if (!mappings.isEmpty()) {
						mappingExists = true;
						break;
					}
				}
			}

			if (mappingExists) {
				ErrorDTO error = new ErrorDTO("Mappings of the target(s) exist. Delete the mappings first or use 'force' parameter to delete the target(s), which will also delete the mappings of the target(s).");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		boolean succeed = false;
		if (hasIdParam) {
			Integer id = request.getIntegerParameter("id");
			succeed = service.deleteTarget(id);

		} else if (hasTypeParams) {
			if (target == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Target is not found.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			Integer id = target.getId();
			succeed = service.deleteTarget(id);

		} else if (hasIdsParam) {
			Integer[] ids = request.getIntegerArrayParameter("ids");
			if (ids != null && ids.length > 0) {
				succeed = service.deleteTargets(ids);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
