package org.orbit.component.server.tier1.config.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.orbit.component.server.tier1.config.service.ConfigRegistryServiceIndexTimer;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.api.IndexProvider;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryWSApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(ConfigRegistryWSApplication.class);

	protected IndexProvider indexProvider;
	protected ServiceRegistration<?> serviceRegistration;
	protected ConfigRegistryServiceIndexTimer serviceIndexTimer;

	public ConfigRegistryWSApplication() {
	}

	public IndexProvider getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	@Override
	public void start() {
		logger.debug("ConfigRegistryWSApplication.start()");
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
		this.serviceIndexTimer = new ConfigRegistryServiceIndexTimer(this.indexProvider);
		this.serviceIndexTimer.start();
	}

	@Override
	public void stop() {
		logger.debug("ConfigRegistryWSApplication.stop()");
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
		classes.add(ConfigRegistryServiceResource.class);
		classes.add(ConfigRegistryResource.class);

		// resolvers
		classes.add(ConfigRegistryServiceResolver.class);

		return classes;
	}

}
