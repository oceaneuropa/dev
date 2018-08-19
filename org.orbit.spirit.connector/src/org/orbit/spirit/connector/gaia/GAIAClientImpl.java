package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.spirit.api.gaia.GAIAClient;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;

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
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new GAIAWSClient(config);
	}

	@Override
	public String getURL() {
		String url = (String) this.properties.get(WSClientConstants.URL);
		return url;
	}

}
