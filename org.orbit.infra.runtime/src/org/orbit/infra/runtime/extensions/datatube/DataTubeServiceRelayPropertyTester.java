package org.orbit.infra.runtime.extensions.datatube;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.IProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class DataTubeServiceRelayPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.DataTubeServiceRelayPropertyTester";

	public static DataTubeServiceRelayPropertyTester INSTANCE = new DataTubeServiceRelayPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof IProcessContext) {
			IProcessContext platformContext = (IProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATATUBE__RELAY_AUTOSTART);
			String autoStart = (String) properties.get(InfraConstants.DATATUBE__RELAY_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
