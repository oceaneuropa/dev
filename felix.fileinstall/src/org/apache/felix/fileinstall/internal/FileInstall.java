/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.fileinstall.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.felix.fileinstall.ArtifactInstaller;
import org.apache.felix.fileinstall.ArtifactListener;
import org.apache.felix.fileinstall.ArtifactTransformer;
import org.apache.felix.fileinstall.ArtifactUrlTransformer;
import org.apache.felix.fileinstall.internal.Util.Logger;
import org.apache.felix.utils.properties.InterpolationHelper;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.FrameworkWiring;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.url.URLStreamHandlerService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This clever little bundle watches a directory and will install any jar file if finds in that directory (as long as it is a valid bundle and not a
 * fragment).
 *
 */
public class FileInstall implements BundleActivator, ServiceTrackerCustomizer<ArtifactListener, ArtifactListener> {

	protected ConfigAdminSupport cmSupport;
	protected Map<ServiceReference, ArtifactListener> listeners;
	protected BundleTransformer bundleTransformer;
	protected BundleContext context;
	protected Map<String, DirectoryWatcher> watchers;
	protected ServiceTracker listenersTracker;
	protected ReadWriteLock lock;
	protected ServiceRegistration urlHandlerRegistration;
	protected volatile boolean stopped;

	public FileInstall() {
		listeners = new TreeMap<ServiceReference, ArtifactListener>();
		bundleTransformer = new BundleTransformer();
		watchers = new HashMap<String, DirectoryWatcher>();
		lock = new ReentrantReadWriteLock();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		println("FileInstall.start()");

		this.context = context;
		lock.writeLock().lock();

		try {
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			props.put("url.handler.protocol", JarDirUrlHandler.PROTOCOL);
			urlHandlerRegistration = context.registerService(URLStreamHandlerService.class.getName(), new JarDirUrlHandler(), props);

			String flt = "(|(" + Constants.OBJECTCLASS + "=" + ArtifactInstaller.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactTransformer.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactUrlTransformer.class.getName() + "))";
			listenersTracker = new ServiceTracker(context, FrameworkUtil.createFilter(flt), this);
			listenersTracker.open();

			try {
				cmSupport = new ConfigAdminSupport(context, this);
			} catch (NoClassDefFoundError e) {
				Util.log(context, Logger.LOG_DEBUG, "ConfigAdmin is not available, some features will be disabled", e);
			}

			// Created the initial configuration
			Hashtable<String, String> initProps = new Hashtable<String, String>();
			set(initProps, DirectoryWatcher.POLL);
			set(initProps, DirectoryWatcher.DIR);
			set(initProps, DirectoryWatcher.LOG_LEVEL);
			set(initProps, DirectoryWatcher.FILTER);
			set(initProps, DirectoryWatcher.TMPDIR);
			set(initProps, DirectoryWatcher.START_NEW_BUNDLES);
			set(initProps, DirectoryWatcher.USE_START_TRANSIENT);
			set(initProps, DirectoryWatcher.NO_INITIAL_DELAY);
			set(initProps, DirectoryWatcher.START_LEVEL);
			set(initProps, DirectoryWatcher.OPTIONAL_SCOPE);

			// check if dir is an array of dirs
			String dirs = initProps.get(DirectoryWatcher.DIR);
			if (dirs != null && dirs.indexOf(',') != -1) {
				StringTokenizer st = new StringTokenizer(dirs, ",");
				int index = 0;
				while (st.hasMoreTokens()) {
					final String dir = st.nextToken().trim();
					initProps.put(DirectoryWatcher.DIR, dir);

					String name = "initial";
					if (index > 0) {
						name = name + index;
					}
					updated(name, new Hashtable<String, String>(initProps));

					index++;
				}
			} else {
				updated("initial", initProps);
			}
		} finally {
			// now notify all the directory watchers to proceed
			// We need this to avoid race conditions observed in FELIX-2791
			lock.writeLock().unlock();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		println("FileInstall.stop()");

		lock.writeLock().lock();
		try {
			urlHandlerRegistration.unregister();

			List<DirectoryWatcher> watchersToClose = new ArrayList<DirectoryWatcher>();
			synchronized (watchers) {
				watchersToClose.addAll(watchers.values());
				watchers.clear();
			}

			for (DirectoryWatcher watcherToClose : watchersToClose) {
				try {
					watcherToClose.close();
				} catch (Exception e) {
					// Ignore
				}
			}

			if (listenersTracker != null) {
				listenersTracker.close();
			}

			if (cmSupport != null) {
				cmSupport.close();
			}
		} finally {
			stopped = true;
			lock.writeLock().unlock();
		}
	}

	@Override
	public ArtifactListener addingService(ServiceReference<ArtifactListener> serviceReference) {
		println("FileInstall.addingService()");

		ArtifactListener listener = (ArtifactListener) context.getService(serviceReference);
		println("\tlistener = " + listener.getClass().getName());

		addListener(serviceReference, listener);
		return listener;
	}

	@Override
	public void modifiedService(ServiceReference<ArtifactListener> reference, ArtifactListener service) {
		println("FileInstall.modifiedService()");

		removeListener(reference, (ArtifactListener) service);
		addListener(reference, (ArtifactListener) service);
	}

	@Override
	public void removedService(ServiceReference<ArtifactListener> serviceReference, ArtifactListener service) {
		println("FileInstall.removedService()");

		removeListener(serviceReference, (ArtifactListener) service);
	}

	// Adapted for FELIX-524
	private void set(Hashtable<String, String> props, String key) {
		String o = context.getProperty(key);
		if (o == null) {
			o = System.getProperty(key.toUpperCase().replace('.', '_'));
			if (o == null) {
				return;
			}
		}
		props.put(key, o);
	}

	/**
	 * 
	 * @param pid
	 *            e.g. "initial" (or "initial1", "initial2", ... when there are multiple dirs)
	 * @param properties
	 */
	public void updated(String pid, Map<String, String> properties) {
		println("FileInstall.updated()");
		println("\t pid = " + pid);
		println("\t propertis = ");
		println(properties);

		InterpolationHelper.performSubstitution(properties, context);

		DirectoryWatcher watcher = null;
		synchronized (watchers) {
			watcher = watchers.get(pid);
			// do not receate watcher if properties are the same
			if (watcher != null && watcher.getProperties().equals(properties)) {
				return;
			}
		}
		if (watcher != null) {
			// distroy exis
			watcher.close();
		}

		watcher = new DirectoryWatcher(this, properties, context);
		watcher.setDaemon(true);
		synchronized (watchers) {
			watchers.put(pid, watcher);
		}
		watcher.start();
	}

	/**
	 * 
	 * @param pid
	 */
	public void deleted(String pid) {
		println("FileInstall.deleted()");
		println("\t pid = " + pid);

		DirectoryWatcher watcher = null;

		synchronized (watchers) {
			watcher = watchers.remove(pid);
		}

		if (watcher != null) {
			watcher.close();
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void updateChecksum(File file) {
		println("FileInstall.updateChecksum()");
		println("\t file = " + file.getAbsolutePath());

		List<DirectoryWatcher> watchersToUpdate = new ArrayList<DirectoryWatcher>();
		synchronized (watchers) {
			watchersToUpdate.addAll(watchers.values());
		}

		for (DirectoryWatcher dirWatcher : watchersToUpdate) {
			dirWatcher.scanner.updateChecksum(file);
		}
	}

	/**
	 * 
	 * @param reference
	 * @param listener
	 */
	protected void addListener(ServiceReference<ArtifactListener> reference, ArtifactListener listener) {
		println("FileInstall.addListener()");
		println("\t listener = " + listener);

		synchronized (listeners) {
			listeners.put(reference, listener);
		}

		long currentStamp = reference.getBundle().getLastModified();

		List<DirectoryWatcher> watchersToNotify = new ArrayList<DirectoryWatcher>();
		synchronized (watchers) {
			watchersToNotify.addAll(watchers.values());
		}

		for (DirectoryWatcher dirWatcher : watchersToNotify) {
			dirWatcher.addListener(listener, currentStamp);
		}
	}

	/**
	 * 
	 * @param reference
	 * @param listener
	 */
	protected void removeListener(ServiceReference<ArtifactListener> reference, ArtifactListener listener) {
		println("FileInstall.removeListener()");
		println("\t listener = " + listener);

		synchronized (listeners) {
			listeners.remove(reference);
		}

		List<DirectoryWatcher> watchersToNotify = new ArrayList<DirectoryWatcher>();
		synchronized (watchers) {
			watchersToNotify.addAll(watchers.values());
		}

		for (DirectoryWatcher dirWatcher : watchersToNotify) {
			dirWatcher.removeListener(listener);
		}
	}

	protected List<ArtifactListener> getListeners() {
		synchronized (listeners) {
			List<ArtifactListener> l = new ArrayList<ArtifactListener>(listeners.values());
			Collections.reverse(l);
			l.add(bundleTransformer);
			return l;
		}
	}

	/**
	 * Convenience to refresh the packages
	 */
	public static void refresh(BundleContext context, Collection<Bundle> bundles) throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		FrameworkWiring wiring = context.getBundle(0).adapt(FrameworkWiring.class);

		wiring.refreshBundles(bundles, new FrameworkListener() {
			@Override
			public void frameworkEvent(FrameworkEvent event) {
				latch.countDown();
			}
		});

		latch.await();
	}

	public class ConfigAdminSupport {
		protected ConfigAdminServiceTracker serviceTracker;
		protected ServiceRegistration registration;

		/**
		 * 
		 * @param context
		 * @param fileInstall
		 */
		public ConfigAdminSupport(BundleContext context, FileInstall fileInstall) {
			serviceTracker = new ConfigAdminServiceTracker(context, fileInstall);

			Hashtable<String, Object> props = new Hashtable<String, Object>();
			props.put(Constants.SERVICE_PID, serviceTracker.getName());

			registration = context.registerService(ManagedServiceFactory.class.getName(), serviceTracker, props);
			serviceTracker.open();
		}

		public void close() {
			serviceTracker.close();
			registration.unregister();
		}

		public class ConfigAdminServiceTracker extends ServiceTracker<ConfigurationAdmin, ConfigurationAdmin> implements ManagedServiceFactory {
			protected FileInstall fileInstall;
			protected Set<String> pids = Collections.synchronizedSet(new HashSet<String>());
			protected Map<Long, ConfigInstaller> configInstallers = new HashMap<Long, ConfigInstaller>();

			/**
			 * 
			 * @param bundleContext
			 * @param fileInstall
			 */
			public ConfigAdminServiceTracker(BundleContext bundleContext, FileInstall fileInstall) {
				super(bundleContext, ConfigurationAdmin.class.getName(), null);
				this.fileInstall = fileInstall;
			}

			/** ManagedServiceFactory */
			@Override
			public String getName() {
				return "org.apache.felix.fileinstall";
			}

			@Override
			public void updated(String pid, Dictionary<String, ?> dictionary) throws ConfigurationException {
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
				pids.remove(pid);

				fileInstall.deleted(pid);
			}

			/** ConfigurationAdmin */
			@Override
			public ConfigurationAdmin addingService(ServiceReference<ConfigurationAdmin> serviceReference) {
				lock.writeLock().lock();
				try {
					if (stopped) {
						return null;
					}
					ConfigurationAdmin configAdmin = super.addingService(serviceReference);

					long id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);

					ConfigInstaller configInstaller = new ConfigInstaller(this.context, configAdmin, fileInstall);
					configInstaller.init();

					configInstallers.put(id, configInstaller);

					return configAdmin;
				} finally {
					lock.writeLock().unlock();
				}
			}

			@Override
			public void removedService(ServiceReference<ConfigurationAdmin> serviceReference, ConfigurationAdmin configAdmin) {
				lock.writeLock().lock();
				try {
					if (stopped) {
						return;
					}

					Iterator<String> pidItor = pids.iterator();
					while (pidItor.hasNext()) {
						String pid = pidItor.next();
						fileInstall.deleted(pid);
						pidItor.remove();
					}

					long id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					ConfigInstaller configInstaller = configInstallers.remove(id);
					if (configInstaller != null) {
						configInstaller.destroy();
					}

					super.removedService(serviceReference, configAdmin);

				} finally {
					lock.writeLock().unlock();
				}
			}
		}
	}

	protected void println(String message) {
		System.out.println(message);
	}

	protected void println(Map<String, String> properties) {
		System.out.println("------------------------------------------------------------------------");
		for (Entry<String, String> entry : properties.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		System.out.println("------------------------------------------------------------------------");
	}

}