package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

public interface TransferAgentConnector {

	TransferAgent getService(Map<Object, Object> properties);

}
