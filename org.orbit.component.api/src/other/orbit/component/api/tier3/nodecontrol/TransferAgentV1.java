package other.orbit.component.api.tier3.nodecontrol;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public interface TransferAgentV1 extends IAdaptable {

	String getName();

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping() throws ClientException;

	String echo(String message) throws ClientException;

	String level(String level1, String level2, String message1, String message2) throws ClientException;

	Response sendRequest(Request request) throws ClientException;

	boolean close() throws ClientException;

}

// public NodeConfig[] getNodeConfigs() throws ClientException;
//
// public NodeConfig createNode(CreateNodeRequest request) throws ClientException;
//
// public boolean deleteNode(DeleteNodeRequest request) throws ClientException;
//
// public Map<String, Object> getNodeProperties(String nodeId);
//
// public boolean setNodeProperty(String nodeId, String name, Object value);
//
// public Object getNodeProperty(String nodeId, String name);
//
// public boolean removeNodeProperty(String nodeId, String name);
// public NodeAgent getNodeAgent(String nodeId);

// boolean startNode(String nodeId) throws ClientException;
//
// boolean stopNode(StopNodeRequest request) throws ClientException;
//
// boolean isNodeRunning(String nodeId) throws ClientException;

// TransferAgentResponseConverter getResponseConverter();
