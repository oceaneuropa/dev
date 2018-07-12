package other.orbit.component.api.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.Auth;
import org.origin.common.rest.client.ClientException;

public interface AuthConnectorV0 {

	Auth getService(Map<String, Object> properties) throws ClientException;

	boolean update(Auth auth, Map<String, Object> properties) throws ClientException;

	boolean close(Auth auth) throws ClientException;

}