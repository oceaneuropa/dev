package org.orbit.infra.api.datatube;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.DateRecordAware;
import org.origin.common.model.TransientPropertyAware;

public interface RuntimeChannel extends DateRecordAware<Long>, TransientPropertyAware, IAdaptable {

	DataTubeClient getDataTubeClient();

	String getDataCastId();

	void setDataCastId(String dataCastId);

	String getDataTubeId();

	void setDataTubeId(String dataTubeId);

	String getChannelId();

	void setChannelId(String channelId);

	String getName();

	void setName(String name);

}
