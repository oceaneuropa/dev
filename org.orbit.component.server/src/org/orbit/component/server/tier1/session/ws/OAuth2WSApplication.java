package org.orbit.component.server.tier1.session.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;

public class OAuth2WSApplication extends AbstractApplication {

	// protected static Logger logger = LoggerFactory.getLogger(OAuth2WSApplication.class);

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	// protected OAuth2ServiceIndexTimer serviceIndexTimer;
	protected OAuth2ServiceIndexTimerV2 serviceIndexTimer;

	public OAuth2WSApplication() {
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");
		if (this.isStarted.get()) {
			return;
		}
		super.start();
		this.isStarted.set(true);

		// Register Application service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start timer for indexing the service
		this.serviceIndexTimer = new OAuth2ServiceIndexTimerV2(this.indexProvider);
		this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Start timer for indexing the service
		if (this.serviceIndexTimer != null) {
			this.serviceIndexTimer.stop();
			this.serviceIndexTimer = null;
		}

		// Unregister Application service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		super.stop();
	}

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();

		// resources
		classes.add(OAuth2ServiceResource.class);

		// resolvers
		classes.add(OAuth2ServiceResolver.class);

		return classes;
	}

}
