package org.orbit.infra.runtime.extension.channelservice;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
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
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_CONTEXT_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_HTTP_PORT);

			String globalHostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
			String hostURL = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_HOST_URL);
			String name = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_NAME);
			String contextRoot = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_CONTEXT_ROOT);
			String httpPort = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_HTTP_PORT);

			if ((globalHostURL != null || hostURL != null) && name != null && contextRoot != null && httpPort != null) {
				return true;
			}
		}
		return false;
	}

}
