package org.orbit.component.runtime.extensions.auth;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class AuthServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.AuthServicePropertyTester";

	public static AuthServicePropertyTester INSTANCE = new AuthServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}

		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_AUTH_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_AUTH_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_AUTH_CONTEXT_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_AUTH_TOKEN_SECRET);

			String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
			String hostURL = (String) properties.get(OrbitConstants.COMPONENT_AUTH_HOST_URL);
			String name = (String) properties.get(OrbitConstants.COMPONENT_AUTH_NAME);
			String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_AUTH_CONTEXT_ROOT);
			String tokenSecret = (String) properties.get(OrbitConstants.COMPONENT_AUTH_TOKEN_SECRET);

			if ((globalHostURL != null || hostURL != null) && name != null && contextRoot != null && tokenSecret != null) {
				return true;
			}
		}

		return false;
	}

}
