package org.orbit.infra.api.datacast;

import java.util.List;
import java.util.Map;

public interface ChannelMetadata {

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
	 * If 'public', anyone can join the channel.
	 * 
	 * If 'private', only invited users (by accountId) can join the channel.
	 * 
	 * @return
	 */
	String getAccessType();

	void setAccessType(String accessType);

	/**
	 * like the password in diablo2 game.
	 * 
	 * @return
	 */
	String getAccessCode();

	void setAccessCode(String accessCode);

	String getOwnerAccountId();

	void setOwnerAccountId(String accountId);

	List<String> getAccountIds();

	Map<String, Object> getProperties();

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
