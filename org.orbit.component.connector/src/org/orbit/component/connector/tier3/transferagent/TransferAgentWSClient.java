package org.orbit.component.connector.tier3.transferagent;

import org.origin.common.rest.client.AbstractRequestResponseWSClient;
import org.origin.common.rest.client.ClientConfiguration;

/*
 * Transfer agent web service client.
 * 
 * {contextRoot} example:
 * /orbit/v1/transferagent
 * 
 * Nodes
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/request (Body parameter: Request)
 * 
 * @see HomeAgentWSClient
 * 
 */
public class TransferAgentWSClient extends AbstractRequestResponseWSClient {

	/**
	 * 
	 * @param config
	 */
	public TransferAgentWSClient(ClientConfiguration config) {
		super(config);
	}

}

// @Override
// public Response sendRequest(Request request) throws ClientException {
// return super.sendRequest(request);
// }
