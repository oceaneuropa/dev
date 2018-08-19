package org.orbit.component.connector.tier3.nodecontrol;

import javax.ws.rs.client.Invocation.Builder;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;

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
public class NodeControlWSClient extends WSClient {

	public NodeControlWSClient(WSClientConfiguration config) {
		super(config);
	}

	@Override
	public Builder updateHeaders(Builder builder) {
		return super.updateHeaders(builder);
	}

}
