package org.apache.felix.fileinstall.internal;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.util.tracker.ServiceTracker;

public class ConfigAdminSupport {

	protected BundleContext bundleContext;
	protected FileInstall fileInstall;

	protected ServiceRegistration<?> managedServiceFactoryRegistration;
	protected ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> configAdminServiceTracker;

	protected Set<String> pids = Collections.synchronizedSet(new HashSet<String>());
	protected Map<Long, ConfigInstaller> configInstallerMap = new HashMap<Long, ConfigInstaller>();

	/**
	 * 
	 * @param bundleContext
	 * @param fileInstall
	 */
	public ConfigAdminSupport(BundleContext bundleContext, FileInstall fileInstall) {
		this.bundleContext = bundleContext;
		this.fileInstall = fileInstall;
	}

	public void start() {
		// 1. Registry ManagedServiceFactory as a service
		ManagedServiceFactory managedServiceFactory = new ManagedServiceFactory() {
			@Override
			public String getName() {
				return "org.apache.felix.fileinstall";
			}

			@Override
			public void updated(String pid, Dictionary<String, ?> dictionary) throws ConfigurationException {
				Printer.pl("ConfigAdminSupport.ManagedServiceFactory.updated()");
				Printer.pl("\t pid = " + pid);
				Printer.pl("\t propertis = ");
				Printer.pl(dictionary);

				// example:
				// ------------------------------------------------------------------------------------------
				// felix.fileinstall.bundles.new.start = true
				// felix.fileinstall.debug = 2
				// felix.fileinstall.dir = /Users/example/osgi/runspace
				// felix.fileinstall.filename = file:/Users/example/osgi/runspace/org.apache.felix.fileinstall-runspace.cfg
				// felix.fileinstall.filter = .*
				// felix.fileinstall.poll = 3000
				// felix.fileinstall.tmpdir = /Users/example/osgi/runspace_tmpdir
				// service.factoryPid = org.apache.felix.fileinstall
				// service.pid = org.apache.felix.fileinstall.687fa7e5-1c8c-4703-9d70-2f956d571c91
				// ------------------------------------------------------------------------------------------

				pids.add(pid);

				Map<String, String> props = new HashMap<String, String>();
				for (Enumeration<String> propEnum = dictionary.keys(); propEnum.hasMoreElements();) {
					String key = propEnum.nextElement();
					props.put(key, dictionary.get(key).toString());
				}

				fileInstall.updated(pid, props);
			}

			@Override
			public void deleted(String pid) {
				Printer.pl("ConfigAdminSupport.ManagedServiceFactory.deleted()");
				Printer.pl("\t pid = " + pid);

				pids.remove(pid);
				fileInstall.deleted(pid);
			}
		};

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, managedServiceFactory.getName()); // service.pid = "org.apache.felix.fileinstall"
		managedServiceFactoryRegistration = bundleContext.registerService(ManagedServiceFactory.class.getName(), managedServiceFactory, props);

		// 2. Track the ConfigurationAdmin service.
		// When ConfigurationAdmin becomes available, create a ConfigInstaller to handle cfg (or config) files in the directories.
		// When ConfigurationAdmin becomes unavailable, stops the ConfigInstaller
		configAdminServiceTracker = new ServiceTracker<ConfigurationAdmin, ConfigurationAdmin>(bundleContext, ConfigurationAdmin.class.getName(), null) {
			@Override
			public ConfigurationAdmin addingService(ServiceReference<ConfigurationAdmin> serviceReference) {
				Printer.pl("ConfigAdminSupport.ServiceTracker.addingService()");
				fileInstall.lock.writeLock().lock();
				try {
					if (fileInstall.stopped) {
						return null;
					}
					ConfigurationAdmin configAdmin = super.addingService(serviceReference);

					long pid = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					Printer.pl("\t pid = " + pid);

					ConfigInstaller configInstaller = new ConfigInstaller(this.context, configAdmin, fileInstall);
					configInstaller.start();
					configInstallerMap.put(pid, configInstaller);

					return configAdmin;
				} finally {
					fileInstall.lock.writeLock().unlock();
				}
			}

			@Override
			public void removedService(ServiceReference<ConfigurationAdmin> serviceReference, ConfigurationAdmin configAdmin) {
				Printer.pl("ConfigAdminSupport.ServiceTracker.removedService()");
				fileInstall.lock.writeLock().lock();
				try {
					if (fileInstall.stopped) {
						return;
					}

					Iterator<String> pidItor = pids.iterator();
					while (pidItor.hasNext()) {
						String pid = pidItor.next();
						Printer.pl("\t pid = " + pid);

						fileInstall.deleted(pid);
						pidItor.remove();
					}

					long pid = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					ConfigInstaller configInstaller = configInstallerMap.remove(pid);
					if (configInstaller != null) {
						configInstaller.stop();
					}

					super.removedService(serviceReference, configAdmin);
				} finally {
					fileInstall.lock.writeLock().unlock();
				}
			}
		};
		configAdminServiceTracker.open();
	}

	public void stop() {
		if (configAdminServiceTracker != null) {
			configAdminServiceTracker.close();
			configAdminServiceTracker = null;
		}
		if (managedServiceFactoryRegistration != null) {
			managedServiceFactoryRegistration.unregister();
			managedServiceFactoryRegistration = null;
		}
	}

}
