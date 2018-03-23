package org.orbit.component.runtime.extensions.userregistry;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.relay.OrbitRelayConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class UserRegistryRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.UserRegistryRelayPropertyTester";

	public static UserRegistryRelayPropertyTester INSTANCE = new UserRegistryRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}

		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);

			String globalHostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
			String name = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_NAME);
			String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
			String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
			String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);

			if (globalHostURL != null && name != null && contextRoot != null && (hosts != null || urls != null)) {
				return true;
			}
		}

		return false;
	}

}
