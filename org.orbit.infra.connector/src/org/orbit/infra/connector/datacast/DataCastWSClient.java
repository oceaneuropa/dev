package org.orbit.infra.connector.datacast;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

/*
 * DataCast web service client.
 * 
 * {contextRoot} example:
 * /orbit/v1/datacast
 * 
 */
public class DataCastWSClient extends WSClient {

	public DataCastWSClient(WSClientConfiguration config) {
		super(config);
	}

}
