package org.orbit.component.connector.tier1.session;

import java.util.Map;

import org.orbit.component.api.tier1.session.OAuth2Client;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class OAuth2ClientImpl extends ServiceClientImpl<OAuth2Client, OAuth2WSClient> implements OAuth2Client {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public OAuth2ClientImpl(ServiceConnector<OAuth2Client> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected OAuth2WSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String url = (String) properties.get(OrbitConstants.URL);
		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, url);

		return new OAuth2WSClient(clientConfig);
	}

}

// protected Map<String, Object> properties;
// protected OAuth2WSClient client;

// protected String loadBalanceId;
// protected Properties loadBalanceProperties;

// @Override
// public Map<String, Object> getProperties() {
// return this.properties;
// }
//
// @Override
// public void update(Map<String, Object> properties) {
// this.properties = properties;
// this.loadBalanceId = getLoadBalanceId(this.properties);
// initClient();
// }

// /**
// * Generate a unique id, for load balancing purpose, based on indexing properties for a user registry.
// *
// * @param properties
// * @return
// */
// protected String getLoadBalanceId(Map<String, Object> properties) {
// String userRegistryName = (String) properties.get(OrbitConstants.OAUTH2_NAME);
// String url = (String) properties.get(OrbitConstants.OAUTH2_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.OAUTH2_CONTEXT_ROOT);
// String key = url + "::" + contextRoot + "::" + userRegistryName;
// return key;
// }
//
// /**
// * Get user registry client configuration.
// *
// * @param properties
// * @return
// */
// protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
// String realm = (String) properties.get(OrbitConstants.REALM);
// String username = (String) properties.get(OrbitConstants.USERNAME);
// String url = (String) properties.get(OrbitConstants.OAUTH2_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.OAUTH2_CONTEXT_ROOT);
// return ClientConfiguration.create(realm, username, url, contextRoot);
// }
