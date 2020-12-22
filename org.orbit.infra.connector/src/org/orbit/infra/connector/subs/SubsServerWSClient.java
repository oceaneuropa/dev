package org.orbit.infra.connector.subs;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

/*-
 * SubsServer Client
 * 
 * {contextRoot} example:
 * /orbit/v1/subs/
 * 
 */
public class SubsServerWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public SubsServerWSClient(WSClientConfiguration config) {
		super(config);
	}

}
