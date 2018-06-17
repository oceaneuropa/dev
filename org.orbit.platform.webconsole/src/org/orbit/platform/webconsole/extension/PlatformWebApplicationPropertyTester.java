package org.orbit.platform.webconsole.extension;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.webconsole.WebConstants;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class PlatformWebApplicationPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.platform.webconsole.WebApplicationPropertyTester";

	public static PlatformWebApplicationPropertyTester INSTANCE = new PlatformWebApplicationPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, WebConstants.PLATFORM_WEB_CONSOLE_AUTOSTART);
			String autoStart = (String) properties.get(WebConstants.PLATFORM_WEB_CONSOLE_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
