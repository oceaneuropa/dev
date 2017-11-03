package org.origin.common.rest.server;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.origin.common.deploy.DeployCallback;
import org.origin.common.rest.Constants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

public class AbstractResourceConfigApplication extends ResourceConfig implements DeployCallback {

	protected BundleContext bundleContext;
	protected String contextRoot;
	protected ServiceRegistration<?> serviceRegistration;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 * @param contextRoot
	 */
	public AbstractResourceConfigApplication(BundleContext bundleContext, String contextRoot) {
		this.bundleContext = bundleContext;
		this.contextRoot = contextRoot;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	public void start() {
		System.out.println(getClass().getSimpleName() + ".start()");
		if (!this.isStarted.compareAndSet(false, true)) {
			return;
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.CONTEXT_ROOT, this.contextRoot);
		this.serviceRegistration = this.bundleContext.registerService(Application.class, this, props);
	}

	public void stop() {
		System.out.println(getClass().getSimpleName() + ".stop()");
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public BundleContext getBundleContext() {
		return this.bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void deployedTo(Object target) {
		System.out.println(getClass().getSimpleName() + ".deployedTo() " + target);

		if (target instanceof HttpService) {
			HttpService httpService = (HttpService) target;
			System.out.println(this + " ===> " + httpService);
		}
	}

	@Override
	public void undeployedFrom(Object target) {
		System.out.println(getClass().getSimpleName() + ".undeployedFrom() " + target);

		if (target instanceof HttpService) {
			HttpService httpService = (HttpService) target;
			System.out.println(this + " <=== " + httpService);
		}
	}

}
