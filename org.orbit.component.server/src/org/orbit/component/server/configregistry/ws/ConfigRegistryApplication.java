package org.orbit.component.server.configregistry.ws;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.core.Application;

import org.orbit.component.server.configregistry.service.ConfigRegistryServiceTimer;
import org.origin.common.rest.Constants;
import org.origin.common.rest.server.AbstractApplication;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigRegistryApplication extends AbstractApplication {

	protected static Logger logger = LoggerFactory.getLogger(ConfigRegistryApplication.class);

	protected BundleContext bundleContext;
	protected String hostURL;
	protected String contextRoot;
	protected String componentName;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected ServiceRegistration<?> serviceRegistration;
	protected ConfigRegistryServiceTimer serviceTimer;

	public ConfigRegistryApplication() {
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public IndexProviderLoadBalancer getIndexProviderLoadBalancer() {
		return indexProviderLoadBalancer;
	}

	public void setIndexProviderLoadBalancer(IndexProviderLoadBalancer indexProviderLoadBalancer) {
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException("ConfigRegistryApplication is not started.");
		}
	}

	/**
	 * Registry as an OSGi service. Called when Activator is started.
	 */
	public void start() {
		logger.debug("ConfigRegistryApplication.start()");
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// Register Application service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);

		// Start Timers
		this.serviceTimer = new ConfigRegistryServiceTimer(this.hostURL, this.contextRoot, this.componentName, this.indexProviderLoadBalancer);
		this.serviceTimer.start();
	}

	/**
	 * Unregister OSGi service. Called when Activator is stopped.
	 */
	public void stop() {
		logger.debug("ConfigRegistryApplication.stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Stop Timers
		if (this.serviceTimer != null) {
			this.serviceTimer.stop();
			this.serviceTimer = null;
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
		classes.add(ConfigRegistryResource.class);

		// resolvers
		classes.add(ConfigRegistryServiceResolver.class);

		return classes;
	}

}
