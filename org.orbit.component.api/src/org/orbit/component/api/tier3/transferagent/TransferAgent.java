package org.orbit.component.api.tier3.transferagent;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public interface TransferAgent extends IAdaptable {

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping() throws ClientException;

	String echo(String message) throws ClientException;

	String level(String level1, String level2, String message1, String message2) throws ClientException;

	Response sendRequest(Request request) throws ClientException;

	boolean close() throws ClientException;

}
