package org.orbit.component.runtime.tier1.config.service;

import org.origin.common.service.WebServiceAware;

public interface ConfigRegistryService extends WebServiceAware {

	ConfigRegistry getRegistry(String accountId);

}
