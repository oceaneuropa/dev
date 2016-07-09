package org.nb.home.client.api;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface IHomeControl extends IAdaptable {

	/**
	 * Ping the Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException;

}
