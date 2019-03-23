package org.orbit.component.runtime.extension.identity;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.platform.sdk.IProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class IdentityServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.IdentityServicePropertyTester";

	public static IdentityServicePropertyTester INSTANCE = new IdentityServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IProcessContext) {
			IProcessContext platformContext = (IProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_AUTOSTART);
			String autoStart = (String) properties.get(ComponentConstants.COMPONENT_IDENTITY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
