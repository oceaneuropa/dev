package org.orbit.component.connector.tier3.transferagent;

import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.CommonWSClient;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

/*
 * DomainManagement Machines resource client.
 * 
 * {contextRoot} example:
 * /orbit/v1/transferagent
 * 
 * Nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/nodes/{nodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/nodes (Body parameter: NodeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/nodes (Body parameter: NodeConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/nodes/{nodeId}
 * 
 * @see HomeAgentWSClient
 * 
 */
public class TransferAgentWSClient extends CommonWSClient {

	public static String PATH_NODES = "nodes";

	/**
	 * 
	 * @param config
	 */
	public TransferAgentWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public Responses sendRequest(Request request) throws ClientException {
		return super.sendRequest(request);
	}

}
