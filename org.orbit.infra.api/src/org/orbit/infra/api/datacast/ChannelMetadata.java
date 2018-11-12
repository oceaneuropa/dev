package org.orbit.infra.api.datacast;

import java.util.List;
import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.AccountConfig;
import org.origin.common.model.DateRecordAware;
import org.origin.common.model.TransientPropertyAware;

public interface ChannelMetadata extends DateRecordAware<Long>, TransientPropertyAware, IAdaptable {

	DataCastClient getDataCastClient();

	String getDataCastId();

	void setDataCastId(String dataCastId);

	String getDataTubeId();

	void setDataTubeId(String dataTubeId);

	String getChannelId();

	void setChannelId(String channelId);

	String getName();

	void setName(String name);

	/**
	 * Values are: public, private.
	 * 
	 * - If 'public', anyone can join.
	 * 
	 * - If 'private', only the owner and the invited users can join.
	 * 
	 * @return
	 */
	String getAccessType();

	void setAccessType(String accessType);

	/**
	 * Like the password for a diablo2 game.
	 * 
	 * - If 'public', anyone has the password can join.
	 * 
	 * - If 'private', only the owner and the invited users who have the password can join.
	 * 
	 * @return
	 */
	String getAccessCode();

	void setAccessCode(String accessCode);

	String getOwnerAccountId();

	void setOwnerAccountId(String accountId);

	List<AccountConfig> getAccountConfigs();

	Map<String, Object> getProperties();

	ChannelStatus getStatus();

}
