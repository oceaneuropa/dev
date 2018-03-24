package org.orbit.component.api.tier3.nodecontrol.other;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.origin.common.rest.client.ClientException;

public interface TransferAgentConnector {

	NodeControlClient getService(Map<String, Object> properties) throws ClientException;

	boolean update(NodeControlClient agent, Map<String, Object> properties) throws ClientException;

	boolean close(NodeControlClient agent) throws ClientException;

}
