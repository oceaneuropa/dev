package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.spirit.api.Constants;
import org.orbit.spirit.api.gaia.GAIAClient;
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
		String realm = (String) properties.get(Constants.REALM);
		String username = (String) properties.get(Constants.USERNAME);
		String fullUrl = (String) properties.get(Constants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new GAIAWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(Constants.URL);
		return fullUrl;
	}

}
