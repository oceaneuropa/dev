package org.orbit.component.runtime.extensions.missioncontrol;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class MissionControlServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.MissionControlServicePropertyTester";

	public static MissionControlServicePropertyTester INSTANCE = new MissionControlServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_MISSION_CONTROL_AUTOSTART);
			String autoStart = (String) properties.get(OrbitConstants.COMPONENT_MISSION_CONTROL_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
