package org.orbit.component.runtime.tier1.config.service;

import org.origin.common.service.AccessTokenProvider;
import org.origin.common.service.IWebService;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ConfigRegistryServiceV0 extends IWebService, AccessTokenProvider {

	ConfigRegistry getRegistry(String accountId);

}
