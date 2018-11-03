package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class SetChannelMetadataPropertiesCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.SetChannelMetadataPropertiesCommand";

	public SetChannelMetadataPropertiesCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__SET_CHANNEL_METADATA_PROPERTIES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		// boolean hasChannelId = request.hasParameter("channel_id");
		// boolean hasName = request.hasParameter("name");
		// if (!hasChannelId && !hasName) {
		// ErrorDTO error = new ErrorDTO("'channel_id' parameter or 'name' parameter is not set.");
		// return Response.status(Status.BAD_REQUEST).entity(error).build();
		// }

		String channelId = request.getStringParameter("channel_id");
		if (channelId == null || channelId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'channel_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}
		if (properties == null || properties.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'properties' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataCastService service = getService();

		boolean succeed = false;
		ChannelMetadata channelMetadata = service.getChannelMetadataById(channelId);
		if (channelMetadata != null) {
			Map<String, Object> existingProperties = channelMetadata.getProperties();
			existingProperties.putAll(properties);

			succeed = service.updateChannelMetadataPropertiesById(channelId, existingProperties);
		}

		// else if (hasName) {
		// String name = request.getStringParameter("name");
		// channelMetadata = service.getChannelMetadataByName(name);
		//
		// if (channelMetadata != null) {
		// Map<String, Object> existingProperties = channelMetadata.getProperties();
		// existingProperties.putAll(properties);
		//
		// succeed = service.updateChannelMetadataPropertiesByName(name, existingProperties);
		// }
		// }

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
