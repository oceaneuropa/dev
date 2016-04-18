package org.nb.mgm.home.client.ws;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.common.rest.client.AbstractClient;
import org.nb.common.rest.client.ClientConfiguration;
import org.nb.common.rest.client.ClientException;

/**
 * Home API web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * 
 */
public class HomeApiClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public HomeApiClient(ClientConfiguration config) {
		super(config);
	}

	public int ping() throws ClientException {
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

}
