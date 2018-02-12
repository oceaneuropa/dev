package org.orbit.platform.connector.platform;

import java.util.Map;

import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.platform.PlatformClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;

public class PlatformClientImpl extends ServiceClientImpl<PlatformClient, PlatformWSClient> implements PlatformClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public PlatformClientImpl(ServiceConnector<PlatformClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected PlatformWSClient createWSClient(Map<String, Object> properties) {
		String realm = (String) properties.get(PlatformConstants.REALM);
		String username = (String) properties.get(PlatformConstants.USERNAME);
		String fullUrl = (String) properties.get(PlatformConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		return new PlatformWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) this.properties.get(PlatformConstants.URL);
		return fullUrl;
	}

}
