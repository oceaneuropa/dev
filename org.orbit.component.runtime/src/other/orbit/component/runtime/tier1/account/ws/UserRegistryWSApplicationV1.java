package other.orbit.component.runtime.tier1.account.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryServiceIndexTimer;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryUserAccountsWSResource;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.osgi.framework.ServiceRegistration;

/**
 * @see https://www.ibm.com/support/knowledgecenter/en/SSHRKX_8.0.0/plan/plan_ureg.html
 *
 */
public class UserRegistryWSApplicationV1 extends OrbitWSApplication {

	protected UserRegistryService service;
	protected IndexProviderClient indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected UserRegistryServiceIndexTimer serviceIndexTimer;

	/**
	 * 
	 * @param service
	 * @param feature
	 */
	public UserRegistryWSApplicationV1(final UserRegistryService service, int feature) {
		super(service, feature);
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(UserRegistryService.class);
			}
		});
		register(UserRegistryServiceWSResourceV1.class);
		register(UserRegistryUserAccountsWSResource.class);
	}

	public UserRegistryService getUserRegistryService() {
		return this.service;
	}

	public IndexProviderClient getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProviderClient indexProvider) {
		this.indexProvider = indexProvider;
	}

	// @Override
	// public void start() {
	// // System.out.println(getClass().getSimpleName() + ".start()");
	// if (this.isStarted.get()) {
	// return;
	// }
	// super.start();
	// this.isStarted.set(true);
	//
	// // Register Application service
	// Hashtable<String, Object> props = new Hashtable<String, Object>();
	// props.put(Constants.CONTEXT_ROOT, this.contextRoot);
	// this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	//
	// // Start a timer to update the indexing of the service
	// this.serviceIndexTimer = new UserRegistryServiceIndexTimer(this.indexProvider, this.service);
	// this.serviceIndexTimer.start();
	//
	// System.out.println(getClass().getSimpleName() + ".start(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is
	// started.");
	// }
	//
	// @Override
	// public void stop() {
	// System.out.println(getClass().getSimpleName() + ".stop()");
	// if (!this.isStarted.compareAndSet(true, false)) {
	// return;
	// }
	// super.stop();
	//
	// // Stop Timers
	// if (this.serviceIndexTimer != null) {
	// this.serviceIndexTimer.stop();
	// this.serviceIndexTimer = null;
	// }
	//
	// // Unregister Application service
	// if (this.serviceRegistration != null) {
	// this.serviceRegistration.unregister();
	// this.serviceRegistration = null;
	// }
	//
	// System.out.println(getClass().getSimpleName() + ".stop(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is
	// stopped.");
	// }

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
