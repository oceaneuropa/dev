package org.orbit.component.server.tier1.auth.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.server.tier1.auth.service.AuthService;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.core.resources.server.ws.ResourceWSApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;

/**
 * @see ResourceWSApplication
 *
 */
public class AuthWSApplication extends AbstractResourceConfigApplication {

	protected AuthService service;
	protected IndexProvider indexProvider;
	protected AuthServiceIndexTimer serviceIndexTimer;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public AuthWSApplication(final BundleContext bundleContext, final AuthService service) {
		super(bundleContext, service.getContextRoot());
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(AuthService.class);
			}
		});
		register(AuthWSResource.class);
	}

	public AuthService getAuthService() {
		return this.service;
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		if (isStarted()) {
			System.out.println(getClass().getSimpleName() + ".stop() App is already started.");
			return;
		}
		super.start();

		// Start timer for indexing the service
		if (this.indexProvider != null) {
			this.serviceIndexTimer = new AuthServiceIndexTimer(this.service, this.indexProvider);
			this.serviceIndexTimer.start();
		}
	}

	@Override
	public void stop() {
		if (!isStarted()) {
			System.out.println(getClass().getSimpleName() + ".stop() App is already stopped.");
			return;
		}

		// Stop timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		super.stop();
	}

}
