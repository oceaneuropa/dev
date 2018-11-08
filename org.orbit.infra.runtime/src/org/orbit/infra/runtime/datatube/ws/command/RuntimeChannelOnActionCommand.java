package org.orbit.infra.runtime.datatube.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class RuntimeChannelOnActionCommand extends AbstractInfraCommand<DataTubeService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datatube.RuntimeChannelOnActionCommand";

	public RuntimeChannelOnActionCommand() {
		super(DataTubeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATATUBE__RUNTIME_CHANNEL_ON_ACTION.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasChannelId = request.hasParameter("channel_id");
		boolean hasName = request.hasParameter("name");

		if (!hasChannelId && !hasName) {
			ErrorDTO error = new ErrorDTO("'channel_id' parameter or 'name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String action = request.getStringParameter("action");
		if (action == null || action.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'action' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataTubeService dataTubeService = getService();

		if (!RequestConstants.RUNTIME_CHANNEL_ACTIONS.contains(action)) {
			ErrorDTO error = new ErrorDTO("Runtime channel action '" + action + "' is not supported.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;
		if (hasChannelId) {
			String channelId = request.getStringParameter("channel_id");

			if (RequestConstants.RUNTIME_CHANNEL_ACTION__START.equals(action)) {
				succeed = dataTubeService.startRuntimeChannelById(channelId);

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__SUSPEND.equals(action)) {
				succeed = dataTubeService.suspendRuntimeChannelById(channelId);

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__STOP.equals(action)) {
				succeed = dataTubeService.stopRuntimeChannelById(channelId);
			}

		} else if (hasName) {
			String name = request.getStringParameter("name");

			if (RequestConstants.RUNTIME_CHANNEL_ACTION__START.equals(action)) {
				succeed = dataTubeService.startRuntimeChannelByName(name);

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__SUSPEND.equals(action)) {
				succeed = dataTubeService.suspendRuntimeChannelByName(name);

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__STOP.equals(action)) {
				succeed = dataTubeService.stopRuntimeChannelByName(name);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
