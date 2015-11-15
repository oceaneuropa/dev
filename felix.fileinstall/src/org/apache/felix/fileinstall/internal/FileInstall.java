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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.felix.fileinstall.ArtifactInstaller;
import org.apache.felix.fileinstall.ArtifactListener;
import org.apache.felix.fileinstall.ArtifactTransformer;
import org.apache.felix.fileinstall.ArtifactUrlTransformer;
import org.apache.felix.fileinstall.internal.Util.Logger;
import org.apache.felix.utils.properties.InterpolationHelper;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
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

	protected Map<ServiceReference<ArtifactListener>, ArtifactListener> artifactListenersMap;
	protected Map<String, DirectoryProcessor> dirProcessorsMap;
	protected ReadWriteLock lock;
	protected BundleTransformer bundleTransformer;

	protected BundleContext context;
	protected ServiceRegistration urlHandlerRegistration;
	protected ServiceTracker listenersTracker;

	protected ConfigAdminSupport cmSupport;
	protected volatile boolean stopped; // stopped in stop()

	public FileInstall() {
		artifactListenersMap = new TreeMap<ServiceReference<ArtifactListener>, ArtifactListener>();
		dirProcessorsMap = new HashMap<String, DirectoryProcessor>();
		lock = new ReentrantReadWriteLock();
		bundleTransformer = new BundleTransformer();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		println("FileInstall.start()");

		this.context = context;
		lock.writeLock().lock();

		try {
			// 1. register a osgi URLStreamHandlerService
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			props.put("url.handler.protocol", JarDirUrlHandler.PROTOCOL);
			urlHandlerRegistration = context.registerService(URLStreamHandlerService.class.getName(), new JarDirUrlHandler(), props);

			// 2. track ArtifactListener service (adding/modified/removed)
			String flt = "(|(" + Constants.OBJECTCLASS + "=" + ArtifactInstaller.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactTransformer.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactUrlTransformer.class.getName() + "))";
			listenersTracker = new ServiceTracker(context, FrameworkUtil.createFilter(flt), this);
			listenersTracker.open();

			// 3. when ConfigurationAdmin service is available, register ConfigInstaller (ArtifactInstaller) as a service.
			try {
				cmSupport = new ConfigAdminSupport(context, this);
			} catch (NoClassDefFoundError e) {
				Util.log(context, Logger.LOG_DEBUG, "ConfigAdmin is not available, some features will be disabled", e);
			}

			// 4. Created the initial configuration and use it to initialize DirectoryProcessor
			Hashtable<String, String> initProps = new Hashtable<String, String>();
			set(initProps, DirectoryProcessor.POLL);
			set(initProps, DirectoryProcessor.DIR);
			set(initProps, DirectoryProcessor.LOG_LEVEL);
			set(initProps, DirectoryProcessor.FILTER);
			set(initProps, DirectoryProcessor.TMPDIR);
			set(initProps, DirectoryProcessor.START_NEW_BUNDLES);
			set(initProps, DirectoryProcessor.USE_START_TRANSIENT);
			set(initProps, DirectoryProcessor.NO_INITIAL_DELAY);
			set(initProps, DirectoryProcessor.START_LEVEL);
			set(initProps, DirectoryProcessor.OPTIONAL_SCOPE);

			// check if dir is an array of dirs
			String dirs = initProps.get(DirectoryProcessor.DIR);
			if (dirs != null && dirs.indexOf(',') != -1) {
				StringTokenizer st = new StringTokenizer(dirs, ",");
				int index = 0;
				while (st.hasMoreTokens()) {
					final String dir = st.nextToken().trim();
					initProps.put(DirectoryProcessor.DIR, dir);

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

	@Override
	public void stop(BundleContext context) throws Exception {
		println("FileInstall.stop()");

		lock.writeLock().lock();
		try {
			urlHandlerRegistration.unregister();

			List<DirectoryProcessor> dirProcessors = new ArrayList<DirectoryProcessor>();
			synchronized (dirProcessorsMap) {
				dirProcessors.addAll(dirProcessorsMap.values());
				dirProcessorsMap.clear();
			}

			for (DirectoryProcessor dirProcessor : dirProcessors) {
				try {
					dirProcessor.close();
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

		ArtifactListener artifactListener = context.getService(serviceReference);
		println("\tartifactListener = " + artifactListener.getClass().getName());

		addArtifactListener(serviceReference, artifactListener);
		return artifactListener;
	}

	@Override
	public void modifiedService(ServiceReference<ArtifactListener> reference, ArtifactListener artifactListener) {
		println("FileInstall.modifiedService()");

		removeArtifactListener(reference, artifactListener);
		addArtifactListener(reference, artifactListener);
	}

	@Override
	public void removedService(ServiceReference<ArtifactListener> serviceReference, ArtifactListener artifactListener) {
		println("FileInstall.removedService()");

		removeArtifactListener(serviceReference, (ArtifactListener) artifactListener);
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

		DirectoryProcessor dirProcessor = null;
		synchronized (dirProcessorsMap) {
			dirProcessor = dirProcessorsMap.get(pid);
			// do not receate watcher if properties are the same
			if (dirProcessor != null && dirProcessor.getProperties().equals(properties)) {
				return;
			}
		}
		if (dirProcessor != null) {
			dirProcessor.close();
		}

		dirProcessor = new DirectoryProcessor(this, properties, context);
		dirProcessor.setDaemon(true);
		synchronized (dirProcessorsMap) {
			dirProcessorsMap.put(pid, dirProcessor);
		}
		dirProcessor.start();
	}

	/**
	 * 
	 * @param pid
	 */
	public void deleted(String pid) {
		println("FileInstall.deleted()");
		println("\t pid = " + pid);

		DirectoryProcessor watcher = null;
		synchronized (dirProcessorsMap) {
			watcher = dirProcessorsMap.remove(pid);
		}

		if (watcher != null) {
			watcher.close();
		}
	}

	protected List<ArtifactListener> getArtifactListeners() {
		synchronized (artifactListenersMap) {
			List<ArtifactListener> listeners = new ArrayList<ArtifactListener>(artifactListenersMap.values());
			Collections.reverse(listeners);
			listeners.add(bundleTransformer);
			return listeners;
		}
	}

	/**
	 * 
	 * @param reference
	 * @param artifactListener
	 */
	protected void addArtifactListener(ServiceReference<ArtifactListener> reference, ArtifactListener artifactListener) {
		println("FileInstall.addArtifactListener()");
		println("\t artifactListener = " + artifactListener);

		synchronized (artifactListenersMap) {
			artifactListenersMap.put(reference, artifactListener);
		}

		long currentStamp = reference.getBundle().getLastModified();

		List<DirectoryProcessor> watchersToNotify = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			watchersToNotify.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirWatcher : watchersToNotify) {
			dirWatcher.addArtifactListener(artifactListener, currentStamp);
		}
	}

	/**
	 * 
	 * @param reference
	 * @param artifactListener
	 */
	protected void removeArtifactListener(ServiceReference<ArtifactListener> reference, ArtifactListener artifactListener) {
		println("FileInstall.removeListener()");
		println("\t artifactListener = " + artifactListener);

		synchronized (artifactListenersMap) {
			artifactListenersMap.remove(reference);
		}

		List<DirectoryProcessor> watchersToNotify = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			watchersToNotify.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirWatcher : watchersToNotify) {
			dirWatcher.removeArtifactListener(artifactListener);
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void updateChecksum(File file) {
		println("FileInstall.updateChecksum()");
		println("\t file = " + file.getAbsolutePath());

		List<DirectoryProcessor> dirProcessors = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			dirProcessors.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirProcessor : dirProcessors) {
			dirProcessor.scanner.updateChecksum(file);
		}
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

			// e.g. "service.pid" = "org.apache.felix.fileinstall"
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
			protected Map<Long, ConfigInstaller> configInstallerMap = new HashMap<Long, ConfigInstaller>();

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

					long pid = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					println("ConfigAdminServiceTracker.addingService()");
					println("\tpid = " + pid);

					ConfigInstaller configInstaller = new ConfigInstaller(this.context, configAdmin, fileInstall);
					configInstallerMap.put(pid, configInstaller);

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

					println("ConfigAdminServiceTracker.removedService()");
					Iterator<String> pidItor = pids.iterator();
					while (pidItor.hasNext()) {
						String pid = pidItor.next();
						println("\tpid = " + pid);

						fileInstall.deleted(pid);
						pidItor.remove();
					}

					long pid = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					ConfigInstaller configInstaller = configInstallerMap.remove(pid);
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