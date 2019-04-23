package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.datacast.ChannelMetadataDTO;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.model.AccountConfig;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.util.NameUtil;

public class CreateChannelMetadataCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

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
		List<AccountConfig> accountConfigs = null;
		if (request.hasParameter("account_configs")) {
			String accountConfigsString = request.getStringParameter("account_configs");
			accountConfigs = RuntimeModelConverter.DATA_CAST.toAccountConfigs(accountConfigsString);
		}
		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}

		boolean generateUniqueName = false;
		if (request.hasParameter("generate_unique_name")) {
			generateUniqueName = true;
		}

		// if (dataTubeId == null || dataTubeId.isEmpty()) {
		// ErrorDTO error = new ErrorDTO("'data_tube_id' parameter is empty.");
		// return Response.status(Status.BAD_REQUEST).entity(error).build();
		// }
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'name' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		DataCastService dataCastService = getService();

		if (generateUniqueName) {
			name = NameUtil.getUniqueName(dataCastService, name);
		}

		if (dataCastService.channelMetadataExistsByName(name)) {
			ErrorDTO error = new ErrorDTO("Channel with name '" + name + "' already exists.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ChannelMetadata channelMetadata = dataCastService.createChannelMetadata(dataTubeId, name, accessType, accessCode, ownerAccountId, accountConfigs, properties);
		if (channelMetadata == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "ChannelMetadata cannot be created");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ChannelMetadataDTO channelMetadataDTO = RuntimeModelConverter.DATA_CAST.toDTO(channelMetadata);
		return Response.ok().entity(channelMetadataDTO).build();
	}

}

// List<String> accountIds = null;
// if (request.hasParameter("account_ids")) {
// accountIds = (List<String>) request.getListParameter("account_ids");
// }
