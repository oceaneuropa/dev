package org.origin.common.rest.client;

import java.net.ConnectException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Ping the service.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 *
 */
public abstract class AbstractWSClient implements Pingable {

	protected ClientConfiguration config;
	protected Client client;

	/**
	 * 
	 * @param config
	 */
	public AbstractWSClient(ClientConfiguration config) {
		this.config = config;
		this.client = config.createClient();
	}

	public ClientConfiguration getClientConfiguration() {
		return this.config;
	}

	/**
	 * 
	 * @param failOnUnknownProperties
	 * @return
	 */
	protected ObjectMapper createObjectMapper(boolean failOnUnknownProperties) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		return mapper;
	}

	/**
	 * 
	 * @param response
	 * @throws ClientException
	 */
	protected void checkResponse(Response response) throws ClientException {
		if (response != null && response.getStatusInfo() != null) {
			if (!Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
				// ErrorDTO error = null;
				// try {
				// // String responseString = response.readEntity(String.class);
				// error = response.readEntity(ErrorDTO.class);
				// } catch (Exception e) {
				// // e.printStackTrace();
				// // System.err.println(getClass().getSimpleName() + ".checkResponse(Response) Exception [" + e.getClass().getSimpleName() + "] " +
				// e.getMessage());
				// // throw new ClientException(500, e.getMessage(), null);
				// }
				// if (error != null) {
				// throw new ClientException(response.getStatus(), error.getMessage(), null);
				// } else {
				// throw new ClientException(response);
				// }
				throw new ClientException(response);
			}
		}
	}

	/**
	 * 
	 * @param e
	 * @throws ClientException
	 */
	protected void handleException(Exception e) throws ClientException {
		if (e instanceof ClientException) {
			ClientException ce = (ClientException) e;
			throw ce;

		} else if (e instanceof ProcessingException) {
			ProcessingException pe = (ProcessingException) e;
			if (pe.getCause() instanceof ConnectException) {
				throw new ClientException(503, pe.getMessage(), pe.getCause());
			}
			throw new ClientException(500, pe.getMessage(), pe);
		} else if (e != null) {
			throw new ClientException(500, e.getMessage(), e);
		}
	}

	/**
	 * Get WetTarget of {scheme}://{host}:{port}/{contextRoot}
	 * 
	 * @return
	 */
	public WebTarget getRootPath() {
		return this.client.target(this.config.getBaseUrl());
	}

	/**
	 * Update headers of the Builder
	 * 
	 * @param builder
	 * @return
	 */
	public Builder updateHeaders(Builder builder) {
		String tokenType = this.config.getTokenType();
		String accessToken = this.config.getAccessToken();

		if (accessToken != null && !accessToken.isEmpty()) {
			builder = builder.header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken);
		}

		String cookies = "";
		if (accessToken != null && !accessToken.isEmpty()) {
			cookies += ("tokenType=" + tokenType);
			cookies += (";accessToken=" + accessToken);
		}
		// Map<String, String> props = this.config.getProperties();
		// if (props != null) {
		// int i = 0;
		// for (Iterator<String> itor = props.keySet().iterator(); itor.hasNext();) {
		// if (i > 0) {
		// cookies += ";";
		// }
		// String key = itor.next();
		// String value = props.get(key);
		// cookies += (key + "=" + value);
		// i++;
		// }
		// }
		builder = builder.header(HttpHeaders.COOKIE, cookies);

		return builder;
	}

	/**
	 * Close the web service client
	 */
	public void close() {
		this.client.close();
	}

	/**
	 * 
	 * @return
	 */
	public boolean doPing() {
		try {
			int ping = ping();
			if (ping > 0) {
				return true;
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Ping the service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException {
		int result = 0;
		Response response = null;
		try {
			Builder builder = getRootPath().path("ping").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			result = response.readEntity(Integer.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

}
