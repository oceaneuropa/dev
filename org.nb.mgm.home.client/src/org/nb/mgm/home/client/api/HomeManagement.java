package org.nb.mgm.home.client.api;

import org.nb.common.util.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface HomeManagement extends IAdaptable {

	/**
	 * Ping the Home management server.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException;

}
