package org.orbit.component.runtime.extensions.nodemanagement;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class NodeManagementServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.NodeManagementServicePropertyTester";

	public static NodeManagementServicePropertyTester INSTANCE = new NodeManagementServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IPlatformContext) {
			IPlatformContext platformContext = (IPlatformContext) context;
			bundleContext = platformContext.getBundleContext();
		}

		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_MANAGEMENT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_MANAGEMENT_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_MANAGEMENT_CONTEXT_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_NODE_MANAGEMENT_HOME);

			String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
			String hostURL = (String) properties.get(OrbitConstants.COMPONENT_NODE_MANAGEMENT_HOST_URL);
			String name = (String) properties.get(OrbitConstants.COMPONENT_NODE_MANAGEMENT_NAME);
			String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_NODE_MANAGEMENT_CONTEXT_ROOT);

			if ((globalHostURL != null || hostURL != null) && name != null && contextRoot != null) {
				return true;
			}
		}

		return false;
	}

}
