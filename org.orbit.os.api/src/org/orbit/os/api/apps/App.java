package org.orbit.os.api.apps;

/**
 * Whan an app is started, the app's Activator can register AppService as a service.
 *
 */
public interface App {

	/**
	 * Launch an application instance.
	 * 
	 * @param context
	 * @return an application instance.
	 */
	public AppInstance newInstance(AppContext context);

}
