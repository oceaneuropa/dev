package org.orbit.infra.runtime.datacast.service;

import java.util.List;

public interface DataTubeMetadata {

	String getDataCastId();

	void setDataCastId(String dataCastId);

	String getDataTubeId();

	void setDataTubeId(String dataTubeId);

	String getDataTubeURL();

	void setDataTubeURL(String dataTubeUrl);

	List<ChannelMetadata> getChannels();

	void setChannels(List<ChannelMetadata> channels);

}
