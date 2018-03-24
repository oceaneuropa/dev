package org.orbit.component.runtime.extensions.auth;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitRelayConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class AuthRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.AuthRelayPropertyTester";

	public static AuthRelayPropertyTester INSTANCE = new AuthRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_AUTOSTART);
			String autoStart = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
