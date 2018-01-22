package org.origin.common.rest.client;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.model.Request;
import org.origin.common.service.ProxyService;

public interface ServiceClient extends ProxyService, IAdaptable {

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	String getURL();

	boolean ping();

	String echo(String message) throws ClientException;

	Response sendRequest(Request request) throws ClientException;

	boolean close() throws ClientException;

}
