package osgi.mgm.ws.client;

import java.net.ConnectException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.dto.ErrorDTO;

public abstract class AbstractMgmClient {

	protected ClientConfiguration config;
	protected Client client;

	/**
	 * 
	 * @param config
	 */
	public AbstractMgmClient(ClientConfiguration config) {
		this.config = config;
		this.client = config.createClient();
	}

	public ClientConfiguration getClientConfiguration() {
		return config;
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
					throw new ClientException(response.getStatus(), error.getCode() + ": " + error.getMessage(), null);
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

}
