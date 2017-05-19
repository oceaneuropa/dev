package org.orbit.component.server.tier1.account.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.orbit.component.server.tier1.account.service.UserRegistryServiceIndexTimer;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://www.ibm.com/support/knowledgecenter/en/SSHRKX_8.0.0/plan/plan_ureg.html
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class UserRegistryWSApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(UserRegistryWSApplication.class);

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected UserRegistryServiceIndexTimer serviceIndexTimer;

	public UserRegistryWSApplication() {
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		logger.debug("UserRegistryWSApplication.start()");
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
		this.serviceIndexTimer = new UserRegistryServiceIndexTimer(this.indexProvider);
		this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		logger.debug("UserRegistryWSApplication.stop()");
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
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(UserRegistryServiceResource.class);
		classes.add(UserRegistryUserAccountsResource.class);

		// resolvers
		classes.add(UserRegistryServiceResolver.class);

		// http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
		// In order to use multipart in your Jersey application you need to register MultiPartFeature in your application.
		// Add additional features such as support for Multipart.
		classes.add(MultiPartFeature.class);

		return classes;
	}

}
