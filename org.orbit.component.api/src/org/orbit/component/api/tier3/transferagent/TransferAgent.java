package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

import org.orbit.component.api.tier3.transferagent.request.CreateNodeRequest;
import org.orbit.component.api.tier3.transferagent.request.DeleteNodeRequest;
import org.orbit.component.api.tier3.transferagent.request.StopNodeRequest;
import org.origin.common.rest.client.ClientException;

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
	public boolean ping();

	// ------------------------------------------------------
	// Node management
	// ------------------------------------------------------
	public NodeConfig[] getNodeConfigs() throws ClientException;

	public NodeConfig createNode(CreateNodeRequest request) throws ClientException;

	public boolean deleteNode(DeleteNodeRequest request) throws ClientException;

	public Map<String, Object> getNodeProperties(String nodeId);

	public boolean setNodeProperty(String nodeId, String name, Object value);

	public Object getNodeProperty(String nodeId, String name);

	public boolean removeNodeProperty(String nodeId, String name);

	// ------------------------------------------------------
	// Node life cycle
	// ------------------------------------------------------
	public boolean startNode(String nodeId) throws ClientException;

	public boolean stopNode(StopNodeRequest request) throws ClientException;

	public boolean isNodeRunning(String nodeId) throws ClientException;

	// public NodeAgent getNodeAgent(String nodeId);

}
