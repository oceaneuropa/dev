package org.orbit.infra.runtime.datatube.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datatube.RuntimeChannelDTO;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.RuntimeChannel;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetRuntimeChannelCommand extends AbstractInfraCommand<DataTubeService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datatube.GetRuntimeChannelCommand";

	public GetRuntimeChannelCommand() {
		super(DataTubeService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATATUBE__GET_RUNTIME_CHANNEL.equalsIgnoreCase(requestName)) {
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

		boolean createIfNotExist = request.getBooleanParameter("create_if_not_exist");

		DataTubeService dataTubeService = getService();

		RuntimeChannel runtimeChannel = null;
		if (hasChannelId) {
			String channelId = request.getStringParameter("channel_id");
			runtimeChannel = dataTubeService.getRuntimeChannelById(channelId);

			if (runtimeChannel == null && createIfNotExist) {
				runtimeChannel = dataTubeService.createRuntimeChannelById(channelId);

				if (runtimeChannel == null) {
					ErrorDTO error = new ErrorDTO("Runtime channel (channelId='" + channelId + "') is not found and cannot be created.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
			}

		} else if (hasName) {
			String name = request.getStringParameter("name");
			runtimeChannel = dataTubeService.getRuntimeChannelByName(name);

			if (runtimeChannel == null && createIfNotExist) {
				runtimeChannel = dataTubeService.createRuntimeChannelByName(name);

				if (runtimeChannel == null) {
					ErrorDTO error = new ErrorDTO("Runtime channel (name='" + name + "') is not found and cannot be created.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
			}
		}

		if (runtimeChannel == null) {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "RuntimeChannel is not found");
			// return Response.status(Status.BAD_REQUEST).entity(error).build();
			return Response.ok().build();
		}

		RuntimeChannelDTO runtimeChannelDTO = ModelConverter.DATA_TUBE.toDTO(runtimeChannel);
		return Response.ok().entity(runtimeChannelDTO).build();
	}

}
