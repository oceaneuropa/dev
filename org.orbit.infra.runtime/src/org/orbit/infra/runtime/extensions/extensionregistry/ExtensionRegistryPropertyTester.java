package org.orbit.infra.runtime.extensions.extensionregistry;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.ProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class ExtensionRegistryPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.ExtensionRegistryPropertyTester";

	public static ExtensionRegistryPropertyTester INSTANCE = new ExtensionRegistryPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_EXTENSION_REGISTRY_AUTOSTART);
			String autoStart = (String) properties.get(InfraConstants.COMPONENT_EXTENSION_REGISTRY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
