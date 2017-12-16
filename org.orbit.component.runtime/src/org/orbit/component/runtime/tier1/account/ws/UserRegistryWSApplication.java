package org.orbit.component.runtime.tier1.account.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;

/**
 * @see https://www.ibm.com/support/knowledgecenter/en/SSHRKX_8.0.0/plan/plan_ureg.html
 *
 */
public class UserRegistryWSApplication extends OrbitWSApplication {

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public UserRegistryWSApplication(final UserRegistryService service, int feature) {
		super(service.getContextRoot(), feature);
		adapt(UserRegistryService.class, service);

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(UserRegistryService.class);
			}
		});
		register(UserRegistryUserAccountsWSResource.class);
	}

}
