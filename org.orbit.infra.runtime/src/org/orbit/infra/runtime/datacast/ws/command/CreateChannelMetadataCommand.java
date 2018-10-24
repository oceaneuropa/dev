package org.orbit.infra.runtime.datacast.ws.command;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractDataCastCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateChannelMetadataCommand extends AbstractDataCastCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.CreateChannelMetadataCommand";

	public CreateChannelMetadataCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__CREATE_CHANNEL_METADATA.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String dataTubeId = request.getStringParameter("data_tube_id");
		String name = request.getStringParameter("name");
		String accessType = request.getStringParameter("access_type");
		String accessCode = request.getStringParameter("access_code");
		String ownerAccountId = request.getStringParameter("owner_account_id");
		List<String> accountIds = null;
		if (request.hasParameter("account_ids")) {
			accountIds = (List<String>) request.getListParameter("account_ids");
		}
		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}
		boolean generateUniqueName = false;
		if (request.hasParameter("generate_unique_name")) {
			generateUniqueName = true;
		}

		if (dataTubeId == null || dataTubeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'data_tube_id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'name' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataCastService dataCastService = getService();

		if (generateUniqueName) {
			String defaultName = name;
			boolean isLastSegmentNumber = false;
			int lastNumber = -1;
			int index = defaultName.lastIndexOf("_");
			if (index > 0 && index < defaultName.length() - 1) {
				String lastSegment = defaultName.substring(index + 1);
				try {
					lastNumber = Integer.parseInt(lastSegment);
					isLastSegmentNumber = true;
				} catch (Exception e) {
				}
			}

			int appendNumber = 1;
			if (isLastSegmentNumber) {
				appendNumber = lastNumber + 1;
			}
			while (dataCastService.channelMetadataExistsByName(name)) {
				String nextName = null;
				if (isLastSegmentNumber) {
					nextName = defaultName.substring(0, index) + "_" + appendNumber;
				} else {
					nextName = defaultName + "_" + appendNumber;
				}
				name = nextName;
				appendNumber++;
			}

		} else {
			if (dataCastService.channelMetadataExistsByName(name)) {
				ErrorDTO error = new ErrorDTO("Channel with name '" + name + "' already exists.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		ChannelMetadata channelMetadata = dataCastService.createChannelMetadata(dataTubeId, name, accessType, accessCode, ownerAccountId, accountIds, properties);
		if (channelMetadata == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "ChannelMetadata cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ChannelMetadataDTO channelMetadataDTO = ModelConverter.DATA_CAST.toDTO(channelMetadata);
		return Response.ok().entity(channelMetadataDTO).build();
	}

}
