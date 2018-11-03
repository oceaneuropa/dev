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

public class RuntimeChannelExistsCommand extends AbstractInfraCommand<DataTubeService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datatube.RuntimeChannelExistsCommand";

	public RuntimeChannelExistsCommand() {
		super(DataTubeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATATUBE__RUNTIME_CHANNEL_EXISTS.equalsIgnoreCase(requestName)) {
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

		DataTubeService dataTubeService = getService();

		boolean exists = false;

		if (hasChannelId) {
			String channelId = request.getStringParameter("channel_id");
			exists = dataTubeService.runtimeChannelExistsById(channelId);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			exists = dataTubeService.runtimeChannelExistsByName(name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
