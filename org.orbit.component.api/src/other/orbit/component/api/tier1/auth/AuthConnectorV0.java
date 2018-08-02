package other.orbit.component.api.tier1.auth;

import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.origin.common.rest.client.ClientException;

public interface AuthConnectorV0 {

	AuthClient getService(Map<String, Object> properties) throws ClientException;

	boolean update(AuthClient auth, Map<String, Object> properties) throws ClientException;

	boolean close(AuthClient auth) throws ClientException;

}
