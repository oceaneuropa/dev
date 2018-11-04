package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.ChannelStatus;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ClearChannelMetadataStatusCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.ClearChannelMetadataStatusCommand";

	public ClearChannelMetadataStatusCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__CLEAR_CHANNEL_METADATA_STATUS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String channelId = request.getStringParameter("channel_id");
		if (channelId == null || channelId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'channel_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Integer statusValue = null;
		if (request.hasParameter("status")) {
			statusValue = request.getIntegerParameter("status");
		}
		if (statusValue == null) {
			ErrorDTO error = new ErrorDTO("'status' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		ChannelStatus status = new ChannelStatus(statusValue);

		boolean succeed = false;

		DataCastService service = getService();
		ChannelMetadata channelMetadata = service.getChannelMetadataById(channelId);
		if (channelMetadata != null) {
			channelMetadata.clearStatus(status);

			Map<String, Object> properties = channelMetadata.getProperties();
			succeed = service.updateChannelMetadataPropertiesById(channelId, properties);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}

// boolean hasChannelId = request.hasParameter("channel_id");
// boolean hasName = request.hasParameter("name");
// if (!hasChannelId && !hasName) {
// ErrorDTO error = new ErrorDTO("'channel_id' parameter or 'name' parameter is not set.");
// return Response.status(Status.BAD_REQUEST).entity(error).build();
// }

// else if (hasName) {
// String name = request.getStringParameter("name");
// channelMetadata = service.getChannelMetadataByName(name);
//
// if (channelMetadata != null) {
// Map<String, Object> existingProperties = channelMetadata.getProperties();
// for (String propName : propertyNames) {
// existingProperties.remove(propName);
// }
//
// succeed = service.updateChannelMetadataPropertiesByName(name, existingProperties);
// }
// }
