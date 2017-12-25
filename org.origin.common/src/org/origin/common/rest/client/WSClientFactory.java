package org.origin.common.rest.client;

import java.util.Map;

import javax.ws.rs.client.Client;

public interface WSClientFactory {

	Client createClient(Map<String, Object> properties);

}
