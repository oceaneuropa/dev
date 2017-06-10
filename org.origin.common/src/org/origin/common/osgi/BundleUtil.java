package org.origin.common.osgi;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleReference;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.Version;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.FrameworkWiring;

public class BundleUtil {

	// private static final String MANIFEST_FILE_KEY = "META-INF/MANIFEST.MF";

	private static final String CHECKSUM_SUFFIX = ".checksum";

	/**
	 * Get current BundleContext.
	 * 
	 * @return
	 */
	public static BundleContext getBundleContext() {
		Bundle b = ((BundleReference) BundleUtil.class.getClassLoader()).getBundle();
		return b.getBundleContext();
	}

	/**
	 * Check whether a file is a bundle file.
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isBundleFile(File file) {
		Manifest manifest = null;
		try {
			manifest = getManifest(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (manifest != null && manifest.getMainAttributes().getValue(new Attributes.Name("Bundle-SymbolicName")) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Get Manifest from bundle file.
	 * 
	 * @param bundleFile
	 * @return
	 * @throws IOException
	 */
	public static Manifest getManifest(File bundleFile) throws IOException {
		Manifest manifest = null;
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(bundleFile);
			manifest = jarFile.getManifest();
		} finally {
			IOUtil.closeQuietly(jarFile, true);
		}
		return manifest;
	}

	/**
	 * Get Manifest from bundle input stream.
	 * 
	 * @param bundleInputStream
	 * @return
	 * @throws IOException
	 */
	public static Manifest getManifest(InputStream bundleInputStream) throws IOException {
		Manifest manifest = null;
		JarInputStream jarIs = null;
		try {
			jarIs = new JarInputStream(bundleInputStream);
			manifest = jarIs.getManifest();
		} finally {
			IOUtil.closeQuietly(jarIs, true);
		}
		return manifest;
	}

	public static String getBundleId(Manifest manifest) {
		String bundleId = manifest.getMainAttributes().getValue(new Attributes.Name("Bundle-SymbolicName"));
		return bundleId;
	}

	public static String getBundleVersion(Manifest manifest) {
		String bundleVersion = manifest.getMainAttributes().getValue(new Attributes.Name("Bundle-Version"));
		return bundleVersion;
	}

	// ------------------------------------------------------------------------------------
	// Install, refresh, start, stop, update, uninstall bundle.
	// ------------------------------------------------------------------------------------
	/**
	 * Install a bundle jar file.
	 * 
	 * @param bundleContext
	 * @param bundleFile
	 * @param startLevel
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	public static Bundle installBundle(BundleContext bundleContext, File bundleFile, int startLevel) throws IOException, BundleException {
		AtomicBoolean modified = new AtomicBoolean();

		String bundleLocation = bundleFile.toURI().normalize().toString();
		long checksum = FileUtil.getChecksum(bundleFile);

		Bundle bundle = null;
		FileInputStream fis = null;
		BufferedInputStream buffIs = null;
		try {
			fis = new FileInputStream(bundleFile);
			buffIs = new BufferedInputStream(fis);

			bundle = installOrUpdateBundle(bundleContext, bundleLocation, buffIs, checksum, startLevel, modified);

		} finally {
			IOUtil.closeQuietly(buffIs, true);
			IOUtil.closeQuietly(fis, true);
		}
		return modified.get() ? bundle : null;
	}

	/**
	 * Install or update a bundle.
	 * 
	 * @param bundleContext
	 * @param bundleLocation
	 * @param inputStream
	 * @param checksum
	 * @param startLevel
	 * @param modified
	 * @return
	 * @throws IOException
	 * @throws BundleException
	 */
	protected static Bundle installOrUpdateBundle(BundleContext bundleContext, String bundleLocation, BufferedInputStream inputStream, long checksum, int startLevel, AtomicBoolean modified) throws IOException, BundleException {
		inputStream.mark(256 * 1024);

		Manifest manifest = getManifest(inputStream);
		if (manifest == null) {
			throw new BundleException("The bundle " + bundleLocation + " does not have a META-INF/MANIFEST.MF! " + "Make sure, META-INF and MANIFEST.MF are the first 2 entries in your JAR!");
		}

		String bundleSymName = manifest.getMainAttributes().getValue(Constants.BUNDLE_SYMBOLICNAME);
		String bundleVersion = manifest.getMainAttributes().getValue(Constants.BUNDLE_VERSION);

		Version version = bundleVersion == null ? Version.emptyVersion : Version.parseVersion(bundleVersion);

		Bundle[] bundles = bundleContext.getBundles();
		for (Bundle bundle : bundles) {
			if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(bundleSymName)) {
				bundleVersion = bundle.getHeaders().get(Constants.BUNDLE_VERSION);
				Version currVersion = bundleVersion == null ? Version.emptyVersion : Version.parseVersion(bundleVersion);

				if (version.equals(currVersion)) {
					inputStream.reset();

					if (loadChecksum(bundle, bundleContext) != checksum) {
						System.out.println("A bundle with the same symbolic name (" + bundleSymName + ") and version (" + bundleVersion + ") is already installed.  Updating this bundle instead.");
						stopBundle(bundle);

						storeChecksum(bundle, checksum, bundleContext);
						updateBundle(bundle, inputStream);

						modified.set(true);
					}

					return bundle;
				}
			}
		}
		inputStream.reset();

		System.out.println("Installing bundle " + bundleSymName + " / " + version);

		Bundle bundle = bundleContext.installBundle(bundleLocation, inputStream);
		storeChecksum(bundle, checksum, bundleContext);

		modified.set(true);

		// Set default start level at install time, the user can override it if he wants
		if (startLevel > 0) {
			bundle.adapt(BundleStartLevel.class).setStartLevel(startLevel);
		}

		return bundle;
	}

	/**
	 * 
	 * @param bundles
	 * @throws InterruptedException
	 */
	protected void refreshBundles(BundleContext bundleContext, Collection<Bundle> bundles) throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		FrameworkWiring wiring = bundleContext.getBundle(0).adapt(FrameworkWiring.class);
		if (wiring == null) {
			System.out.println("FrameworkWiring is not found. Refresh bundles failed.");
			return;
		}

		wiring.refreshBundles(bundles, new FrameworkListener() {
			@Override
			public void frameworkEvent(FrameworkEvent event) {
				latch.countDown();
			}
		});

		latch.await();
	}

	/**
	 * Tries to start all the bundles which somehow got stopped transiently. The File Install component will only retry the start When
	 * {@link #USE_START_TRANSIENT} is set to true or when a bundle is persistently started. Persistently stopped bundles are ignored.
	 */
	public static void startAllBundles(BundleContext bundleContext, Collection<Bundle> allBundles, boolean useStartTransient, boolean useStartActivationPolicy) {
		FrameworkStartLevel startLevelSvc = bundleContext.getBundle(0).adapt(FrameworkStartLevel.class);

		List<Bundle> bundles = new ArrayList<Bundle>();

		for (Bundle bundle : allBundles) {
			if (bundle != null) {
				if (bundle.getState() != Bundle.STARTING && bundle.getState() != Bundle.ACTIVE && (useStartTransient || bundle.adapt(BundleStartLevel.class).isPersistentlyStarted()) && startLevelSvc.getStartLevel() >= bundle.adapt(BundleStartLevel.class).getStartLevel()) {
					bundles.add(bundle);
				}
			}
		}

		// Starts a bundle and removes it from the Collection when successfully started.
		for (Iterator<Bundle> bundleItor = bundles.iterator(); bundleItor.hasNext();) {
			if (startBundle(bundleContext, bundleItor.next(), useStartTransient, useStartActivationPolicy)) {
				bundleItor.remove();
			}
		}
	}

	/**
	 * Start a bundle, if the framework's startlevel allows it.
	 * 
	 * @param bundleContext
	 * @param bundle
	 *            the bundle to start.
	 * @param useStartTransient
	 * @param useStartActivationPolicy
	 * @return whether the bundle was started.
	 */
	public static boolean startBundle(BundleContext bundleContext, Bundle bundle, boolean useStartTransient, boolean useStartActivationPolicy) {
		if (bundle.getState() == Bundle.UNINSTALLED) {
			// Cannot start a bundle if the bundle is already uninstalled.
			return false;
		}
		if (isFragment(bundle)) {
			// Fragments can never be started.
			return false;
		}

		// Bundles can only be started transient when the start level of the framework is high enough.
		// Persistent (i.e. non-transient) starts will simply make the framework start the bundle when the start level is high enough.
		FrameworkStartLevel fwkStartLevel = bundleContext.getBundle(0).adapt(FrameworkStartLevel.class);
		if (fwkStartLevel.getStartLevel() >= bundle.adapt(BundleStartLevel.class).getStartLevel()) {
			try {
				int options = 0;
				options |= useStartTransient ? Bundle.START_TRANSIENT : 0;
				options |= useStartActivationPolicy ? Bundle.START_ACTIVATION_POLICY : 0;

				bundle.start(options);

				System.out.println("Started bundle: " + bundle.getLocation());
				return true;

			} catch (BundleException e) {
				// Don't log this as an error, instead we start the bundle repeatedly.
				System.out.println("Error while starting bundle: " + bundle.getLocation());
			}
		}
		return false;
	}

	/**
	 * Stop a bundle.
	 * 
	 * @param bundle
	 * @throws BundleException
	 */
	public static void stopBundle(Bundle bundle) throws BundleException {
		if (!isFragment(bundle)) {
			bundle.stop(Bundle.STOP_TRANSIENT);
		}
	}

	/**
	 * Update a bundle.
	 * 
	 * @param bundle
	 * @param inputStream
	 * @throws BundleException
	 */
	public static void updateBundle(Bundle bundle, InputStream inputStream) throws BundleException {
		bundle.update(inputStream);
	}

	/**
	 * 
	 * @param bundle
	 * @throws BundleException
	 */
	public static void uninstallBundle(Bundle bundle) throws BundleException {
		bundle.uninstall();
	}

	/**
	 * Check if a bundle is a fragment.
	 */
	protected static boolean isFragment(Bundle bundle) {
		BundleRevision revision = bundle.adapt(BundleRevision.class);
		return (revision.getTypes() & BundleRevision.TYPE_FRAGMENT) != 0;
	}

	/**
	 * 
	 * @param bundle
	 * @return
	 */
	protected static String getBundleKey(Bundle bundle) {
		return Long.toString(bundle.getBundleId());
	}

	// ------------------------------------------------------------------------------------
	// Checksum
	// ------------------------------------------------------------------------------------
	/**
	 * Stores the checksum into a bundle data file.
	 * 
	 * @param bundle
	 *            The bundle whose checksum must be stored
	 * @param checksum
	 *            the lastModified date to be stored in bc
	 * @param bundleContext
	 *            the FileInstall's bundle context where to store the checksum.
	 */
	public static void storeChecksum(Bundle bundle, long checksum, BundleContext bundleContext) {
		String key = getBundleKey(bundle);
		File file = bundleContext.getDataFile(key + CHECKSUM_SUFFIX);

		DataOutputStream outputStream = null;
		try {
			outputStream = new DataOutputStream(new FileOutputStream(file));
			outputStream.writeLong(checksum);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(outputStream, true);
		}
	}

	/**
	 * Returns the stored checksum of the bundle.
	 * 
	 * @param bundle
	 *            the bundle whose checksum must be returned
	 * @param bundleContext
	 *            the FileInstall's bundle context.
	 * @return the stored checksum of the bundle
	 */
	public static long loadChecksum(Bundle bundle, BundleContext bundleContext) {
		String key = getBundleKey(bundle);
		File file = bundleContext.getDataFile(key + CHECKSUM_SUFFIX);

		FileInputStream fis = null;
		DataInputStream dataIs = null;
		try {
			fis = new FileInputStream(file);
			dataIs = new DataInputStream(fis);

			return dataIs.readLong();

		} catch (Exception e) {
			return Long.MIN_VALUE;

		} finally {
			IOUtil.closeQuietly(dataIs, true);
			IOUtil.closeQuietly(fis, true);
		}
	}

	public static boolean isBundleInstalled(Bundle bundle) {
		if ((Bundle.INSTALLED & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleResolved(Bundle bundle) {
		if ((Bundle.RESOLVED & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStarting(Bundle bundle) {
		if ((Bundle.STARTING & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleActive(Bundle bundle) {
		if ((Bundle.ACTIVE & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStopping(Bundle bundle) {
		if ((Bundle.STOPPING & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleUninstalled(Bundle bundle) {
		if ((Bundle.UNINSTALLED & bundle.getState()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleInstalledEvent(BundleEvent event) {
		if ((BundleEvent.INSTALLED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleUpdatedEvent(BundleEvent event) {
		if ((BundleEvent.UPDATED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleResolvedEvent(BundleEvent event) {
		if ((BundleEvent.RESOLVED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStartingEvent(BundleEvent event) {
		if ((BundleEvent.STARTING & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStartedEvent(BundleEvent event) {
		if ((BundleEvent.STARTED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStoppingEvent(BundleEvent event) {
		if ((BundleEvent.STOPPING & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleStoppedEvent(BundleEvent event) {
		if ((BundleEvent.STOPPED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleUnresolvedEvent(BundleEvent event) {
		if ((BundleEvent.UNRESOLVED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleUninstalledEvent(BundleEvent event) {
		if ((BundleEvent.UNINSTALLED & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static boolean isBundleLazyActivationEvent(BundleEvent event) {
		if ((BundleEvent.LAZY_ACTIVATION & event.getType()) != 0) {
			return true;
		}
		return false;
	}

	public static void debugBundle(Bundle bundle) {
		long id = bundle.getBundleId();
		String name = bundle.getSymbolicName();
		String version = bundle.getVersion().toString();
		String location = bundle.getLocation();
		int state = bundle.getState();

		String stateName = null;
		if (Bundle.INSTALLED == state) { // 2
			stateName = "Bundle.INSTALLED";

		} else if (Bundle.RESOLVED == state) { // 4
			stateName = "Bundle.RESOLVED";

		} else if (Bundle.STARTING == state) { // 8
			stateName = "Bundle.STARTING";

		} else if (Bundle.ACTIVE == state) { // 32
			stateName = "Bundle.ACTIVE";

		} else if (Bundle.STOPPING == state) { // 16
			stateName = "Bundle.STOPPING";

		} else if (Bundle.UNINSTALLED == state) { // 1
			stateName = "Bundle.UNINSTALLED";

		} else {
			stateName = "unknown";
		}

		System.out.println("(" + id + ") " + name + "_" + version + " " + stateName + " (" + state + ") (" + location + ")");
	}

	public static void debugBundleEvent(BundleEvent event) {
		int eventType = event.getType();
		String eventName = null;
		if (BundleEvent.INSTALLED == eventType) { // 1
			eventName = "BundleEvent.INSTALLED";

		} else if (BundleEvent.UPDATED == eventType) { // 8
			eventName = "BundleEvent.UPDATED";

		} else if (BundleEvent.RESOLVED == eventType) { // 32
			eventName = "BundleEvent.RESOLVED";

		} else if (BundleEvent.STARTING == eventType) { // 128
			eventName = "BundleEvent.STARTING";

		} else if (BundleEvent.STARTED == eventType) { // 2
			eventName = "BundleEvent.STARTED";

		} else if (BundleEvent.STOPPING == eventType) { // 256
			eventName = "BundleEvent.STOPPING";

		} else if (BundleEvent.STOPPED == eventType) { // 4
			eventName = "BundleEvent.STOPPED";

		} else if (BundleEvent.UNRESOLVED == eventType) { // 64
			eventName = "BundleEvent.UNRESOLVED";

		} else if (BundleEvent.UNINSTALLED == eventType) { // 16
			eventName = "BundleEvent.UNINSTALLED";

		} else if (BundleEvent.LAZY_ACTIVATION == eventType) { // 512
			eventName = "BundleEvent.LAZY_ACTIVATION";

		} else {
			eventName = "unknown";
		}

		System.out.println(eventName + " (" + eventType + ")");
	}

}
