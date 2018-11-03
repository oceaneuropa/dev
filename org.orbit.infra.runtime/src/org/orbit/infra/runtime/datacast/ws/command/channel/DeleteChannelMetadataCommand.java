package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class DeleteChannelMetadataCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.DeleteChannelMetadataCommand";

	public DeleteChannelMetadataCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__DELETE_CHANNEL_METADATA.equalsIgnoreCase(requestName)) {
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

		DataCastService dataCastService = getService();

		boolean succeed = false;

		if (hasChannelId) {
			String channelId = request.getStringParameter("channel_id");
			succeed = dataCastService.deleteChannelMetadataById(channelId);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			succeed = dataCastService.deleteChannelMetadataByName(name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
