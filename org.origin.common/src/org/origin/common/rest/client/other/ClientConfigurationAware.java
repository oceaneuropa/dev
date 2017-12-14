package org.origin.common.rest.client.other;

import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public interface ClientConfigurationAware {

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	public ClientConfiguration getClientConfiguration() throws ClientException;

}
