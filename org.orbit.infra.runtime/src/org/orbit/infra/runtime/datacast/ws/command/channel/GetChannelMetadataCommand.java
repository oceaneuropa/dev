package org.orbit.infra.runtime.datacast.ws.command.channel;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetChannelMetadataCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.GetChannelMetadataCommand";

	public GetChannelMetadataCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__GET_CHANNEL_METADATA.equalsIgnoreCase(requestName)) {
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

		DataCastService service = getService();

		ChannelMetadata channelMetadata = null;
		if (hasChannelId) {
			String channelId = request.getStringParameter("channel_id");
			channelMetadata = service.getChannelMetadataById(channelId);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			channelMetadata = service.getChannelMetadataByName(name);
		}

		if (channelMetadata == null) {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "ChannelMetadata cannot be created");
			// return Response.status(Status.BAD_REQUEST).entity(error).build();
			return Response.ok().build();
		}

		ChannelMetadataDTO channelMetadataDTO = ModelConverter.DATA_CAST.toDTO(channelMetadata);
		return Response.ok().entity(channelMetadataDTO).build();
	}

}
