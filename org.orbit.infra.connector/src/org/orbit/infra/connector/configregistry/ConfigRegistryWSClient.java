package org.orbit.infra.connector.configregistry;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

/*
 * Config Registry Client
 * 
 * {contextRoot} example:
 * /orbit/v1/configregistry/
 * 
 */
public class ConfigRegistryWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public ConfigRegistryWSClient(WSClientConfiguration config) {
		super(config);
	}

}
