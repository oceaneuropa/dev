package org.orbit.component.connector.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.util.StringUtil;

public class TransferAgentImpl implements TransferAgent {

	protected Map<Object, Object> properties;
	protected TransferAgentWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public TransferAgentImpl(Map<Object, Object> properties) {
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
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Update properties. Re-initiate web service client if host URL or context root is changed.
	 * 
	 * @param properties
	 */
	@Override
	public void update(Map<Object, Object> properties) {
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

	private Map<Object, Object> checkProperties(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		return properties;
	}

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new TransferAgentWSClient(clientConfig);
	}

	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<Object, Object> properties) {
		String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
		return ClientConfiguration.get(url, contextRoot, null, null);
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return this.client.sendRequest(request);
	}

}
