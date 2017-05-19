package org.origin.common.rest.client;

import java.net.ConnectException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.origin.common.rest.model.ErrorDTO;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Ping the service.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public abstract class AbstractClient implements IClient {

	protected ClientConfiguration config;
	protected Client client;

	/**
	 * 
	 * @param config
	 */
	public AbstractClient(ClientConfiguration config) {
		this.config = config;
		this.client = config.createClient();
	}

	public ClientConfiguration getClientConfiguration() {
		return config;
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
				ErrorDTO error = null;
				try {
					error = response.readEntity(ErrorDTO.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (error != null) {
					throw new ClientException(response.getStatus(), error.getMessage(), null);
				} else {
					throw new ClientException(response.getStatus(), response.getStatusInfo().getReasonPhrase(), null);
				}
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
		return this.config.updateHeaders(builder);
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
		} catch (ClientException e) {
			System.out.println(e.getMessage());
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
			ClientUtil.closeQuietly(response, true);
		}
		return result;
	}

}
