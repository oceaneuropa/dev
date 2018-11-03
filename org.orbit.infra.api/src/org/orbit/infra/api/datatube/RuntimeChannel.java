package org.orbit.infra.api.datatube;

public interface RuntimeChannel {

	DataTubeClient getDataTubeClient();

	String getDataCastId();

	void setDataCastId(String dataCastId);

	String getDataTubeId();

	void setDataTubeId(String dataTubeId);

	String getChannelId();

	void setChannelId(String channelId);

	String getName();

	void setName(String name);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
