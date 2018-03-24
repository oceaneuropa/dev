package org.orbit.infra.runtime.extension.channel;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class ChannelServiceRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.ChannelServiceRelayPropertyTester";

	public static ChannelServiceRelayPropertyTester INSTANCE = new ChannelServiceRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_AUTOSTART);
			String autoStart = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_RELAY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
