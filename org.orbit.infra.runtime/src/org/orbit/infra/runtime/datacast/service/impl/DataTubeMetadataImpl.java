package org.orbit.infra.runtime.datacast.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataTubeMetadata;

public class DataTubeMetadataImpl implements DataTubeMetadata {

	protected String dataCastId;
	protected String dataTubeId;
	protected String dataTubeURL;
	protected List<ChannelMetadata> channels;

	@Override
	public String getDataCastId() {
		return this.dataCastId;
	}

	@Override
	public void setDataCastId(String dataCastId) {
		this.dataCastId = dataCastId;
	}

	@Override
	public String getDataTubeId() {
		return this.dataTubeId;
	}

	@Override
	public void setDataTubeId(String dataTubeId) {
		this.dataTubeId = dataTubeId;
	}

	@Override
	public String getDataTubeURL() {
		return this.dataTubeURL;
	}

	@Override
	public void setDataTubeURL(String dataTubeURL) {
		this.dataTubeURL = dataTubeURL;
	}

	@Override
	public synchronized List<ChannelMetadata> getChannels() {
		if (this.channels == null) {
			this.channels = new ArrayList<ChannelMetadata>();
		}
		return this.channels;
	}

	@Override
	public synchronized void setChannels(List<ChannelMetadata> channels) {
		this.channels = channels;
	}

}
