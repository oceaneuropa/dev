package org.orbit.component.runtime.tier1.config.service;

import org.origin.common.rest.server.WebServiceAware;

public interface ConfigRegistryService extends WebServiceAware {

	String getName();

	ConfigRegistry getRegistry(String userId);

}
