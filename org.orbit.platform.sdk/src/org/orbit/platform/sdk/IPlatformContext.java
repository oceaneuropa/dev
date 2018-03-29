package org.orbit.platform.sdk;

import java.util.Map;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.extensions.util.ExtensionServiceAware;
import org.osgi.framework.BundleContext;

public interface IPlatformContext extends ExtensionServiceAware, IAdaptable {

	IPlatform getPlatform();

	BundleContext getBundleContext();

	Map<Object, Object> getProperties();

	void setProperties(Map<Object, Object> properties);

	boolean hasProperty(String propName);

	Object getProperty(String propName);

	void setProperty(String propName, Object propValue);

	Object removeProperty(String propName);

}
