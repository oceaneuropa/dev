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
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.model.AccessConfig;
import org.origin.common.model.AccountConfig;
import org.origin.common.model.RoleConfig;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class SetChannelMetadataAccountConfigsCommand extends AbstractInfraCommand<DataCastService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.datacast.SetChannelMetadataAccountConfigsCommand";

	public SetChannelMetadataAccountConfigsCommand() {
		super(DataCastService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.DATACAST__SET_CHANNEL_METADATA_ACCOUNT_CONFIGS.equalsIgnoreCase(requestName)) {
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

		if (!request.hasParameter("account_configs")) {
			ErrorDTO error = new ErrorDTO("'account_configs' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String accountConfigsString = request.getStringParameter("account_configs");
		List<AccountConfig> newAccountConfigs = ModelConverter.DATA_CAST.toAccountConfigs(accountConfigsString);

		boolean appendAccountConfigs = request.getBooleanParameter("append_account_configs");
		boolean appendAccountConfig = request.getBooleanParameter("append_account_config");

		boolean succeed = false;

		DataCastService service = getService();
		ChannelMetadata channelMetadata = service.getChannelMetadataById(channelId);

		if (channelMetadata != null) {
			List<AccountConfig> existingAccountConfigs = channelMetadata.getAccountConfigs();
			List<AccountConfig> updatedAccountConfigs = getUpdatedAccountConfigs(newAccountConfigs, existingAccountConfigs, appendAccountConfigs, appendAccountConfig);

			succeed = service.updateChannelMetadataAccountConfigsById(channelId, updatedAccountConfigs);
			if (succeed) {
				channelMetadata.setAccountConfigs(updatedAccountConfigs);
			}

			Map<String, Object> properties = channelMetadata.getProperties();
			succeed = service.updateChannelMetadataPropertiesById(channelId, properties);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

	/**
	 * 
	 * @param newAccountConfigs
	 * @param existingAccountConfigs
	 * @param appendAccountConfigs
	 * @param appendAccountConfig
	 * @return
	 */
	public List<AccountConfig> getUpdatedAccountConfigs(List<AccountConfig> newAccountConfigs, List<AccountConfig> existingAccountConfigs, boolean appendAccountConfigs, boolean appendAccountConfig) {
		if (newAccountConfigs == null) {
			newAccountConfigs = new ArrayList<AccountConfig>();
		}
		if (existingAccountConfigs == null) {
			existingAccountConfigs = new ArrayList<AccountConfig>();
		}

		List<AccountConfig> updatedAccountConfigs = null;

		if (appendAccountConfigs) {
			updatedAccountConfigs = new ArrayList<AccountConfig>();

			Map<String, AccountConfig> newAccountIdToAccountConfigMap = new HashMap<String, AccountConfig>();
			for (AccountConfig accountConfig : newAccountConfigs) {
				newAccountIdToAccountConfigMap.put(accountConfig.getElement(), accountConfig);
			}

			Map<String, AccountConfig> existingAccountIdToAccountConfigMap = new HashMap<String, AccountConfig>();
			for (AccountConfig existingAccountConfig : existingAccountConfigs) {
				existingAccountIdToAccountConfigMap.put(existingAccountConfig.getElement(), existingAccountConfig);
			}

			for (AccountConfig existingAccountConfig : existingAccountConfigs) {
				String accountId = existingAccountConfig.getElement();
				AccountConfig newAccountConfig = newAccountIdToAccountConfigMap.get(accountId);
				if (newAccountConfig != null) {
					// new account config found with same accountId for the current existing account config
					if (appendAccountConfig) {
						// append
						AccessConfig newAccessConfig = newAccountConfig.getAccessConfig(AccessConfig.EMPTY);
						RoleConfig newRoleConfig = newAccountConfig.getRoleConfig(RoleConfig.EMPTY);

						AccessConfig existingAccessConfig = existingAccountConfig.getAccessConfig(AccessConfig.EMPTY);
						RoleConfig existingRoleConfig = existingAccountConfig.getRoleConfig(RoleConfig.EMPTY);

						existingAccessConfig.append(newAccessConfig);
						existingRoleConfig.append(newRoleConfig);

						updatedAccountConfigs.add(existingAccountConfig);

					} else {
						// set
						updatedAccountConfigs.add(newAccountConfig);
					}
				} else {
					// no new account config with same accountId for the current existing account config
					updatedAccountConfigs.add(existingAccountConfig);
				}
			}

			List<AccountConfig> accountConfigsToAdd = new ArrayList<AccountConfig>();
			for (AccountConfig newAccountConfig : newAccountConfigs) {
				if (!existingAccountIdToAccountConfigMap.containsKey(newAccountConfig.getElement())) {
					accountConfigsToAdd.add(newAccountConfig);
				}
			}
			if (!accountConfigsToAdd.isEmpty()) {
				updatedAccountConfigs.addAll(accountConfigsToAdd);
			}

		} else {
			updatedAccountConfigs = newAccountConfigs;
		}

		return updatedAccountConfigs;
	}

}
