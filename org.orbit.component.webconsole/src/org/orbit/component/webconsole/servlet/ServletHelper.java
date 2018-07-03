package org.orbit.component.webconsole.servlet;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.origin.common.rest.client.ClientException;

public class ServletHelper {

	public static ServletHelper INSTANCE = new ServletHelper();

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String checkMessage(String message) {
		if (message == null) {
			message = "";
		}
		if (!message.isEmpty()) {
			message += " ";
		}
		return message;
	}

	/**
	 * 
	 * @param platformConfig
	 * @return
	 * @throws ClientException
	 */
	public NodeControlClient getNodeControlClient(PlatformConfig platformConfig) throws ClientException {
		NodeControlClient nodeControlClient = null;
		if (platformConfig != null) {
			String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
			nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
		}
		return nodeControlClient;
	}

}
