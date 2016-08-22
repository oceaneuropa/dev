package org.nb.home.client.api;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface HomeAgent extends IAdaptable {

	/**
	 * Ping the Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException;

	/**
	 * Connect to remote Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean connect() throws ClientException;

	/**
	 * Disconnect from remote Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean disconnect() throws ClientException;

	/**
	 * Check whether the IHomeAdmin is connected to a remote home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean isConnected() throws ClientException;

	/**
	 * Check whether the remote home agent is active.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean isActive() throws ClientException;

	/**
	 * 
	 * @param projectId
	 * @param projectHomeId
	 */
	public void buildProject(String projectId, String projectHomeId) throws ClientException;

}
