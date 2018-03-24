package org.orbit.component.runtime.extensions.userregistry;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class UserRegistryServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.UserRegistryServicePropertyTester";

	public static UserRegistryServicePropertyTester INSTANCE = new UserRegistryServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART);
			String autoStart = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
