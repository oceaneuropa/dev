package org.orbit.component.runtime.extension.configregistry;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentsConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.ConfigRegistryServicePropertyTester";

	public static ConfigRegistryServicePropertyTester INSTANCE = new ConfigRegistryServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART);
			String autoStart = (String) properties.get(ComponentsConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
