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

public class UpdateChannelMetadataCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.UpdateChannelMetadataCommand";

	public UpdateChannelMetadataCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__UPDATE_CHANNEL_METADATA.equalsIgnoreCase(requestName)) {
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

		boolean hasSucceed = false;
		boolean hasFailed = false;

		DataCastService dataCastService = getService();

		// 1. dataTubeId
		boolean updateDataTubeId = request.getBooleanParameter("update_data_tube_id");
		if (updateDataTubeId) {
			if (!request.hasParameter("data_tube_id")) {
				ErrorDTO error = new ErrorDTO("'data_tube_id' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String dataTubeId = request.getStringParameter("data_tube_id");
			boolean currSucceed = dataCastService.updateChannelMetadataDataTubeId(channelId, dataTubeId);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// 2. name
		boolean updateName = request.getBooleanParameter("update_name");
		if (updateName) {
			if (!request.hasParameter("name")) {
				ErrorDTO error = new ErrorDTO("'name' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String name = request.getStringParameter("name");
			boolean currSucceed = dataCastService.updateChannelMetadataName(channelId, name);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// 3. accessType (public or private)
		// - public --- anyone can join
		// - private --- only the owner and the invited users can join
		boolean updateAccessType = request.getBooleanParameter("update_access_type");
		if (updateAccessType) {
			if (!request.hasParameter("access_type")) {
				ErrorDTO error = new ErrorDTO("'access_type' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String accessType = request.getStringParameter("access_type");
			boolean currSucceed = dataCastService.updateChannelMetadataAccessType(channelId, accessType);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// 4. accessCode
		boolean updateAccessCode = request.getBooleanParameter("update_access_code");
		if (updateAccessCode) {
			if (!request.hasParameter("access_code")) {
				ErrorDTO error = new ErrorDTO("'access_code' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String accessCode = request.getStringParameter("access_code");
			boolean currSucceed = dataCastService.updateChannelMetadataAccessCode(channelId, accessCode);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		// 5. ownerAccountId
		boolean updateOwnerAccountId = request.getBooleanParameter("update_owner_account_id");
		if (updateOwnerAccountId) {
			if (!request.hasParameter("owner_account_id")) {
				ErrorDTO error = new ErrorDTO("'owner_account_id' parameter is not set.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			String ownerAccountId = request.getStringParameter("owner_account_id");
			boolean currSucceed = dataCastService.updateChannelMetadataOwnerAccountId(channelId, ownerAccountId);
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
