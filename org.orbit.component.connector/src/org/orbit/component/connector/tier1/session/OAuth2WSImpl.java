package org.orbit.component.connector.tier1.session;

import java.util.Map;
import java.util.Properties;

import org.orbit.component.api.tier1.session.OAuth2;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;

public class OAuth2WSImpl implements OAuth2 {

	protected Map<String, Object> properties;
	protected OAuth2WSClient client;

	protected String loadBalanceId;
	protected Properties loadBalanceProperties;

	/**
	 * 
	 * @param properties
	 */
	public OAuth2WSImpl(Map<String, Object> properties) {
		this.properties = properties;
		this.loadBalanceId = getLoadBalanceId(this.properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new OAuth2WSClient(clientConfig);
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = properties;
		this.loadBalanceId = getLoadBalanceId(this.properties);
		initClient();
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.OAUTH2_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.OAUTH2_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.OAUTH2_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	// ------------------------------------------------------------------------------------------------
	// Web service client methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Generate a unique id, for load balancing purpose, based on indexing properties for a user registry.
	 * 
	 * @param properties
	 * @return
	 */
	protected String getLoadBalanceId(Map<String, Object> properties) {
		String userRegistryName = (String) properties.get(OrbitConstants.OAUTH2_NAME);
		String url = (String) properties.get(OrbitConstants.OAUTH2_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.OAUTH2_CONTEXT_ROOT);
		String key = url + "::" + contextRoot + "::" + userRegistryName;
		return key;
	}

	/**
	 * Get user registry client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String url = (String) properties.get(OrbitConstants.OAUTH2_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.OAUTH2_CONTEXT_ROOT);
		return ClientConfiguration.create(realm, username, url, contextRoot);
	}

}
