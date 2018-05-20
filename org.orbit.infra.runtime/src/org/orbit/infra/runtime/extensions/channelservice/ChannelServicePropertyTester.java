package org.orbit.infra.runtime.extensions.channelservice;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class ChannelServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.ChannelServicePropertyTester";

	public static ChannelServicePropertyTester INSTANCE = new ChannelServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_AUTOSTART);
			String autoStart = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}