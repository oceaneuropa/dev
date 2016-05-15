package org.origin.common.osgi;

import java.util.Dictionary;

import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class ConfigurationUtil {

	/**
	 * 
	 * @param configAdmin
	 * @param filter
	 * @return
	 */
	public static Configuration[] getFactoryConfiguration(ConfigurationAdmin configAdmin, String filter) {
		Configuration[] configs = null;
		try {
			configs = configAdmin.listConfigurations(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configs;
	}

	public static void listConfigurations(ConfigurationAdmin configAdmin, String filter) {
		try {
			System.out.println("filter = " + filter);
			Configuration[] allConfigs = configAdmin.listConfigurations(null);
			System.out.println("Configurations:");
			System.out.println("--------------------------------------------------------");
			if (allConfigs != null) {
				for (Configuration config : allConfigs) {
					System.out.println(config);

					String factoryPid = config.getFactoryPid();
					String pid = config.getPid();
					Dictionary<String, Object> properties = config.getProperties();
					System.out.println("\tfactoryPid=" + factoryPid);
					System.out.println("\tpid=" + pid);
					System.out.println("\tproperties=");
					Printer.pl(properties);
				}
			}
			System.out.println("--------------------------------------------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static ConfigurationAdmin getConfigAdmin(BundleContext bundleContext) {
		return getService(bundleContext, ConfigurationAdmin.class);
	}

	public static <T> T getService(BundleContext bundleContext, Class<T> serviceClass) {
		T service = null;
		ServiceReference<T> serviceReference = bundleContext.getServiceReference(serviceClass);
		if (serviceReference != null) {
			service = bundleContext.getService(serviceReference);
		}
		return service;
	}

}
