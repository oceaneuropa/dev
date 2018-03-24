package org.orbit.component.runtime.extensions.missioncontrol;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.OrbitRelayConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class MissionControlRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.MissionControlRelayPropertyTester";

	public static MissionControlRelayPropertyTester INSTANCE = new MissionControlRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_AUTOSTART);
			String autoStart = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
