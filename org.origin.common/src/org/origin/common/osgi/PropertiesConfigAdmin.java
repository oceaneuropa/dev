package org.origin.common.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * http://enroute.osgi.org/services/org.osgi.service.cm.html
 * http://www.programcreek.com/java-api-examples/index.php?api=org.osgi.service.cm.ConfigurationAdmin
 */
public class PropertiesConfigAdmin {

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> managedServiceFactoryReg;
	protected PropertiesConfigServiceFactory qnameServiceFactory;
	protected ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> configAdminServiceTracker;

	/**
	 * 
	 * @param bundleContext
	 */
	public PropertiesConfigAdmin(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("PropertiesConfigAdmin.start()");

		// Track the ConfigurationAdmin service.
		configAdminServiceTracker = new ServiceTracker<ConfigurationAdmin, ConfigurationAdmin>(bundleContext, ConfigurationAdmin.class.getName(), null) {
			@Override
			public ConfigurationAdmin addingService(ServiceReference<ConfigurationAdmin> serviceReference) {
				ConfigurationAdmin configAdmin = super.addingService(serviceReference);

				// ConfigurationAdmin (service.id='62') is added.
				long configAdminServiceId = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
				System.out.println("PropertiesConfigAdmin.ServiceTracker.addingService() ConfigurationAdmin (service.id='" + configAdminServiceId + "') is added.");

				if (configAdmin != null) {
					if (qnameServiceFactory == null) {
						qnameServiceFactory = new PropertiesConfigServiceFactory(bundleContext);
						qnameServiceFactory.start();
					}
				}

				return configAdmin;
			}

			@Override
			public void removedService(ServiceReference<ConfigurationAdmin> serviceReference, ConfigurationAdmin configAdmin) {
				// ConfigurationAdmin (service.id='62') is removed.
				long configAdminServiceId = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
				System.out.println("PropertiesConfigAdmin.ServiceTracker.removedService() ConfigurationAdmin (service.id='" + configAdminServiceId + "') is removed.");

				if (qnameServiceFactory != null) {
					qnameServiceFactory = new PropertiesConfigServiceFactory(bundleContext);
					qnameServiceFactory.stop();
					qnameServiceFactory = null;
				}

				super.removedService(serviceReference, configAdmin);
			}
		};
		configAdminServiceTracker.open();
	}

	public void stop() {
		System.out.println("PropertiesConfigAdmin.stop()");

		if (this.qnameServiceFactory != null) {
			this.qnameServiceFactory = new PropertiesConfigServiceFactory(this.bundleContext);
			this.qnameServiceFactory.stop();
			this.qnameServiceFactory = null;
		}
	}

}
