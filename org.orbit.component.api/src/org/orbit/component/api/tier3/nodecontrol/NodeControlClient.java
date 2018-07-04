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

	NodeInfo getNode(String id) throws ClientException;

	boolean createNode(String id, String name, String typeId) throws ClientException;

	boolean updateNode(String id, String name, String typeId) throws ClientException;

	boolean deleteNode(String id) throws ClientException;

	boolean startNode(String id) throws ClientException;

	boolean stopNode(String id) throws ClientException;

	boolean addNodeAttribute(String id, String name, Object value) throws ClientException;

	boolean updateNodeAttribute(String id, String oldName, String name, Object value) throws ClientException;

	boolean deleteNodeAttribute(String id, String name) throws ClientException;

	boolean close() throws ClientException;

}
