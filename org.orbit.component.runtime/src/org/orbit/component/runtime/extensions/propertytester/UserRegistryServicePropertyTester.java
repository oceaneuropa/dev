package org.orbit.component.runtime.extensions.propertytester;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class UserRegistryServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.runtime.UserRegistryServicePropertyTester";

	public static UserRegistryServicePropertyTester INSTANCE = new UserRegistryServicePropertyTester();

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
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
			PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

			String globalHostURL = (String) properties.get(OrbitConstants.ORBIT_HOST_URL);
			String hostURL = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_HOST_URL);
			String name = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
			String contextRoot = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_CONTEXT_ROOT);
			String jdbcDriver = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_DRIVER);
			String jdbcURL = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_URL);
			String jdbcUsername = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_USERNAME);
			String jdbcPassword = (String) properties.get(OrbitConstants.COMPONENT_USER_REGISTRY_JDBC_PASSWORD);

			if ((globalHostURL != null || hostURL != null) && name != null && contextRoot != null && jdbcDriver != null && jdbcURL != null && jdbcUsername != null && jdbcPassword != null) {
				return true;
			}
		}

		return false;
	}

}
