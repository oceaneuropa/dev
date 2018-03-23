package org.orbit.infra.runtime.extension.indexservice;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class IndexServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.IndexServicePropertyTester";

	public static IndexServicePropertyTester INSTANCE = new IndexServicePropertyTester();

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
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

			String globalHostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
			String hostURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL);
			String name = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_NAME);
			String contextRoot = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT);
			String jdbcDriver = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_DRIVER);
			String jdbcURL = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_URL);
			String jdbcUsername = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_USERNAME);
			String jdbcPassword = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_JDBC_PASSWORD);

			if ((globalHostURL != null || hostURL != null) && name != null && contextRoot != null && jdbcDriver != null && jdbcURL != null && jdbcUsername != null && jdbcPassword != null) {
				return true;
			}
		}

		return false;
	}

}
