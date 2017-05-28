package org.orbit.component.connector.tier3.transferagent;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;

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
 */
public class TransferAgentWSClient extends AbstractClient {

	public static String PATH_NODES = "nodes";

	/**
	 * 
	 * @param config
	 */
	public TransferAgentWSClient(ClientConfiguration config) {
		super(config);
	}

}
