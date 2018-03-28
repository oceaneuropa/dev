package org.orbit.component.api.tier1.auth.other;

import org.orbit.component.api.tier1.auth.Auth;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;

public interface AuthConnectorV1 extends LoadBalancedServiceConnector<Auth> {

}
