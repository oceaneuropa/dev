package org.origin.common.rest.server;

import javax.ws.rs.core.Application;

import org.origin.common.deploy.DeployCallback;
import org.osgi.service.http.HttpService;

public abstract class AbstractApplication extends Application implements DeployCallback {

	/**
	 * Start the application.
	 */
	public abstract void start();

	/**
	 * Stop the application.
	 */
	public abstract void stop();

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
