package com.osgi.example1.vpn;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

/**
 * YahooFinance web service client to retrieve stock data.
 *
 */
public class JIRAWebServiceClient extends AbstractWSClient {

	public JIRAWebServiceClient() {
		super(ClientConfiguration.get("https://some.company.com:443", "browse/some-ticket", null, null));
	}

	public JIRAWebServiceClient(ClientConfiguration config) {
		super(config);
	}

	public String getResponseString() throws ClientException {
		String responseString = null;
		Response response = null;
		try {
			WebTarget target = getRootPath();
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			responseString = response.readEntity(String.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return responseString;
	}

	public static void main(String[] args) {
		try {
			JIRAWebServiceClient client = new JIRAWebServiceClient();
			String responseString = client.getResponseString();
			System.out.println("responseString is:");
			System.out.println(responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
