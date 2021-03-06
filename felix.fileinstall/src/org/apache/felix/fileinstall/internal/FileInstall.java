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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.felix.fileinstall.ArtifactInstaller;
import org.apache.felix.fileinstall.ArtifactListener;
import org.apache.felix.fileinstall.ArtifactTransformer;
import org.apache.felix.fileinstall.ArtifactUrlTransformer;
import org.apache.felix.utils.properties.InterpolationHelper;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.url.URLStreamHandlerService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This clever little bundle watches a directory and will install any jar file if finds in that directory (as long as it is a valid bundle and not a
 * fragment).
 *
 */
public class FileInstall implements BundleActivator {

	protected Map<ServiceReference<ArtifactListener>, ArtifactListener> artifactListenersMap;
	protected Map<String, DirectoryProcessor> dirProcessorsMap;
	protected ReadWriteLock lock;
	protected BundleTransformer bundleTransformer;

	protected BundleContext context;
	protected ServiceRegistration<?> urlHandlerRegistration;
	protected ServiceTracker<ArtifactListener, ArtifactListener> listenersTracker;

	protected ConfigAdminSupport configAdminSupport;
	protected volatile boolean stopped; // stopped in stop()

	public FileInstall() {
		artifactListenersMap = new TreeMap<ServiceReference<ArtifactListener>, ArtifactListener>();
		dirProcessorsMap = new HashMap<String, DirectoryProcessor>();
		lock = new ReentrantReadWriteLock();
		bundleTransformer = new BundleTransformer();
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		Printer.pl("FileInstall.start()");

		this.context = context;
		lock.writeLock().lock();

		try {
			// 1. register a osgi URLStreamHandlerService
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			props.put("url.handler.protocol", JarDirUrlHandler.PROTOCOL);
			urlHandlerRegistration = context.registerService(URLStreamHandlerService.class.getName(), new JarDirUrlHandler(), props);

			// 2. track ArtifactListener service (adding/modified/removed)
			String flt = "(|(" + Constants.OBJECTCLASS + "=" + ArtifactInstaller.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactTransformer.class.getName() + ")" + "(" + Constants.OBJECTCLASS + "=" + ArtifactUrlTransformer.class.getName() + "))";
			listenersTracker = new ServiceTracker<ArtifactListener, ArtifactListener>(context, FrameworkUtil.createFilter(flt), new ServiceTrackerCustomizer<ArtifactListener, ArtifactListener>() {
				@Override
				public ArtifactListener addingService(ServiceReference<ArtifactListener> serviceReference) {
					Printer.pl("FileInstall.addingService()");

					ArtifactListener artifactListener = context.getService(serviceReference);
					Printer.pl("\tartifactListener = " + artifactListener.getClass().getName());

					addArtifactListener(serviceReference, artifactListener);
					return artifactListener;
				}

				@Override
				public void modifiedService(ServiceReference<ArtifactListener> reference, ArtifactListener artifactListener) {
					Printer.pl("FileInstall.modifiedService()");

					removeArtifactListener(reference, artifactListener);
					addArtifactListener(reference, artifactListener);
				}

				@Override
				public void removedService(ServiceReference<ArtifactListener> serviceReference, ArtifactListener artifactListener) {
					Printer.pl("FileInstall.removedService()");

					removeArtifactListener(serviceReference, (ArtifactListener) artifactListener);
				}
			});
			listenersTracker.open();

			// 3. when ConfigurationAdmin service is available, register ConfigInstaller as ArtifactInstaller.
			configAdminSupport = new ConfigAdminSupport(context, this);
			configAdminSupport.start();

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

					String name = "runspace";
					if (index > 0) {
						name = name + index;
					}
					updated(name, new Hashtable<String, String>(initProps));

					index++;
				}
			} else {
				updated("runspace", initProps);
			}

		} finally {
			// now notify all the directory watchers to proceed
			// We need this to avoid race conditions observed in FELIX-2791
			lock.writeLock().unlock();
		}
	}

	// Adapted for FELIX-524
	protected void set(Hashtable<String, String> props, String key) {
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
		Printer.pl("FileInstall.stop()");

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

			if (configAdminSupport != null) {
				configAdminSupport.stop();
			}
		} finally {
			stopped = true;
			lock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param pid
	 *            e.g. "initial" (or "initial1", "initial2", ... when there are multiple dirs)
	 * @param properties
	 */
	public void updated(String pid, Map<String, String> properties) {
		Printer.pl("FileInstall.updated()");
		Printer.pl("\t pid = " + pid);
		Printer.pl("\t propertis = ");
		Printer.pl(properties);

		InterpolationHelper.performSubstitution(properties, context);

		DirectoryProcessor dirProcessor = null;
		synchronized (dirProcessorsMap) {
			dirProcessor = dirProcessorsMap.get(pid);

			// If cannot locate dirProcessor with pid (e.g. org.apache.felix.fileinstall.<uuid>), find dirProcessor with same DIR property.
			if (dirProcessor == null && properties != null) {
				String fileinstallDir = properties.get(DirectoryProcessor.DIR);
				for (DirectoryProcessor currDirProcessor : dirProcessorsMap.values()) {
					String currFileinstallDir = currDirProcessor.getProperties().get(DirectoryProcessor.DIR);
					if (fileinstallDir != null && fileinstallDir.equals(currFileinstallDir)) {
						dirProcessor = currDirProcessor;
						break;
					}
				}
			}

			// do not re-create watcher if properties are the same
			if (dirProcessor != null && dirProcessor.getProperties().equals(properties)) {
				return;
			}
		}
		if (dirProcessor != null) {
			dirProcessor.close();
		}

		dirProcessor = new DirectoryProcessor(context, this, properties);
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
		Printer.pl("FileInstall.deleted()");
		Printer.pl("\t pid = " + pid);

		DirectoryProcessor dirProcessor = null;
		synchronized (dirProcessorsMap) {
			dirProcessor = dirProcessorsMap.remove(pid);
		}

		if (dirProcessor != null) {
			dirProcessor.close();
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
		Printer.pl("FileInstall.addArtifactListener()");
		Printer.pl("\t artifactListener = " + artifactListener);

		synchronized (artifactListenersMap) {
			artifactListenersMap.put(reference, artifactListener);
		}

		long currentStamp = reference.getBundle().getLastModified();

		List<DirectoryProcessor> dirProcessors = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			dirProcessors.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirProcessor : dirProcessors) {
			dirProcessor.addArtifactListener(artifactListener, currentStamp);
		}
	}

	/**
	 * 
	 * @param reference
	 * @param artifactListener
	 */
	protected void removeArtifactListener(ServiceReference<ArtifactListener> reference, ArtifactListener artifactListener) {
		Printer.pl("FileInstall.removeListener()");
		Printer.pl("\t artifactListener = " + artifactListener);

		synchronized (artifactListenersMap) {
			artifactListenersMap.remove(reference);
		}

		List<DirectoryProcessor> dirProcessors = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			dirProcessors.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirProcessor : dirProcessors) {
			dirProcessor.removeArtifactListener(artifactListener);
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void updateChecksum(File file) {
		Printer.pl("FileInstall.updateChecksum()");
		Printer.pl("\t file = " + file.getAbsolutePath());

		List<DirectoryProcessor> dirProcessors = new ArrayList<DirectoryProcessor>();
		synchronized (dirProcessorsMap) {
			dirProcessors.addAll(dirProcessorsMap.values());
		}

		for (DirectoryProcessor dirProcessor : dirProcessors) {
			dirProcessor.scanner.updateChecksum(file);
		}
	}

}