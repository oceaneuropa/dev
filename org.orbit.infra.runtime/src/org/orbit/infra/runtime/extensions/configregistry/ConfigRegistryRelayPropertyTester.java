package org.orbit.infra.runtime.extensions.configregistry;

import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.util.ConfigRegistryConfigPropertiesHandler;
import org.orbit.platform.sdk.ProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.osgi.framework.BundleContext;

public class ConfigRegistryRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.ConfigRegistryRelayPropertyTester";

	public static ConfigRegistryRelayPropertyTester INSTANCE = new ConfigRegistryRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			String autoStart = ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.CONFIG_REGISTRY__RELAY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
