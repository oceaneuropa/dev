package org.nb.home.client.api;

import org.origin.common.rest.client.ClientException;
import org.origin.common.util.IAdaptable;

public interface HomeManagement extends IAdaptable {

	/**
	 * Ping the Home management server.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException;

}
