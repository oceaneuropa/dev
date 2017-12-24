package org.orbit.component.connector.tier3.transferagent;

import java.util.Map;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ServiceConnector;
//import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;

public class TransferAgentConnectorImpl extends ServiceConnector<TransferAgent> /* implements TransferAgentConnector */ {

	public TransferAgentConnectorImpl() {
		super(TransferAgent.class);
	}

	@Override
	protected TransferAgent create(Map<String, Object> properties) {
		return new TransferAgentImpl(properties);
	}

}
