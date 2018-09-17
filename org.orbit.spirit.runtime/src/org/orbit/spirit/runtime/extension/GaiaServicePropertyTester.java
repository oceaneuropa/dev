package org.orbit.spirit.runtime.extension;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.spirit.runtime.SpiritConstants;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class GaiaServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.spirit.runtime.GaiaServicePropertyTester";

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.GAIA__AUTOSTART);
			String autoStart = (String) properties.get(SpiritConstants.GAIA__AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
