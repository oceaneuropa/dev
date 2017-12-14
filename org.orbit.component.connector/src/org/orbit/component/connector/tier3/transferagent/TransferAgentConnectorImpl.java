package org.orbit.component.connector.tier3.transferagent;

import java.util.Map;

import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ServiceConnector;

public class TransferAgentConnectorImpl extends ServiceConnector<TransferAgent> {

	public TransferAgentConnectorImpl() {
		super(TransferAgent.class);
	}

	@Override
	protected TransferAgent create(Map<Object, Object> properties) {
		return new TransferAgentImpl(properties);
	}

}

// public class TransferAgentConnectorImpl extends ServiceConnector<TransferAgent> implements TransferAgentConnector {
// import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
