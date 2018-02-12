package org.orbit.platform.api.apps;

/**
 * Whan an app is started, the app's Activator can register AppService as a service.
 * 
 */
public interface Service {

	/**
	 * Service name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Start the service.
	 * 
	 * @param context
	 */
	void start(ServiceContext context);

	/**
	 * Stop the service.
	 * 
	 * @param context
	 */
	void stop(ServiceContext context);

}
