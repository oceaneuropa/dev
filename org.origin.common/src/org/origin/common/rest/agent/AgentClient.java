package org.origin.common.rest.agent;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

public abstract class AgentClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public AgentClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public Responses sendRequest(Request request) throws ClientException {
		Responses result = null;
		Response response = null;
		try {
			Builder builder = fillRequestPath(getRootPath()).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<Request>(request) {
			}));
			checkResponse(response);

			result = response.readEntity(Responses.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return result;
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
