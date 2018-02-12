package org.orbit.platform.connector.gaia;

import java.util.Map;

import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.gaia.GAIAClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class GAIAClientImpl extends ServiceClientImpl<GAIAClient, GAIAWSClient> implements GAIAClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public GAIAClientImpl(ServiceConnector<GAIAClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected GAIAWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(PlatformConstants.REALM);
		String username = (String) properties.get(PlatformConstants.USERNAME);
		String fullUrl = (String) properties.get(PlatformConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new GAIAWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(PlatformConstants.URL);
		return fullUrl;
	}

}
