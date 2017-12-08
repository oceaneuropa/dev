package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public interface TransferAgent {

	String getName();

	String getURL();

	Map<Object, Object> getProperties();

	void update(Map<Object, Object> properties);

	boolean ping();

	Response sendRequest(Request request) throws ClientException;

}

// public NodeConfig[] getNodeConfigs() throws ClientException;
//
// public NodeConfig createNode(CreateNodeRequest request) throws ClientException;
//
// public boolean deleteNode(DeleteNodeRequest request) throws ClientException;
//
// public Map<String, Object> getNodeProperties(String nodeId);
//
// public boolean setNodeProperty(String nodeId, String name, Object value);
//
// public Object getNodeProperty(String nodeId, String name);
//
// public boolean removeNodeProperty(String nodeId, String name);
// public NodeAgent getNodeAgent(String nodeId);

// boolean startNode(String nodeId) throws ClientException;
//
// boolean stopNode(StopNodeRequest request) throws ClientException;
//
// boolean isNodeRunning(String nodeId) throws ClientException;

// TransferAgentResponseConverter getResponseConverter();
