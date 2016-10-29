package org.origin.common.rest.model;

import org.origin.common.rest.client.ClientException;

public interface Pingable {

	public int ping() throws ClientException;

}
