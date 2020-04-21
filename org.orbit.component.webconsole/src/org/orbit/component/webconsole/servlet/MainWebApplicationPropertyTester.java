package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.ProcessContext;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MainWebApplicationPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.component.webconsole.MainWebApplicationPropertyTester";

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		BundleContext bundleContext = null;
		if (context instanceof ProcessContext) {
			ProcessContext platformContext = (ProcessContext) context;
			bundleContext = platformContext.getBundleContext();
		}
		if (bundleContext != null) {
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, WebConstants.MAIN_WEB_CONSOLE_AUTOSTART);
			String autoStart = (String) properties.get(WebConstants.MAIN_WEB_CONSOLE_AUTOSTART);
			if ("true".equalsIgnoreCase(autoStart)) {
				return true;
			}
		}
		return false;
	}

}
