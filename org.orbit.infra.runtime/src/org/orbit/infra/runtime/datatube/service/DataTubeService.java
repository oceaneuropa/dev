package org.orbit.infra.runtime.datatube.service;

import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.service.WebServiceAware;

public interface DataTubeService extends WebServiceAware, EditPoliciesAwareService {

	String getWebSocketHttpPort();

	Channel[] getChannels();

	boolean channelExists(String channelId);

	Channel getChannel(String channelId);

	Channel removeChannel(String channelId);

	void dispose();

}
