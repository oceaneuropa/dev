package org.orbit.platform.api.apps;

/**
 * Context data could be: BundleContext, World, service configuration properties (stored in and retrieved from personal config registry)
 * 
 * The World may provide API for setting/getting configuration properties for services and apps.
 * 
 */
public interface ServiceContext {

	String[] getPropertyNames();

	Object getProperty(String key);

	<T> T getProperty(String key, Class<T> clazz);

}
