package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

import org.origin.common.rest.client.ClientException;

public interface TransferAgentConnector {

	TransferAgent getService(Map<String, Object> properties) throws ClientException;

	boolean update(TransferAgent agent, Map<String, Object> properties) throws ClientException;

	boolean close(TransferAgent agent) throws ClientException;

}
