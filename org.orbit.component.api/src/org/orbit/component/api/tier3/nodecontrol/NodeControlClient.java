package org.orbit.component.api.tier3.nodecontrol;

import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface NodeControlClient extends ServiceClient {

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	String echo(String message) throws ClientException;

	String level(String level1, String level2, String message1, String message2) throws ClientException;

	NodeInfo[] getNodes() throws ClientException;

	boolean close() throws ClientException;

}

// boolean ping() throws ClientException;
// Response sendRequest(Request request) throws ClientException;
