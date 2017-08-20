package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

public interface TransferAgent {

	/**
	 * Get service name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get service URL.
	 * 
	 * @return
	 */
	String getURL();

	/**
	 * Get configuration properties.
	 * 
	 * @return
	 */
	Map<String, Object> getProperties();

	/**
	 * Update configuration properties.
	 * 
	 * @param properties
	 */
	void update(Map<String, Object> properties);

	/**
	 * Ping the service.
	 * 
	 * @return
	 */
	boolean ping();

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	Responses sendRequest(Request request) throws ClientException;

	/**
	 * 
	 * @return
	 */
	TransferAgentResponseConverter getResponseConverter();

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