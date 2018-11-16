package org.orbit.infra.runtime.datatube.service;

import java.io.IOException;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.DateRecordAware;
import org.origin.common.model.TransientPropertyAware;

public interface RuntimeChannel extends MessageListenerSupport, DateRecordAware<Long>, TransientPropertyAware, IAdaptable {

	ChannelMetadata getChannelMetadata();

	void setChannelMetadata(ChannelMetadata channelMetadata);

	int onMessage(String senderId, String message) throws IOException;

	void dispose();

}
