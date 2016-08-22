package org.origin.common.rest.server;

import javax.ws.rs.core.Application;

import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;
import org.origin.common.deploy.DeployCallback;
import org.osgi.service.http.HttpService;

public abstract class AbstractApplication extends Application implements DeployCallback {

	protected String id;
	protected IEditingDomain editingDomain;
	protected ICommandStack commandStack;

	public AbstractApplication() {
		this.id = this.toString();
		System.out.println(getClass().getName() + ": id=" + this.id);
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

	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	public ICommandStack getCommandStack() {
		return this.commandStack;
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
