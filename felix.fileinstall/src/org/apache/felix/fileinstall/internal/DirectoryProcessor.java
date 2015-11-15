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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.apache.felix.fileinstall.ArtifactInstaller;
import org.apache.felix.fileinstall.ArtifactListener;
import org.apache.felix.fileinstall.ArtifactTransformer;
import org.apache.felix.fileinstall.ArtifactUrlTransformer;
import org.apache.felix.fileinstall.internal.Util.Logger;
import org.apache.felix.utils.manifest.Clause;
import org.apache.felix.utils.manifest.Parser;
import org.apache.felix.utils.version.VersionRange;
import org.apache.felix.utils.version.VersionTable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.Version;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;

/**
 * -DirectoryWatcher-
 *
 * This class runs a background task that checks a directory for new files or removed files. These files can be configuration files or jars. For jar
 * files, its behavior is defined below: - If there are new jar files, it installs them and optionally starts them. - If it fails to install a jar, it
 * does not try to install it again until the jar has been modified. - If it fail to start a bundle, it attempts to start it in following iterations
 * until it succeeds or the corresponding jar is uninstalled. - If some jar files have been deleted, it uninstalls them. - If some jar files have been
 * updated, it updates them. - If it fails to update a bundle, it tries to update it in following iterations until it is successful. - If any bundle
 * gets updated or uninstalled, it refreshes the framework for the changes to take effect. - If it detects any new installations, uninstallations or
 * updations, it tries to start all the managed bundle unless it has been configured to only install bundles.
 *
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class DirectoryProcessor extends Thread implements BundleListener {

	public final static String FILENAME = "felix.fileinstall.filename";
	public final static String POLL = "felix.fileinstall.poll";
	public final static String DIR = "felix.fileinstall.dir";
	public final static String LOG_LEVEL = "felix.fileinstall.log.level";
	public final static String LOG_DEFAULT = "felix.fileinstall.log.default";
	public final static String TMPDIR = "felix.fileinstall.tmpdir";
	public final static String FILTER = "felix.fileinstall.filter";
	public final static String START_NEW_BUNDLES = "felix.fileinstall.bundles.new.start";
	public final static String USE_START_TRANSIENT = "felix.fileinstall.bundles.startTransient";
	public final static String USE_START_ACTIVATION_POLICY = "felix.fileinstall.bundles.startActivationPolicy";
	public final static String NO_INITIAL_DELAY = "felix.fileinstall.noInitialDelay";
	public final static String DISABLE_CONFIG_SAVE = "felix.fileinstall.disableConfigSave";
	public final static String ENABLE_CONFIG_SAVE = "felix.fileinstall.enableConfigSave";
	public final static String START_LEVEL = "felix.fileinstall.start.level";
	public final static String ACTIVE_LEVEL = "felix.fileinstall.active.level";
	public final static String UPDATE_WITH_LISTENERS = "felix.fileinstall.bundles.updateWithListeners";
	public final static String OPTIONAL_SCOPE = "felix.fileinstall.optionalImportRefreshScope";
	public final static String FRAGMENT_SCOPE = "felix.fileinstall.fragmentRefreshScope";
	public final static String DISABLE_NIO2 = "felix.fileinstall.disableNio2";

	public final static String SCOPE_NONE = "none";
	public final static String SCOPE_MANAGED = "managed";
	public final static String SCOPE_ALL = "all";

	public final static String LOG_STDOUT = "stdout";
	public final static String LOG_JUL = "jul";

	static final SecureRandom random = new SecureRandom();

	protected File javaIoTmpdir = new File(System.getProperty("java.io.tmpdir"));

	protected FileInstall fileInstall;

	protected Map<String, String> properties;
	protected File watchedDirectory;
	protected File tmpDir;
	protected long poll;
	protected int logLevel;
	protected boolean startNewBundles;
	protected boolean useStartTransient;
	protected boolean useStartActivationPolicy;
	protected String filter;
	protected BundleContext context;
	protected String originatingFileName;
	protected boolean noInitialDelay;
	protected int startLevel;
	protected int activeLevel;
	protected boolean updateWithListeners;
	protected String fragmentScope;
	protected String optionalScope;
	protected boolean disableNIO2;

	// Map of all installed artifacts
	protected Map<File, Artifact> fileToArtifactMap = new HashMap<File, Artifact>();

	// The scanner to report files changes
	protected Scanner scanner;

	// Represents files that could not be processed because of a missing artifact listener
	protected Set<File> processingFailureFiles = new HashSet<File>();

	// Represents installed artifacts which need to be started later because they failed to start
	protected Set<Bundle> delayedBundles = new HashSet<Bundle>();

	// Represents artifacts that could not be installed
	protected Map<File, Artifact> installationFailureFiles = new HashMap<File, Artifact>();

	// flag (acces to which must be synchronized) that indicates wheter there's a change in state of system, which may result in an attempt to start
	// the watched bundles
	protected AtomicBoolean stateChanged = new AtomicBoolean();

	/**
	 * 
	 * @param fileInstall
	 * @param properties
	 * @param context
	 */
	public DirectoryProcessor(FileInstall fileInstall, Map<String, String> properties, BundleContext context) {
		super("fileinstall-" + getThreadName(properties));

		this.fileInstall = fileInstall;
		this.properties = properties;
		this.context = context;

		// read properties
		poll = getLong(properties, POLL, 2000);
		logLevel = getInt(properties, LOG_LEVEL, Util.getGlobalLogLevel(context));
		originatingFileName = properties.get(FILENAME);

		watchedDirectory = getFile(properties, DIR, new File("./load"));
		verifyWatchedDir();

		tmpDir = getFile(properties, TMPDIR, null);
		prepareTempDir();

		startNewBundles = getBoolean(properties, START_NEW_BUNDLES, true); // by default, we start bundles.
		useStartTransient = getBoolean(properties, USE_START_TRANSIENT, false); // by default, we start bundles persistently.
		useStartActivationPolicy = getBoolean(properties, USE_START_ACTIVATION_POLICY, true); // by default, we start bundles using activation policy.
		filter = properties.get(FILTER);
		noInitialDelay = getBoolean(properties, NO_INITIAL_DELAY, false);
		startLevel = getInt(properties, START_LEVEL, 0); // by default, do not touch start level
		activeLevel = getInt(properties, ACTIVE_LEVEL, 0); // by default, always scan
		updateWithListeners = getBoolean(properties, UPDATE_WITH_LISTENERS, false); // by default, do not update bundles when listeners are updated
		fragmentScope = properties.get(FRAGMENT_SCOPE);
		optionalScope = properties.get(OPTIONAL_SCOPE);
		disableNIO2 = getBoolean(properties, DISABLE_NIO2, false); // by default, enable NIO API to scan dir

		this.context.addBundleListener(this);

		if (!disableNIO2) {
			try {
				scanner = new WatcherScanner(context, watchedDirectory, filter);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (scanner == null) {
			scanner = new Scanner(watchedDirectory, filter);
		}
	}

	protected void verifyWatchedDir() {
		if (!watchedDirectory.exists()) {
			// Issue #2069: Do not create the directory if it does not exist,
			// instead, warn user and continue. We will automatically start
			// monitoring the dir when it becomes available.
			log(Logger.LOG_WARNING, watchedDirectory + " does not exist, please create it.", null);
		} else if (!watchedDirectory.isDirectory()) {
			log(Logger.LOG_ERROR, "Cannot use " + watchedDirectory + " because it's not a directory", null);
			throw new RuntimeException("File Install can't monitor " + watchedDirectory + " because it is not a directory");
		}
	}

	public static String getThreadName(Map<String, String> properties) {
		return (properties.get(DIR) != null ? properties.get(DIR) : "./load");
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public void start() {
		if (noInitialDelay) {
			log(Logger.LOG_DEBUG, "Starting initial scan", null);
			initializeCurrentManagedBundles();

			Set<File> files = scanner.scan(true);
			if (files != null) {
				try {
					process(files);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		super.start();
	}

	/**
	 * Main run loop, will traverse the directory, and then handle the delta between installed and newly found/lost bundles and configurations.
	 */
	@Override
	public void run() {
		// We must wait for FileInstall to complete initialization to avoid race conditions observed in FELIX-2791
		try {
			fileInstall.lock.readLock().lockInterruptibly();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log(Logger.LOG_INFO, "Watcher for " + watchedDirectory + " exiting because of interruption.", e);
			return;
		}

		try {
			log(Logger.LOG_DEBUG, "{" + POLL + " (ms) = " + poll + ", " + DIR + " = " + watchedDirectory.getAbsolutePath() + ", " + LOG_LEVEL + " = " + logLevel + ", " + START_NEW_BUNDLES + " = " + startNewBundles + ", " + TMPDIR + " = " + tmpDir + ", " + FILTER + " = " + filter + ", " + START_LEVEL + " = " + startLevel + "}", null);

			if (!noInitialDelay) {
				try {
					// enforce a delay before the first directory scan
					Thread.sleep(poll);
				} catch (InterruptedException e) {
					log(Logger.LOG_DEBUG, "Watcher for " + watchedDirectory + " was interrupted while waiting " + poll + " milliseconds for initial directory scan.", e);
					return;
				}
				initializeCurrentManagedBundles();
			}
		} finally {
			fileInstall.lock.readLock().unlock();
		}

		while (!interrupted()) {
			try {
				FrameworkStartLevel startLevelSvc = context.getBundle(0).adapt(FrameworkStartLevel.class);
				// Don't access the disk when the framework is still in a startup phase.
				if (startLevelSvc.getStartLevel() >= activeLevel && context.getBundle(0).getState() == Bundle.ACTIVE) {

					Set<File> files = scanner.scan(false);
					// Check that there is a result. If not, this means that the directory can not be listed, so it's presumably not a valid directory
					// (it may have been deleted by someone). In such case, just sleep
					if (files != null && !files.isEmpty()) {
						process(files);
					}
				}

				// e.g. wait for 2 seconds and scan the dir again
				synchronized (this) {
					wait(poll);
				}

			} catch (InterruptedException e) {
				return;
			} catch (Throwable e) {
				try {
					context.getBundle();
				} catch (IllegalStateException t) {
					// FileInstall bundle has been uninstalled, exiting loop
					return;
				}
				log(Logger.LOG_ERROR, "In main loop, we have serious trouble", e);
			}
		}
	}

	/** BundleListener */
	@Override
	public void bundleChanged(BundleEvent bundleEvent) {
		int type = bundleEvent.getType();

		if (type == BundleEvent.UNINSTALLED) {
			for (Iterator<Artifact> itor = getArtifacts().iterator(); itor.hasNext();) {
				Artifact artifact = itor.next();
				if (artifact.getBundleId() == bundleEvent.getBundle().getBundleId()) {
					log(Logger.LOG_DEBUG, "Bundle " + bundleEvent.getBundle().getBundleId() + " has been uninstalled", null);
					itor.remove();
					break;
				}
			}
		}

		if (type == BundleEvent.INSTALLED || type == BundleEvent.RESOLVED || type == BundleEvent.UNINSTALLED || type == BundleEvent.UNRESOLVED || type == BundleEvent.UPDATED) {
			setStateChanged(true);
		}
	}

	/**
	 * 
	 * @param files
	 * @throws InterruptedException
	 */
	private void process(Set<File> files) throws InterruptedException {
		fileInstall.lock.readLock().lockInterruptibly();
		try {
			doProcess(files);
		} finally {
			fileInstall.lock.readLock().unlock();
		}
	}

	/**
	 * 
	 * @param files
	 * @throws InterruptedException
	 */
	private void doProcess(Set<File> files) throws InterruptedException {
		List<ArtifactListener> atrifactListeners = fileInstall.getArtifactListeners();

		List<Artifact> deletedArtifacts = new ArrayList<Artifact>();
		List<Artifact> modifiedArtifacts = new ArrayList<Artifact>();
		List<Artifact> createdArtifacts = new ArrayList<Artifact>();

		// Try to process again files that could not be processed
		synchronized (processingFailureFiles) {
			files.addAll(processingFailureFiles);
			processingFailureFiles.clear();
		}

		for (File file : files) {
			boolean exists = file.exists();
			Artifact artifact = getArtifact(file);

			if (!exists) {
				// File has been deleted
				if (artifact != null) {
					deleteJaredDirectory(artifact);
					deleteTransformedFile(artifact);
					deletedArtifacts.add(artifact);
				}

			} else {
				// File exists
				File bundleFile = file;
				URL jaredUrl = null;
				try {
					jaredUrl = file.toURI().toURL();
				} catch (MalformedURLException e) {
					// Ignore, can't happen
				}

				// Jar up the directory if needed
				if (file.isDirectory()) {
					prepareTempDir();
					try {
						bundleFile = new File(tmpDir, file.getName() + ".jar");
						Util.jarDir(file, bundleFile);
						jaredUrl = new URL(JarDirUrlHandler.PROTOCOL, null, file.getPath());

					} catch (IOException e) {
						// Notify user of problem, won't retry until the dir is updated.
						log(Logger.LOG_ERROR, "Unable to create jar for: " + file.getAbsolutePath(), e);
						continue;
					}
				}

				if (artifact != null) {
					// File has been modified
					artifact.setChecksum(scanner.getChecksum(file));

					// If there's no listener, this is because this artifact has been installed before fileinstall has been restarted. In this case,
					// try to find a listener.
					if (artifact.getArtifactListener() == null) {
						ArtifactListener artifactListener = findListener(bundleFile, atrifactListeners);
						// If no listener can handle this artifact, we need to defer the processing for this artifact until one is found
						if (artifactListener == null) {
							synchronized (processingFailureFiles) {
								processingFailureFiles.add(file);
							}
							continue;
						}
						artifact.setArtifactListener(artifactListener);
					}

					// If the listener can not handle this file anymore, uninstall the artifact and try as if is was new
					if (!atrifactListeners.contains(artifact.getArtifactListener()) || !artifact.getArtifactListener().canHandle(bundleFile)) {
						deletedArtifacts.add(artifact);

					} else {
						// The listener is still ok
						deleteTransformedFile(artifact);
						artifact.setJaredDirectory(bundleFile);
						artifact.setJaredUrl(jaredUrl);

						if (transformArtifact(artifact)) {
							modifiedArtifacts.add(artifact);
						} else {
							deleteJaredDirectory(artifact);
							deletedArtifacts.add(artifact);
						}
					}

				} else {
					// File has been added.

					// Find the listener.
					ArtifactListener artifactListener = findListener(bundleFile, atrifactListeners);

					// If no listener can handle this artifact, we need to defer the processing for this artifact until one is found
					if (artifactListener == null) {
						synchronized (processingFailureFiles) {
							processingFailureFiles.add(file);
						}
						continue;
					}

					// Create the artifact
					artifact = new Artifact();
					artifact.setFile(file);
					artifact.setJaredDirectory(bundleFile);
					artifact.setJaredUrl(jaredUrl);
					artifact.setArtifactListener(artifactListener);
					artifact.setChecksum(scanner.getChecksum(file));

					if (transformArtifact(artifact)) {
						createdArtifacts.add(artifact);
					} else {
						deleteJaredDirectory(artifact);
					}
				}
			}
		}

		// Handle deleted artifacts

		// do the operations in the following order: uninstall, update, install, refresh & start.
		Collection<Bundle> uninstalledBundles = uninstall(deletedArtifacts);
		Collection<Bundle> updatedBundles = update(modifiedArtifacts);
		Collection<Bundle> installedBundles = install(createdArtifacts);

		if (!uninstalledBundles.isEmpty() || !updatedBundles.isEmpty() || !installedBundles.isEmpty()) {
			Set<Bundle> bundlesToRefresh = new HashSet<Bundle>();
			bundlesToRefresh.addAll(uninstalledBundles);
			bundlesToRefresh.addAll(updatedBundles);
			bundlesToRefresh.addAll(installedBundles);

			findBundlesWithFragmentsToRefresh(bundlesToRefresh);
			findBundlesWithOptionalPackagesToRefresh(bundlesToRefresh);

			if (bundlesToRefresh.size() > 0) {
				// Refresh if any bundle got uninstalled or updated.
				refreshBundles(bundlesToRefresh);

				// set the state to reattempt starting managed bundles which aren't already STARTING or ACTIVE
				setStateChanged(true);
			}
		}

		if (startNewBundles && isStateChanged()) {
			// Try to start all the bundles that are not persistently stopped
			startAllBundles();

			delayedBundles.addAll(installedBundles);
			delayedBundles.removeAll(uninstalledBundles);

			// Try to start newly installed bundles, or bundles which we missed on a previous round.
			// Starts a bundle and removes it from the Collection when successfully started.
			for (Iterator<Bundle> bundleItor = delayedBundles.iterator(); bundleItor.hasNext();) {
				if (startBundle(bundleItor.next())) {
					bundleItor.remove();
				}
			}

			// set the state as unchanged to not re-attempt starting failed bundles
			setStateChanged(false);
		}
	}

	/**
	 * 
	 * @param artifactListener
	 * @param stamp
	 */
	public void addArtifactListener(ArtifactListener artifactListener, long stamp) {
		if (updateWithListeners) {
			for (Artifact artifact : getArtifacts()) {
				if (artifact.getArtifactListener() == null && artifact.getBundleId() > 0) {
					Bundle bundle = context.getBundle(artifact.getBundleId());
					if (bundle != null && bundle.getLastModified() < stamp) {
						File file = artifact.getFile();
						if (artifactListener.canHandle(file)) {
							synchronized (processingFailureFiles) {
								processingFailureFiles.add(file);
							}
						}
					}
				}
			}
		}
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 
	 * @param artifactListener
	 */
	public void removeArtifactListener(ArtifactListener artifactListener) {
		for (Artifact artifact : getArtifacts()) {
			if (artifact.getArtifactListener() == artifactListener) {
				artifact.setArtifactListener(null);
			}
		}
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 
	 * @param file
	 * @param artifactListeners
	 * @return
	 */
	protected ArtifactListener findListener(File file, List<ArtifactListener> artifactListeners) {
		for (ArtifactListener artifactListener : artifactListeners) {
			if (artifactListener.canHandle(file)) {
				return artifactListener;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param artifact
	 * @return
	 */
	protected boolean transformArtifact(Artifact artifact) {
		if (artifact.getArtifactListener() instanceof ArtifactTransformer) {
			prepareTempDir();
			try {
				File transformed = ((ArtifactTransformer) artifact.getArtifactListener()).transform(artifact.getJaredDirectory(), tmpDir);
				if (transformed != null) {
					artifact.setTransformed(transformed);
					return true;
				}
			} catch (Exception e) {
				log(Logger.LOG_WARNING, "Unable to transform artifact: " + artifact.getFile().getAbsolutePath(), e);
			}
			return false;

		} else if (artifact.getArtifactListener() instanceof ArtifactUrlTransformer) {
			try {
				URL url = artifact.getJaredUrl();
				URL transformed = ((ArtifactUrlTransformer) artifact.getArtifactListener()).transform(url);
				if (transformed != null) {
					artifact.setTransformedUrl(transformed);
					return true;
				}
			} catch (Exception e) {
				log(Logger.LOG_WARNING, "Unable to transform artifact: " + artifact.getFile().getAbsolutePath(), e);
			}
			return false;
		}
		return true;
	}

	protected void deleteTransformedFile(Artifact artifact) {
		if (artifact.getTransformed() != null && !artifact.getTransformed().equals(artifact.getFile()) && !artifact.getTransformed().delete()) {
			log(Logger.LOG_WARNING, "Unable to delete transformed artifact: " + artifact.getTransformed().getAbsolutePath(), null);
		}
	}

	protected void deleteJaredDirectory(Artifact artifact) {
		if (artifact.getJaredDirectory() != null && !artifact.getJaredDirectory().equals(artifact.getFile()) && !artifact.getJaredDirectory().delete()) {
			log(Logger.LOG_WARNING, "Unable to delete jared artifact: " + artifact.getJaredDirectory().getAbsolutePath(), null);
		}
	}

	protected void prepareTempDir() {
		if (tmpDir == null) {
			if (!javaIoTmpdir.exists() && !javaIoTmpdir.mkdirs()) {
				throw new IllegalStateException("Unable to create temporary directory " + javaIoTmpdir);
			}
			for (;;) {
				File f = new File(javaIoTmpdir, "fileinstall-" + Long.toString(random.nextLong()));
				if (!f.exists() && f.mkdirs()) {
					tmpDir = f;
					tmpDir.deleteOnExit();
					break;
				}
			}
		} else {
			prepareDir(tmpDir);
		}
	}

	/**
	 * Create the watched directory, if not existing. Throws a runtime exception if the directory cannot be created, or if the provided File parameter
	 * does not refer to a directory.
	 *
	 * @param dir
	 *            The directory File Install will monitor
	 */
	protected void prepareDir(File dir) {
		if (!dir.exists() && !dir.mkdirs()) {
			log(Logger.LOG_ERROR, "Cannot create folder " + dir + ". Is the folder write-protected?", null);
			throw new RuntimeException("Cannot create folder: " + dir);
		}

		if (!dir.isDirectory()) {
			log(Logger.LOG_ERROR, "Cannot use " + dir + " because it's not a directory", null);
			throw new RuntimeException("Cannot start FileInstall using something that is not a directory");
		}
	}

	/**
	 * Log a message and optional throwable. If there is a log service we use it, otherwise we log to the console
	 *
	 * @param message
	 *            The message to log
	 * @param e
	 *            The throwable to log
	 */
	protected void log(int msgLevel, String message, Throwable e) {
		Util.log(context, logLevel, msgLevel, message, e);
	}

	/**
	 * Check if a bundle is a fragment.
	 */
	protected boolean isFragment(Bundle bundle) {
		BundleRevision rev = bundle.adapt(BundleRevision.class);
		return (rev.getTypes() & BundleRevision.TYPE_FRAGMENT) != 0;
	}

	/**
	 * Retrieve a property as a long.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param property
	 *            the name of the property to retrieve
	 * @param dflt
	 *            the default value
	 * @return the property as a long or the default value
	 */
	int getInt(Map<String, String> properties, String property, int dflt) {
		String value = properties.get(property);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				log(Logger.LOG_WARNING, property + " set, but not a int: " + value, null);
			}
		}
		return dflt;
	}

	/**
	 * Retrieve a property as a long.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param property
	 *            the name of the property to retrieve
	 * @param dflt
	 *            the default value
	 * @return the property as a long or the default value
	 */
	long getLong(Map<String, String> properties, String property, long dflt) {
		String value = properties.get(property);
		if (value != null) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				log(Logger.LOG_WARNING, property + " set, but not a long: " + value, null);
			}
		}
		return dflt;
	}

	/**
	 * Retrieve a property as a File.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param property
	 *            the name of the property to retrieve
	 * @param dflt
	 *            the default value
	 * @return the property as a File or the default value
	 */
	File getFile(Map<String, String> properties, String property, File dflt) {
		String value = properties.get(property);
		if (value != null) {
			return new File(value);
		}
		return dflt;
	}

	/**
	 * Retrieve a property as a boolan.
	 *
	 * @param properties
	 *            the properties to retrieve the value from
	 * @param property
	 *            the name of the property to retrieve
	 * @param dflt
	 *            the default value
	 * @return the property as a boolean or the default value
	 */
	boolean getBoolean(Map<String, String> properties, String property, boolean dflt) {
		String value = properties.get(property);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return dflt;
	}

	public void close() {
		this.context.removeBundleListener(this);
		interrupt();
		for (Artifact artifact : getArtifacts()) {
			deleteTransformedFile(artifact);
			deleteJaredDirectory(artifact);
		}
		try {
			scanner.close();
		} catch (IOException e) {
			// Ignore
		}
		try {
			join(10000);
		} catch (InterruptedException ie) {
			// Ignore
		}
	}

	/**
	 * This method goes through all the currently installed bundles and returns information about those bundles whose location refers to a file in our
	 * {@link #watchedDirectory}.
	 */
	protected void initializeCurrentManagedBundles() {
		Bundle[] bundles = this.context.getBundles();
		String watchedDirPath = watchedDirectory.toURI().normalize().getPath();

		Map<File, Long> fileChecksumsMap = new HashMap<File, Long>();
		for (Bundle bundle : bundles) {
			// Convert to a URI because the location of a bundle is typically a URI. At least, that's the case for autostart bundles and bundles
			// installed by fileinstall. Normalisation is needed to ensure that we don't treat e.g. /tmp/foo and /tmp//foo differently.
			String location = bundle.getLocation();

			String path = null;
			if (location != null && !location.equals(Constants.SYSTEM_BUNDLE_LOCATION)) {
				URI uri;
				try {
					uri = new URI(bundle.getLocation()).normalize();
				} catch (URISyntaxException e) {
					// Let's try to interpret the location as a file path
					uri = new File(location).toURI().normalize();
				}

				if (uri.isOpaque() && uri.getSchemeSpecificPart() != null) {
					// blueprint:file:/tmp/foo/baa.jar -> file:/tmp/foo/baa.jar
					// blueprint:mvn:foo.baa/baa/0.0.1 -> mvn:foo.baa/baa/0.0.1
					// wrap:file:/tmp/foo/baa-1.0.jar$Symbolic-Name=baa&Version=1.0 -> file:/tmp/foo/baa-1.0.jar$Symbolic-Name=baa&Version1.0
					final String schemeSpecificPart = uri.getSchemeSpecificPart();
					// extract content behind the 'file:' protocol of scheme specific path
					final int lastIndexOfFileProtocol = schemeSpecificPart.lastIndexOf("file:");
					final int offsetFileProtocol = lastIndexOfFileProtocol >= 0 ? lastIndexOfFileProtocol + "file:".length() : 0;
					final int firstIndexOfDollar = schemeSpecificPart.indexOf("$");
					final int endOfPath = firstIndexOfDollar >= 0 ? firstIndexOfDollar : schemeSpecificPart.length();
					// file:/tmp/foo/baa.jar -> /tmp/foo/baa.jar
					// mvn:foo.baa/baa/0.0.1 -> mvn:foo.baa/baa/0.0.1
					// file:/tmp/foo/baa-1.0.jar$Symbolic-Name=baa&Version=1.0 -> /tmp/foo/baa-1.0.jar
					path = schemeSpecificPart.substring(offsetFileProtocol, endOfPath);

				} else {
					// file:/tmp/foo/baa.jar -> /tmp/foo/baa.jar
					// mnv:foo.baa/baa/0.0.1 -> foo.baa/baa/0.0.1
					path = uri.getPath();
				}
			}

			if (path == null) {
				// jar.getPath is null means we could not parse the location
				// as a meaningful URI or file path.
				// We can't do any meaningful processing for this bundle.
				continue;
			}

			final int index = path.lastIndexOf('/');
			if (index != -1 && path.startsWith(watchedDirPath)) {
				Artifact artifact = new Artifact();
				artifact.setBundleId(bundle.getBundleId());
				artifact.setChecksum(Util.loadChecksum(bundle, context));
				artifact.setArtifactListener(null);
				artifact.setFile(new File(path));
				setArtifact(new File(path), artifact);
				fileChecksumsMap.put(new File(path), artifact.getChecksum());
			}
		}

		scanner.initialize(fileChecksumsMap);
	}

	/**
	 * This method installs a collection of artifacts.
	 * 
	 * @param artifacts
	 *            Collection of {@link Artifact}s to be installed
	 * @return List of Bundles just installed
	 */
	private Collection<Bundle> install(Collection<Artifact> artifacts) {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Artifact artifact : artifacts) {
			Bundle bundle = install(artifact);
			if (bundle != null) {
				bundles.add(bundle);
			}
		}
		return bundles;
	}

	/**
	 * This method uninstalls a collection of artifacts.
	 * 
	 * @param artifacts
	 *            Collection of {@link Artifact}s to be uninstalled
	 * @return Collection of Bundles that got uninstalled
	 */
	private Collection<Bundle> uninstall(Collection<Artifact> artifacts) {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Artifact artifact : artifacts) {
			Bundle bundle = uninstall(artifact);
			if (bundle != null) {
				bundles.add(bundle);
			}
		}
		return bundles;
	}

	/**
	 * This method updates a collection of artifacts.
	 *
	 * @param artifacts
	 *            Collection of {@link Artifact}s to be updated.
	 * @return Collection of bundles that got updated
	 */
	private Collection<Bundle> update(Collection<Artifact> artifacts) {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Artifact artifact : artifacts) {
			Bundle bundle = update(artifact);
			if (bundle != null) {
				bundles.add(bundle);
			}
		}
		return bundles;
	}

	/**
	 * Install an artifact and return the bundle object. It uses {@link Artifact#getFile()} as location of the new bundle. Before installing a file,
	 * it sees if the file has been identified as a bad file in earlier run. If yes, then it compares to see if the file has changed since then. It
	 * installs the file if the file has changed. If the file has not been identified as a bad file in earlier run, then it always installs it.
	 *
	 * @param artifact
	 *            the artifact to be installed
	 * @return Bundle object that was installed
	 */
	private Bundle install(Artifact artifact) {
		File bundleFile = artifact.getFile();
		Bundle bundle = null;

		AtomicBoolean modified = new AtomicBoolean();
		try {
			// If the listener is an installer, ask for an update
			if (artifact.getArtifactListener() instanceof ArtifactInstaller) {
				((ArtifactInstaller) artifact.getArtifactListener()).install(bundleFile);

			} else if (artifact.getArtifactListener() instanceof ArtifactUrlTransformer) {
				// if the listener is an url transformer
				Artifact badArtifact = installationFailureFiles.get(bundleFile);
				if (badArtifact != null && badArtifact.getChecksum() == artifact.getChecksum()) {
					return null; // Don't attempt to install it; nothing has changed.
				}

				URL transformed = artifact.getTransformedUrl();
				String location = transformed.toString();
				BufferedInputStream in = new BufferedInputStream(transformed.openStream());

				try {
					bundle = installOrUpdateBundle(location, in, artifact.getChecksum(), modified);
				} finally {
					in.close();
				}

				artifact.setBundleId(bundle.getBundleId());

			} else if (artifact.getArtifactListener() instanceof ArtifactTransformer) {
				// if the listener is an artifact transformer
				Artifact badArtifact = installationFailureFiles.get(bundleFile);
				if (badArtifact != null && badArtifact.getChecksum() == artifact.getChecksum()) {
					return null; // Don't attempt to install it; nothing has changed.
				}

				File transformed = artifact.getTransformed();
				String location = bundleFile.toURI().normalize().toString();

				BufferedInputStream in = new BufferedInputStream(new FileInputStream(transformed != null ? transformed : bundleFile));
				try {
					bundle = installOrUpdateBundle(location, in, artifact.getChecksum(), modified);
				} finally {
					in.close();
				}
				artifact.setBundleId(bundle.getBundleId());
			}

			installationFailureFiles.remove(bundleFile);

			setArtifact(bundleFile, artifact);

		} catch (Exception e) {
			log(Logger.LOG_ERROR, "Failed to install artifact: " + bundleFile, e);

			// Add it our bad jars list, so that we don't attempt to install it again and again until the underlying jar has been modified.
			installationFailureFiles.put(bundleFile, artifact);
		}

		return modified.get() ? bundle : null;
	}

	/**
	 * 
	 * @param bundleLocation
	 * @param is
	 * @param checksum
	 * @param modified
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	protected Bundle installOrUpdateBundle(String bundleLocation, BufferedInputStream is, long checksum, AtomicBoolean modified) throws IOException, BundleException {
		is.mark(256 * 1024);

		JarInputStream jar = new JarInputStream(is);
		Manifest m = jar.getManifest();
		if (m == null) {
			throw new BundleException("The bundle " + bundleLocation + " does not have a META-INF/MANIFEST.MF! " + "Make sure, META-INF and MANIFEST.MF are the first 2 entries in your JAR!");
		}

		String bundleSymName = m.getMainAttributes().getValue(Constants.BUNDLE_SYMBOLICNAME);
		String bundleVersion = m.getMainAttributes().getValue(Constants.BUNDLE_VERSION);
		Version version = bundleVersion == null ? Version.emptyVersion : Version.parseVersion(bundleVersion);

		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(bundleSymName)) {
				bundleVersion = bundle.getHeaders().get(Constants.BUNDLE_VERSION);
				Version currVersion = bundleVersion == null ? Version.emptyVersion : Version.parseVersion(bundleVersion);
				if (version.equals(currVersion)) {
					is.reset();

					if (Util.loadChecksum(bundle, context) != checksum) {
						log(Logger.LOG_WARNING, "A bundle with the same symbolic name (" + bundleSymName + ") and version (" + bundleVersion + ") is already installed.  Updating this bundle instead.", null);
						stopBundle(bundle);

						Util.storeChecksum(bundle, checksum, context);
						updateBundle(bundle, is);

						modified.set(true);
					}
					return bundle;
				}
			}
		}
		is.reset();

		Util.log(context, Logger.LOG_INFO, "Installing bundle " + bundleSymName + " / " + version, null);

		Bundle b = context.installBundle(bundleLocation, is);
		Util.storeChecksum(b, checksum, context);

		modified.set(true);

		// Set default start level at install time, the user can override it if he wants
		if (startLevel != 0) {
			b.adapt(BundleStartLevel.class).setStartLevel(startLevel);
		}

		return b;
	}

	/**
	 * Uninstall a jar file.
	 */
	protected Bundle uninstall(Artifact artifact) {
		Bundle bundle = null;
		try {
			File bundleFile = artifact.getFile();

			// Find a listener for this artifact if needed
			if (artifact.getArtifactListener() == null) {
				artifact.setArtifactListener(findListener(bundleFile, fileInstall.getArtifactListeners()));
			}

			// Forget this artifact
			removeArtifact(bundleFile);

			// Delete transformed file
			deleteTransformedFile(artifact);

			if (artifact.getArtifactListener() instanceof ArtifactInstaller) {
				// if the listener is an installer, uninstall the artifact
				((ArtifactInstaller) artifact.getArtifactListener()).uninstall(bundleFile);

			} else if (artifact.getBundleId() != 0) {
				// else we need uninstall the bundle
				// old can't be null because of the way we calculate deleted list.
				bundle = context.getBundle(artifact.getBundleId());
				if (bundle == null) {
					log(Logger.LOG_WARNING, "Failed to uninstall bundle: " + bundleFile + " with id: " + artifact.getBundleId() + ". The bundle has already been uninstalled", null);
					return null;
				}

				log(Logger.LOG_INFO, "Uninstalling bundle " + bundle.getBundleId() + " (" + bundle.getSymbolicName() + ")", null);
				uninstallBundle(bundle);
			}

		} catch (Exception e) {
			log(Logger.LOG_WARNING, "Failed to uninstall artifact: " + artifact.getFile(), e);
		}
		return bundle;
	}

	/**
	 * 
	 * @param artifact
	 * @return
	 */
	protected Bundle update(Artifact artifact) {
		Bundle bundle = null;
		try {
			File bundleFile = artifact.getFile();

			if (artifact.getArtifactListener() instanceof ArtifactInstaller) {
				// If the listener is an installer, ask for an update
				((ArtifactInstaller) artifact.getArtifactListener()).update(bundleFile);

			} else if (artifact.getArtifactListener() instanceof ArtifactUrlTransformer) {
				// if the listener is an url transformer
				URL transformed = artifact.getTransformedUrl();
				bundle = context.getBundle(artifact.getBundleId());
				if (bundle == null) {
					log(Logger.LOG_WARNING, "Failed to update bundle: " + bundleFile + " with ID " + artifact.getBundleId() + ". The bundle has been uninstalled", null);
					return null;
				}

				Util.log(context, Logger.LOG_INFO, "Updating bundle " + bundle.getSymbolicName() + " / " + bundle.getVersion(), null);
				stopBundle(bundle);

				Util.storeChecksum(bundle, artifact.getChecksum(), context);

				InputStream is = (transformed != null) ? transformed.openStream() : new FileInputStream(bundleFile);
				try {
					updateBundle(bundle, is);
				} finally {
					is.close();
				}

			} else if (artifact.getArtifactListener() instanceof ArtifactTransformer) {
				// else we need to ask for an update on the bundle
				File transformed = artifact.getTransformed();
				bundle = context.getBundle(artifact.getBundleId());
				if (bundle == null) {
					log(Logger.LOG_WARNING, "Failed to update bundle: " + bundleFile + " with ID " + artifact.getBundleId() + ". The bundle has been uninstalled", null);
					return null;
				}
				Util.log(context, Logger.LOG_INFO, "Updating bundle " + bundle.getSymbolicName() + " / " + bundle.getVersion(), null);

				stopBundle(bundle);

				Util.storeChecksum(bundle, artifact.getChecksum(), context);

				InputStream is = new FileInputStream(transformed != null ? transformed : bundleFile);
				try {
					updateBundle(bundle, is);
				} finally {
					is.close();
				}
			}
		} catch (Throwable t) {
			log(Logger.LOG_WARNING, "Failed to update artifact " + artifact.getFile(), t);
		}
		return bundle;
	}

	/**
	 * Tries to start all the bundles which somehow got stopped transiently. The File Install component will only retry the start When
	 * {@link #USE_START_TRANSIENT} is set to true or when a bundle is persistently started. Persistently stopped bundles are ignored.
	 */
	protected void startAllBundles() {
		FrameworkStartLevel startLevelSvc = context.getBundle(0).adapt(FrameworkStartLevel.class);
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (Artifact artifact : getArtifacts()) {
			if (artifact.getBundleId() > 0) {
				Bundle bundle = context.getBundle(artifact.getBundleId());
				if (bundle != null) {
					if (bundle.getState() != Bundle.STARTING && bundle.getState() != Bundle.ACTIVE && (useStartTransient || bundle.adapt(BundleStartLevel.class).isPersistentlyStarted()) && startLevelSvc.getStartLevel() >= bundle.adapt(BundleStartLevel.class).getStartLevel()) {
						bundles.add(bundle);
					}
				}
			}
		}

		// Starts a bundle and removes it from the Collection when successfully started.
		for (Iterator<Bundle> bundleItor = bundles.iterator(); bundleItor.hasNext();) {
			if (startBundle(bundleItor.next())) {
				bundleItor.remove();
			}
		}
	}

	/**
	 * Start a bundle, if the framework's startlevel allows it.
	 * 
	 * @param bundle
	 *            the bundle to start.
	 * @return whether the bundle was started.
	 */
	protected boolean startBundle(Bundle bundle) {
		FrameworkStartLevel startLevelSvc = context.getBundle(0).adapt(FrameworkStartLevel.class);
		// Fragments can never be started.

		// Bundles can only be started transient when the start level of the framework is high enough. Persistent (i.e. non-transient) starts will
		// simply make the framework start the bundle when the start level is high enough.
		if (startNewBundles && bundle.getState() != Bundle.UNINSTALLED && !isFragment(bundle) && startLevelSvc.getStartLevel() >= bundle.adapt(BundleStartLevel.class).getStartLevel()) {
			try {
				int options = useStartTransient ? Bundle.START_TRANSIENT : 0;
				options |= useStartActivationPolicy ? Bundle.START_ACTIVATION_POLICY : 0;

				bundle.start(options);

				log(Logger.LOG_INFO, "Started bundle: " + bundle.getLocation(), null);

				return true;
			} catch (BundleException e) {
				// Don't log this as an error, instead we start the bundle repeatedly.
				log(Logger.LOG_WARNING, "Error while starting bundle: " + bundle.getLocation(), e);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param bundle
	 * @throws BundleException
	 */
	protected void stopBundle(Bundle bundle) throws BundleException {
		// Stop the bundle transiently so that it will be restarted when startAllBundles() is called but this avoids the need to restart the bundle
		// twice (once for the update and another one when refreshing packages).
		if (startNewBundles) {
			if (!isFragment(bundle)) {
				bundle.stop(Bundle.STOP_TRANSIENT);
			}
		}
	}

	/**
	 * 
	 * @param bundles
	 * @throws InterruptedException
	 */
	protected void refreshBundles(Collection<Bundle> bundles) throws InterruptedException {
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

	/**
	 * 
	 * @param bundle
	 * @param is
	 * @throws BundleException
	 */
	protected void updateBundle(Bundle bundle, InputStream is) throws BundleException {
		bundle.update(is);
	}

	/**
	 * 
	 * @param bundle
	 * @throws BundleException
	 */
	protected void uninstallBundle(Bundle bundle) throws BundleException {
		bundle.uninstall();
	}

	/**
	 * 
	 * @param scope
	 * @return
	 */
	protected Set<Bundle> getScopedBundles(String scope) {
		if (SCOPE_NONE.equals(scope)) {
			// No bundles to check
			return new HashSet<Bundle>();

		} else if (SCOPE_MANAGED.equals(scope)) {
			// Go through managed bundles
			Set<Bundle> bundles = new HashSet<Bundle>();
			for (Artifact artifact : getArtifacts()) {
				if (artifact.getBundleId() > 0) {
					Bundle bundle = context.getBundle(artifact.getBundleId());
					if (bundle != null) {
						bundles.add(bundle);
					}
				}
			}
			return bundles;

		} else {
			// Go through all bundles
			return new HashSet<Bundle>(Arrays.asList(context.getBundles()));
		}
	}

	/**
	 * 
	 * @param toRefresh
	 */
	protected void findBundlesWithFragmentsToRefresh(Set<Bundle> toRefresh) {
		Set<Bundle> fragments = new HashSet<Bundle>();
		Set<Bundle> bundles = getScopedBundles(fragmentScope);
		for (Bundle b : toRefresh) {
			if (b.getState() != Bundle.UNINSTALLED) {
				String hostHeader = b.getHeaders().get(Constants.FRAGMENT_HOST);
				if (hostHeader != null) {
					Clause[] clauses = Parser.parseHeader(hostHeader);
					if (clauses != null && clauses.length > 0) {
						Clause path = clauses[0];
						for (Bundle hostBundle : bundles) {
							if (hostBundle.getSymbolicName() != null && hostBundle.getSymbolicName().equals(path.getName())) {
								String ver = path.getAttribute(Constants.BUNDLE_VERSION_ATTRIBUTE);
								if (ver != null) {
									VersionRange v = VersionRange.parseVersionRange(ver);
									if (v.contains(VersionTable.getVersion(hostBundle.getHeaders().get(Constants.BUNDLE_VERSION)))) {
										fragments.add(hostBundle);
									}
								} else {
									fragments.add(hostBundle);
								}
							}
						}
					}
				}
			}
		}
		toRefresh.addAll(fragments);
	}

	/**
	 * 
	 * @param toRefresh
	 */
	protected void findBundlesWithOptionalPackagesToRefresh(Set<Bundle> toRefresh) {
		Set<Bundle> bundles = getScopedBundles(optionalScope);

		// First pass: include all bundles contained in these features
		bundles.removeAll(toRefresh);
		if (bundles.isEmpty()) {
			return;
		}

		// Second pass: for each bundle, check if there is any unresolved optional package that could be resolved
		Map<Bundle, List<Clause>> imports = new HashMap<Bundle, List<Clause>>();
		for (Iterator<Bundle> it = bundles.iterator(); it.hasNext();) {
			Bundle b = it.next();
			String importsStr = b.getHeaders().get(Constants.IMPORT_PACKAGE);
			List<Clause> importsList = getOptionalImports(importsStr);
			if (importsList.isEmpty()) {
				it.remove();
			} else {
				imports.put(b, importsList);
			}
		}

		if (bundles.isEmpty()) {
			return;
		}

		// Third pass:
		// compute a list of packages that are exported by our bundles and see if some exported packages can be wired to the optional imports
		List<Clause> exports = new ArrayList<Clause>();
		for (Bundle b : toRefresh) {
			if (b.getState() != Bundle.UNINSTALLED) {
				String exportsStr = b.getHeaders().get(Constants.EXPORT_PACKAGE);
				if (exportsStr != null) {
					Clause[] exportsList = Parser.parseHeader(exportsStr);
					exports.addAll(Arrays.asList(exportsList));
				}
			}
		}

		for (Iterator<Bundle> it = bundles.iterator(); it.hasNext();) {
			Bundle b = it.next();
			List<Clause> importsList = imports.get(b);
			for (Iterator<Clause> itpi = importsList.iterator(); itpi.hasNext();) {
				Clause pi = itpi.next();
				boolean matching = false;
				for (Clause pe : exports) {
					if (pi.getName().equals(pe.getName())) {
						String evStr = pe.getAttribute(Constants.VERSION_ATTRIBUTE);
						String ivStr = pi.getAttribute(Constants.VERSION_ATTRIBUTE);
						Version exported = evStr != null ? Version.parseVersion(evStr) : Version.emptyVersion;
						VersionRange imported = ivStr != null ? VersionRange.parseVersionRange(ivStr) : VersionRange.ANY_VERSION;
						if (imported.contains(exported)) {
							matching = true;
							break;
						}
					}
				}
				if (!matching) {
					itpi.remove();
				}
			}
			if (importsList.isEmpty()) {
				it.remove();
				// } else {
				// LOGGER.debug("Refreshing bundle {} ({}) to solve the following optional imports", b.getSymbolicName(), b.getBundleId());
				// for (Clause p : importsList) {
				// LOGGER.debug(" {}", p);
				// }
				//
			}
		}
		toRefresh.addAll(bundles);
	}

	/**
	 * 
	 * @param importsStr
	 * @return
	 */
	protected List<Clause> getOptionalImports(String importsStr) {
		Clause[] imports = Parser.parseHeader(importsStr);
		List<Clause> result = new LinkedList<Clause>();
		for (Clause anImport : imports) {
			String resolution = anImport.getDirective(Constants.RESOLUTION_DIRECTIVE);
			if (Constants.RESOLUTION_OPTIONAL.equals(resolution)) {
				result.add(anImport);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	protected Artifact getArtifact(File file) {
		synchronized (fileToArtifactMap) {
			return fileToArtifactMap.get(file);
		}
	}

	protected List<Artifact> getArtifacts() {
		synchronized (fileToArtifactMap) {
			return new ArrayList<Artifact>(fileToArtifactMap.values());
		}
	}

	protected void setArtifact(File file, Artifact artifact) {
		synchronized (fileToArtifactMap) {
			fileToArtifactMap.put(file, artifact);
		}
	}

	protected void removeArtifact(File file) {
		synchronized (fileToArtifactMap) {
			fileToArtifactMap.remove(file);
		}
	}

	protected void setStateChanged(boolean changed) {
		this.stateChanged.set(changed);
	}

	protected boolean isStateChanged() {
		return stateChanged.get();
	}

}
