package org.orbit.infra.runtime.subscription.ws.command.source;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSource;
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
public class UpdateSubsSourceWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.UpdateSubsSourceWSCommand";

	public UpdateSubsSourceWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__UPDATE_SOURCE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		Integer id = request.getIntegerParameter("id");
		if (id == null) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasTypeParam = request.hasParameter("type");
		boolean hasInstanceIdParam = request.hasParameter("instanceId");

		boolean hasNameParam = request.hasParameter("name");
		boolean hasPropertiesParam = request.hasParameter("properties");

		if (!hasTypeParam && !hasInstanceIdParam && !hasNameParam && !hasPropertiesParam) {
			ErrorDTO error = new ErrorDTO("'type' or 'instanceId' or 'name' or 'properties' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		SubsServerService service = getService();

		SubsSource source = service.getSource(id);
		if (source == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Source is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		if (hasTypeParam || hasInstanceIdParam) {
			String type = null;
			String instanceId = null;
			if (hasTypeParam && hasInstanceIdParam) {
				type = request.getStringParameter("type");
				instanceId = request.getStringParameter("instanceId");

			} else if (hasTypeParam) {
				type = request.getStringParameter("type");
				instanceId = source.getInstanceId();

			} else if (hasInstanceIdParam) {
				type = source.getType();
				instanceId = request.getStringParameter("instanceId");
			}

			SubsSource anotherSource = service.getSource(type, instanceId);

			if (anotherSource != null && id.equals(anotherSource.getId())) {
				ErrorDTO error = new ErrorDTO("Source with type '" + type + "' and instanceId '" + instanceId + "' already exists.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		// Update type
		if (hasTypeParam) {
			String type = request.getStringParameter("type");
			boolean currSucceed = service.updateSourceType(id, type, true);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update instanceId
		if (hasInstanceIdParam) {
			String instanceId = request.getStringParameter("instanceId");
			boolean currSucceed = service.updateSourceInstanceId(id, instanceId);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update name
		if (hasNameParam) {
			String name = request.getStringParameter("name");
			boolean currSucceed = service.updateSourceName(id, name);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// Update properties
		if (hasPropertiesParam) {
			Map<String, Object> properties = (Map<String, Object>) request.getMapParameter("properties");
			boolean clearProperties = request.getBooleanParameter("clearProperties");

			boolean currSucceed = service.updateSourceProperties(id, properties, clearProperties);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		boolean succeed = false;
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
