package org.nb.mgm.home.client.api;

import org.nb.common.rest.client.ClientException;
import org.nb.common.util.IAdaptable;

public interface HomeManagement extends IAdaptable {

	/**
	 * Ping the Home management server.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException;

}
