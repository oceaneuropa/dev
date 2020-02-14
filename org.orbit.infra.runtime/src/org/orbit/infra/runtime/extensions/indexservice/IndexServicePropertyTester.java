package org.orbit.infra.runtime.extensions.indexservice;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.orbit.platform.sdk.ProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServicePropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.infra.runtime.IndexServicePropertyTester";

	protected static Logger LOG = LoggerFactory.getLogger(IndexServicePropertyTester.class);

	public static IndexServicePropertyTester INSTANCE = new IndexServicePropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		LOG.debug("accept()");

		boolean result = false;

		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}

		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);

			Object autoStart = properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
			if (autoStart != null && "true".equalsIgnoreCase(autoStart.toString().trim())) {
				result = true;
			}
		}

		LOG.debug("result = " + result);
		return result;
	}

}
