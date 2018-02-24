package org.orbit.component.api.tier3.domain.other;

import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;

public interface DomainServiceConnector extends LoadBalancedServiceConnector<DomainManagementClient> {

}
