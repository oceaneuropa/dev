package org.orbit.infra.runtime.extensions.indexservice;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IPlatformContext;
import org.origin.common.extensions.condition.IPropertyTester;
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
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
			String autoStart = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
