package org.origin.common.rest.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.model.Request;

public abstract class AbstractRequestResponseWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public AbstractRequestResponseWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public Response sendRequest(Request request) throws ClientException {
		Response wsResponse = null;
		try {
			Builder builder = fillRequestPath(getRootPath()).request(MediaType.APPLICATION_JSON);
			wsResponse = updateHeaders(builder).post(Entity.json(new GenericEntity<Request>(request) {
			}));
			checkResponse(wsResponse);

		} catch (Exception e) {
			handleException(e);
		}
		return wsResponse;
	}

	/**
	 * 
	 * @param webTarget
	 * @return
	 */
	public WebTarget fillRequestPath(WebTarget webTarget) {
		return webTarget.path("/request");
	}

}
