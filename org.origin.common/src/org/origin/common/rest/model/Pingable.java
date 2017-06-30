package org.origin.common.rest.model;

import org.origin.common.rest.client.ClientException;

public interface Pingable {

	int ping() throws ClientException;

}
