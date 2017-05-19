package org.origin.common.rest.server;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.core.Application;

import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;
import org.origin.common.deploy.DeployCallback;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;

public abstract class AbstractApplication extends Application implements DeployCallback {

	protected String id;
	protected String contextRoot;
	protected BundleContext bundleContext;

	protected IEditingDomain editingDomain;
	protected ICommandStack commandStack;
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	public AbstractApplication() {
		this.id = this.toString();
		System.out.println(getClass().getName() + ": id=" + this.id);
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	public ICommandStack getCommandStack() {
		return this.commandStack;
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	/**
	 * Start the application.
	 */
	public void start() {
		this.editingDomain = IEditingDomain.getEditingDomain(this.id);
		this.commandStack = this.editingDomain.getCommandStack(this);
	}

	/**
	 * Stop the application.
	 */
	public void stop() {
		if (this.editingDomain != null) {
			this.editingDomain.disposeCommandStack(this);
			IEditingDomain.disposeEditingDomain(this.id);
			this.editingDomain = null;
			this.commandStack = null;
		}
	}

	@Override
	public void deployedTo(Object target) {
		if (target instanceof HttpService) {
			HttpService httpService = (HttpService) target;
			System.out.println(this + " ===> " + httpService);
		}
	}

	@Override
	public void undeployedFrom(Object target) {
		if (target instanceof HttpService) {
			HttpService httpService = (HttpService) target;
			System.out.println(this + " <=== " + httpService);
		}
	}

}
