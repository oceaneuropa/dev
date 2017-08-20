package org.orbit.component.connector.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentResponseConverter;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;
import org.origin.common.util.StringUtil;

public class TransferAgentImpl implements TransferAgent {

	protected Map<String, Object> properties;
	protected TransferAgentWSClient client;
	protected TransferAgentResponseConverterImpl responseConverter;

	/**
	 * 
	 * @param properties
	 */
	public TransferAgentImpl(Map<String, Object> properties) {
		this.responseConverter = new TransferAgentResponseConverterImpl();
		this.properties = checkProperties(properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
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
		String oldUrl = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);

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
	public Responses sendRequest(Request request) throws ClientException {
		return this.client.sendRequest(request);
	}

	@Override
	public TransferAgentResponseConverter getResponseConverter() {
		return this.responseConverter;
	}

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

// @Override
// public boolean startNode(String nodeId) throws ClientException {
// return false;
// }
//
// @Override
// public boolean stopNode(StopNodeRequest request) throws ClientException {
// return false;
// }
//
// @Override
// public boolean isNodeRunning(String nodeId) throws ClientException {
// return false;
// }