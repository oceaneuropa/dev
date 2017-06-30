package org.orbit.component.connector.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier3.transferagent.NodeConfig;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.request.CreateNodeRequest;
import org.orbit.component.api.tier3.transferagent.request.DeleteNodeRequest;
import org.orbit.component.api.tier3.transferagent.request.StopNodeRequest;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.StringUtil;

public class TransferAgentImpl implements TransferAgent {

	protected Map<String, Object> properties;
	protected TransferAgentWSClient client;

	public TransferAgentImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Update properties. Re-initiate web service client if host URL or context root is changed.
	 * 
	 * @param properties
	 */
	@Override
	public void update(Map<String, Object> properties) {
		String oldUrl = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);

		boolean reinitClient = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinitClient = true;
		}
		if (reinitClient) {
			initClient();
		}
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new TransferAgentWSClient(clientConfig);
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public NodeConfig[] getNodeConfigs() throws ClientException {
		return null;
	}

	@Override
	public NodeConfig createNode(CreateNodeRequest request) throws ClientException {
		org.orbit.component.model.tier3.transferagent.request.CreateNodeRequest createNodeRequest = new org.orbit.component.model.tier3.transferagent.request.CreateNodeRequest();
		createNodeRequest.setNodeName("node1");
		this.client.sendRequest(createNodeRequest);
		return null;
	}

	@Override
	public boolean deleteNode(DeleteNodeRequest request) throws ClientException {
		return false;
	}

	@Override
	public Map<String, Object> getNodeProperties(String nodeId) {
		return null;
	}

	@Override
	public boolean setNodeProperty(String nodeId, String name, Object value) {
		return false;
	}

	@Override
	public Object getNodeProperty(String nodeId, String name) {
		return null;
	}

	@Override
	public boolean removeNodeProperty(String nodeId, String name) {
		return false;
	}

	@Override
	public boolean startNode(String nodeId) throws ClientException {
		return false;
	}

	@Override
	public boolean stopNode(StopNodeRequest request) throws ClientException {
		return false;
	}

	@Override
	public boolean isNodeRunning(String nodeId) throws ClientException {
		return false;
	}

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
		return ClientConfiguration.get(url, contextRoot, null, null);
	}

}
