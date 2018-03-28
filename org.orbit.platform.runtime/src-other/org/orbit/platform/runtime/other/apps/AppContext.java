package org.orbit.platform.runtime.other.apps;

/**
 * Context data could be: BundleContext, World, app configuration properties (stored in and retrieved from personal config registry)
 * 
 */
public interface AppContext {

	String[] getPropertyNames();

	Object getProperty(String key);

	<T> T getProperty(String key, Class<T> clazz);

}
