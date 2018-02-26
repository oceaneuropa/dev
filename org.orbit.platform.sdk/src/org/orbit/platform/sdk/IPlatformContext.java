package org.orbit.platform.sdk;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.osgi.framework.BundleContext;

public interface IPlatformContext extends IAdaptable {

	IPlatform getPlatform();

	BundleContext getBundleContext();

	Map<String, Object> getProperties();

	void setProperties(Map<String, Object> properties);

	boolean hasProperty(String propName);

	Object getProperty(String propName);

	void setProperty(String propName, Object propValue);

	Object removeProperty(String propName);

}
