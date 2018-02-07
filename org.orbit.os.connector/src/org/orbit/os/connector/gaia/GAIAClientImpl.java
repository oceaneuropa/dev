package org.orbit.os.connector.gaia;

import java.util.Map;

import org.orbit.os.api.OSConstants;
import org.orbit.os.api.gaia.GAIAClient;
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
		String realm = (String) properties.get(OSConstants.REALM);
		String username = (String) properties.get(OSConstants.USERNAME);
		String fullUrl = (String) properties.get(OSConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new GAIAWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(OSConstants.URL);
		return fullUrl;
	}

}
