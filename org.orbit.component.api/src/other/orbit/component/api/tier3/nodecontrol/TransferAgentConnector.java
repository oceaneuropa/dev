package other.orbit.component.api.tier3.nodecontrol;

import java.util.Map;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.origin.common.rest.client.ClientException;

public interface TransferAgentConnector {

	NodeControlClient getService(Map<String, Object> properties) throws ClientException;

	boolean update(NodeControlClient agent, Map<String, Object> properties) throws ClientException;

	boolean close(NodeControlClient agent) throws ClientException;

}
