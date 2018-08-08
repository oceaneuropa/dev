package org.orbit.component.runtime.extension.appstore;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class AppStoreServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.AppStoreServicePropertyTester";

	public static AppStoreServicePropertyTester INSTANCE = new AppStoreServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_APP_STORE_AUTOSTART);
			String autoStart = (String) properties.get(OrbitConstants.COMPONENT_APP_STORE_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
