package other.orbit.component.runtime.tier1.config.ws;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.orbit.component.runtime.common.ws.OrbitWSApplication;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryWSResource;
import org.osgi.framework.ServiceRegistration;

public class ConfigRegistryWSApplicationV1 extends OrbitWSApplication {

	protected ConfigRegistryServiceV0 service;
	protected ServiceRegistration<?> serviceRegistration;

	/**
	 * 
	 * @param bundleContext
	 * @param service
	 */
	public ConfigRegistryWSApplicationV1(final ConfigRegistryServiceV0 service, int feature) {
		super(service, feature);
		this.service = service;

		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(service).to(ConfigRegistryServiceV0.class);
			}
		});
		register(ConfigRegistryWSResource.class);
	}

	public ConfigRegistryServiceV0 getConfigRegistryService() {
		return this.service;
	}

}

// protected static Logger logger = LoggerFactory.getLogger(ConfigRegistryWSApplication.class);
// protected ConfigRegistryServiceIndexTimer serviceIndexTimer;

// @Override
// public Set<Class<?>> getClasses() {
// Set<Class<?>> classes = new HashSet<Class<?>>();
//
// // resources
// classes.add(ConfigRegistryServiceResource.class);
// classes.add(ConfigRegistryResource.class);
//
// // resolvers
// classes.add(ConfigRegistryServiceResolver.class);
//
// return classes;
// }

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
// System.out.println(getClass().getSimpleName() + ".start(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is
// started.");
// }
//
// @Override
// public void stop() {
// // System.out.println(getClass().getSimpleName() + ".stop()");
// if (!this.isStarted.compareAndSet(true, false)) {
// return;
// }
// super.stop();
//
// // Unregister Application service
// if (this.serviceRegistration != null) {
// this.serviceRegistration.unregister();
// this.serviceRegistration = null;
// }
//
// System.out.println(getClass().getSimpleName() + ".stop(). Web service for [" + this.service.getNamespace() + "." + this.service.getName() + "] is stopped.");
// }
