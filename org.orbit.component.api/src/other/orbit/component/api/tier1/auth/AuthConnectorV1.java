package other.orbit.component.api.tier1.auth;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;

public interface AuthConnectorV1 extends LoadBalancedServiceConnector<AuthClient> {

}
