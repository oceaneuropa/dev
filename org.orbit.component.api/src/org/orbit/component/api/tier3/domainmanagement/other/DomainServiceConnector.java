package org.orbit.component.api.tier3.domainmanagement.other;

import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;

public interface DomainServiceConnector extends LoadBalancedServiceConnector<DomainManagementClient> {

}
