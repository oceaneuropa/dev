package org.orbit.component.runtime.tier1.account.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.osgi.framework.BundleContext;

/**
 * @see https://www.ibm.com/support/knowledgecenter/en/SSHRKX_8.0.0/plan/plan_ureg.html
 *
 */
public class UserRegistryWSApplication extends AbstractResourceConfigApplication {

	protected UserRegistryService service;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public UserRegistryWSApplication(final BundleContext bundleContext, final UserRegistryService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(UserRegistryService.class);
			}
		});
		register(UserRegistryUserAccountsWSResource.class);
	}

	public UserRegistryService getUserRegistryService() {
		return this.service;
	}

}
