package org.orbit.component.api.tier1.auth.other;

import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.origin.common.rest.client.ClientException;

public interface AuthConnector {

	Auth getService(Map<String, Object> properties) throws ClientException;

	boolean update(Auth auth, Map<String, Object> properties) throws ClientException;

	boolean close(Auth auth) throws ClientException;

}
