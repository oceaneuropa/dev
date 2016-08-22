package org.origin.common.rest.client;

public interface ClientConfigurationAware {

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	public ClientConfiguration getClientConfiguration() throws ClientException;

}
