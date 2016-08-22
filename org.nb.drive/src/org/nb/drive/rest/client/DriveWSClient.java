package org.nb.drive.rest.client;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public class DriveWSClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public DriveWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

}
