package org.orbit.component.api.tier4.mission;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public interface MissionControl extends IAdaptable {

	String getURL();

	Map<String, Object> getProperties();

	void update(Map<String, Object> properties);

	boolean ping() throws ClientException;

	String echo(String message) throws ClientException;

	Response sendRequest(Request request) throws ClientException;

	boolean close() throws ClientException;

}
