package org.orbit.infra.runtime.datacast.ws.command.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.model.AccountConfig;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class RemoveChannelMetadataAccountConfigsCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.RemoveChannelMetadataAccountConfigsCommand";

	public RemoveChannelMetadataAccountConfigsCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__REMOVE_CHANNEL_METADATA_ACCOUNT_CONFIGS.equalsIgnoreCase(requestName)) {
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

		List<String> accountIds = null;
		if (request.hasParameter("account_ids")) {
			accountIds = (List<String>) request.getListParameter("account_ids");
		}
		if (accountIds == null || accountIds.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'account_ids' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;

		DataCastService service = getService();
		ChannelMetadata channelMetadata = service.getChannelMetadataById(channelId);

		if (channelMetadata != null) {
			List<AccountConfig> accountConfigs = channelMetadata.getAccountConfigs();

			List<AccountConfig> updatedAccountConfigs = new ArrayList<AccountConfig>();
			for (AccountConfig accountConfig : accountConfigs) {
				String accountId = accountConfig.getElement();
				if (!accountIds.contains(accountId)) {
					updatedAccountConfigs.add(accountConfig);
				}
			}

			succeed = service.updateChannelMetadataAccountConfigsById(channelId, updatedAccountConfigs);
			if (succeed) {
				channelMetadata.setAccountConfigs(updatedAccountConfigs);
			}
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
