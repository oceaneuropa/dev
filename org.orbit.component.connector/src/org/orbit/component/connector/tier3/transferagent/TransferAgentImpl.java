package org.orbit.component.connector.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.util.StringUtil;

public class TransferAgentImpl implements TransferAgent {

	protected TransferAgentConnector connector;
	protected Map<String, Object> properties;
	protected TransferAgentWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public TransferAgentImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public TransferAgentConnector getConnector() {
		return this.connector;
	}

	public void setConnector(TransferAgentConnector connector) {
		this.connector = connector;
	}

	public boolean close() throws ClientException {
		if (this.connector != null) {
			return this.connector.close(this);
		}
		return false;
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
		// String hostURL = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		// String contextRoot = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
		// return hostURL + contextRoot;
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
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
		// String oldUrl = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		// String oldContextRoot = (String) this.properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);

		String oldFullUrl = (String) this.properties.get(OrbitConstants.URL);
		// String oldToken = (String) this.properties.get(OrbitConstants.ORBIT_TOKEN);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		// String newUrl = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
		// String newContextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
		String newFullUrl = (String) properties.get(OrbitConstants.URL);
		// String newToken = (String) properties.get(OrbitConstants.ORBIT_TOKEN);

		boolean reinitClient = false;
		// if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
		// reinitClient = true;
		// }
		if (!StringUtil.equals(oldFullUrl, newFullUrl)) {
			reinitClient = true;
		}
		if (reinitClient) {
			initClient();
		}
	}

	protected void initClient() {
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new TransferAgentWSClient(config);
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws ClientException {
		return this.client.echo(message);
	}

	@Override
	public String level(String level1, String level2, String message1, String message2) throws ClientException {
		return this.client.level(level1, level2, message1, message2);
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return this.client.sendRequest(request);
	}

}

// String url = (String) properties.get(OrbitConstants.TRANSFER_AGENT_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT);
// return ClientConfiguration.get(url, contextRoot, null, null);
