package org.orbit.component.runtime.extension.appstore;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.platform.sdk.ProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class AppStoreServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.AppStoreServicePropertyTester";

	public static AppStoreServicePropertyTester INSTANCE = new AppStoreServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_APP_STORE_AUTOSTART);
			String autoStart = (String) properties.get(ComponentConstants.COMPONENT_APP_STORE_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
