package org.orbit.component.api.tier3.nodecontrol.other;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.origin.common.rest.client.ClientException;

public interface TransferAgentConnector {

	NodeManagementClient getService(Map<String, Object> properties) throws ClientException;

	boolean update(NodeManagementClient agent, Map<String, Object> properties) throws ClientException;

	boolean close(NodeManagementClient agent) throws ClientException;

}
