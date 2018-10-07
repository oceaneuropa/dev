package org.orbit.infra.runtime.datatube.service;

import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface DataTubeService extends WebServiceAware, PropertiesAware, EditPoliciesAwareService {

	String getDataCastId();
	
	String getDataTubeId();

	String getWebSocketHttpPort();

	Channel[] getChannels();

	boolean channelExists(String channelId);

	Channel getChannel(String channelId);

	Channel removeChannel(String channelId);

	void dispose();

}
