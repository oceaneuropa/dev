package org.orbit.component.runtime.tier1.config.service;

import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface ConfigRegistryServiceV0 extends WebServiceAware, AccessTokenAware {

	ConfigRegistry getRegistry(String accountId);

}
