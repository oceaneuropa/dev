package org.origin.common.rest.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ResponseConverter;
import org.origin.common.rest.model.Responses;

public abstract class CommonWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public CommonWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public Responses sendRequest(Request request) throws ClientException {
		Responses responses = null;

		Response wsResponse = null;
		try {
			Builder builder = fillRequestPath(getRootPath()).request(MediaType.APPLICATION_JSON);
			wsResponse = updateHeaders(builder).post(Entity.json(new GenericEntity<Request>(request) {
			}));
			checkResponse(wsResponse);

			// result = response.readEntity(Responses.class);
			String responsesString = wsResponse.readEntity(String.class);
			if (responsesString != null) {
				responses = ResponseConverter.parse(responsesString);
			}

		} catch (Exception e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(wsResponse, true);
		}
		return responses;
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
