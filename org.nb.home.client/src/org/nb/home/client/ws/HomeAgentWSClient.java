package org.nb.home.client.ws;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.home.model.dto.PingRequest;
import org.nb.home.model.dto.PingResponse;
import org.origin.common.rest.agent.AgentClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Responses;

/**
 * Home Agent web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 * 
 */
public class HomeAgentWSClient extends AgentClient {

	/**
	 * 
	 * @param config
	 */
	public HomeAgentWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int pingDirectly() throws ClientException {
		int pingResult = 0;
		try {
			Builder builder = getRootPath().path("/ping").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			pingResult = response.readEntity(Integer.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return pingResult;
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
	 * 
	 */
	@Override
	public int ping() throws ClientException {
		int pingResult = -1;
		try {
			Responses responses = sendRequest(new PingRequest());
			if (!responses.isEmpty()) {
				PingResponse pingResponse = new PingResponse(responses);
				pingResult = pingResponse.getResult();
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return pingResult;
	}

}
