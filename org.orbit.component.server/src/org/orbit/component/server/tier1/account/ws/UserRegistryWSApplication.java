package org.orbit.component.server.tier1.account.ws;

import java.util.Hashtable;

import javax.ws.rs.core.Application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractResourceConfigApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @see https://www.ibm.com/support/knowledgecenter/en/SSHRKX_8.0.0/plan/plan_ureg.html
 *
 */
public class UserRegistryWSApplication extends AbstractResourceConfigApplication {

	protected UserRegistryService service;
	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected UserRegistryServiceIndexTimerV2 serviceIndexTimer;

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
		register(UserRegistryServiceResource.class);
		register(UserRegistryUserAccountsResource.class);
	}

	public UserRegistryService getUserRegistryService() {
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
		// System.out.println(getClass().getSimpleName() + ".start()");
		if (this.isStarted.get()) {
			return;
		}
		super.start();
		this.isStarted.set(true);

		// Register Application service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start a timer to update the indexing of the service
		this.serviceIndexTimer = new UserRegistryServiceIndexTimerV2(this.indexProvider, this.service);
		this.serviceIndexTimer.start();

		System.out.println(getClass().getSimpleName() + ".start(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is started.");
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}
		super.stop();

		// Stop Timers
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister Application service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		System.out.println(getClass().getSimpleName() + ".stop(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is stopped.");
	}

}

// protected static Logger logger = LoggerFactory.getLogger(UserRegistryWSApplication.class);
// protected UserRegistryServiceIndexTimer serviceIndexTimer;

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(UserRegistryServiceResource.class);
// classes.add(UserRegistryUserAccountsResource.class);
//
// // resolvers
// classes.add(UserRegistryServiceResolver.class);
//
// // http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
// // In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
// // Add additional features such as support for Multipart.
// classes.add(MultiPartFeature.class);
//
// return classes;
// }
