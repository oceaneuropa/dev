package other.orbit.component.api.tier1.account;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;

public interface UserRegistryConnectorV1 extends LoadBalancedServiceConnector<UserAccountClient> {

}
