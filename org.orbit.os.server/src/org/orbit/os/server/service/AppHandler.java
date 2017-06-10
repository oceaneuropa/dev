package org.orbit.os.server.service;

import org.orbit.app.AppManifest;
import org.orbit.app.BundleManifest;
import org.origin.common.osgi.BundleUtil;
import org.origin.common.osgi.Dependency;
import org.origin.common.osgi.DependencySet;
import org.origin.common.osgi.DependencySetEvent;
import org.origin.common.osgi.DependencySetListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class AppHandler {

	protected boolean debug = true;
	protected AppManifest appManifest;
	protected BundleTracker<AppBundle> bundleTracker;
	protected App app;

	/**
	 * 
	 * @param appManifest
	 */
	public AppHandler(AppManifest appManifest) {
		this.appManifest = appManifest;
	}

	public AppManifest getAppManifest() {
		return appManifest;
	}

	public App getApp() {
		return app;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public synchronized void open(BundleContext bundleContext) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".open()");
		}

		// (1) Create runtime App.
		this.app = createApp(bundleContext, this.appManifest);

		// (2) Start tracking Bundles of the App.
		this.bundleTracker = new BundleTracker<AppBundle>(bundleContext, Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE, new BundleTrackerCustomizer<AppBundle>() {
			@Override
			public AppBundle addingBundle(Bundle bundle, BundleEvent event) {
				return handleAddingBundle(bundle, event);
			}

			@Override
			public void modifiedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
				handleModifiedBundle(bundle, event, appBundle);
			}

			@Override
			public void removedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
				handleRemovedBundle(bundle, event, appBundle);
			}
		});
		this.bundleTracker.open();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public synchronized void close(BundleContext bundleContext) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".open()");
		}

		// (1) Stop tracking Bundles of the App.
		if (this.bundleTracker != null) {
			this.bundleTracker.close();
			this.bundleTracker = null;
		}

		// (2) Dispose runtime App.
		this.app = null;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @return
	 */
	protected AppBundle handleAddingBundle(Bundle bundle, BundleEvent event) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".handleAddingBundle()");
			BundleUtil.debugBundle(bundle);
			BundleUtil.debugBundleEvent(event);
		}

		AppBundle appBundle = null;

		// Check whether one of the AppBundle of this app matches the added Bundle.
		String bundleName = bundle.getSymbolicName();
		String bundleVersion = bundle.getVersion().toString();
		for (AppBundle currAppBundle : this.app.getAppBundles()) {
			String currBundleName = currAppBundle.getBundleName();
			String currBundleVersion = currAppBundle.getBundleVersion();

			if (bundleName.equals(currBundleName) && bundleVersion.equals(currBundleVersion)) {
				appBundle = currAppBundle;
				break;
			}
		}

		// If a AppBundle is found, set the added Bundle to it.
		// The AppBundle's Dependency will be resolved. When all Dependencies are resolved, the DependencySet will be resolved.
		if (appBundle != null) {
			appBundle.setBundle(bundle);
		}

		return appBundle;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appBundle
	 */
	protected void handleModifiedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".handleModifiedBundle()");
			BundleUtil.debugBundle(bundle);
			BundleUtil.debugBundleEvent(event);
		}

	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appBundle
	 */
	protected void handleRemovedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".handleRemovedBundle()");
			BundleUtil.debugBundle(bundle);
			BundleUtil.debugBundleEvent(event);
		}

		appBundle.setBundle(null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param appFolder
	 * @param appManifest
	 * @return
	 */
	protected App createApp(BundleContext bundleContext, AppManifest appManifest) {
		App app = new App();

		DependencySet dependencySet = new DependencySet();
		app.setDependencySet(dependencySet);
		dependencySet.addListener(new DependencySetListener() {
			@Override
			public void onDependencySetChange(DependencySetEvent event) {
				DependencySet.STATE newState = event.getNewState();
				if (newState.isAllResolved()) {
					// All dependencies are resolved.
					// - start all app bundles
					startApp();

				} else if (newState.isNotAllResolved()) {
					// One or more dependencies are unresolved.
					// - stop all app bundles
					stopApp();
				}
			}
		});

		BundleManifest[] requireBundles = appManifest.getRequireBundles();
		for (BundleManifest requireBundle : requireBundles) {
			String bundleName = requireBundle.getSymbolicName();
			String bundleVersion = requireBundle.getVersion();

			AppBundle appBundle = new AppBundle(bundleName, bundleVersion);
			Dependency dependency = new Dependency(bundleName + "_" + bundleVersion, Bundle.class);
			app.getDependencySet().addDependency(dependency);
			appBundle.setDependency(dependency);

			app.addAppBundle(appBundle);
		}

		// look for existing bundles
		Bundle[] existingBundles = bundleContext.getBundles();
		for (AppBundle appBundle : app.getAppBundles()) {
			Bundle existingBundle = findBundle(existingBundles, appBundle);
			appBundle.setBundle(existingBundle);
		}

		return app;
	}

	/**
	 * 
	 * @param bundles
	 * @param appBundle
	 * @return
	 */
	protected Bundle findBundle(Bundle[] bundles, AppBundle appBundle) {
		Bundle result = null;

		String bundleName = appBundle.getBundleName();
		String bundleVersion = appBundle.getBundleVersion();

		for (Bundle bundle : bundles) {
			String currBundleId = bundle.getSymbolicName();
			String currBundleVersion = bundle.getVersion().toString();
			String currBundleLocation = bundle.getLocation();

			boolean matchBundleId = (currBundleId.equals(bundleName)) ? true : false;
			boolean matchBundleVersion = (currBundleVersion.equals(bundleVersion)) ? true : false;
			boolean matchBundleLocation = false;

			if (matchBundleId && matchBundleVersion) {
				result = bundle;
				break;
			}
		}

		return result;
	}

	/**
	 * 
	 * @param app
	 */
	public void startApp() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".startApp()");
		}

		// start app bundles
		boolean startAppFailed = false;
		for (AppBundle appBundle : this.app.getAppBundles()) {
			if (appBundle.isReady()) {
				try {
					appBundle.start();
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STARTED);

				} catch (Exception e) {
					e.printStackTrace();
					startAppFailed = true;
				}
			} else {
				startAppFailed = true;
			}
		}

		if (startAppFailed) {
			// stop app bundles
			for (AppBundle appBundle : this.app.getAppBundles()) {
				try {
					appBundle.stop();
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOPPED);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.app.setRuntimeState(App.RUNTIME_STATE.NOT_READY);

		} else {
			this.app.setRuntimeState(App.RUNTIME_STATE.STARTED);
		}
	}

	/**
	 * 
	 * @param app
	 */
	public void stopApp() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stopApp()");
		}

		boolean stopAppFailed = false;
		// stop app bundles
		for (AppBundle appBundle : this.app.getAppBundles()) {
			try {
				appBundle.stop();
				appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOPPED);

			} catch (Exception e) {
				e.printStackTrace();
				stopAppFailed = true;
			}
		}

		this.app.setRuntimeState(App.RUNTIME_STATE.STOPPED);
	}

	/**
	 * https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint
	 * 
	 * Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
	 * 
	 * @param bundle
	 */
	protected void stopBundle(Bundle bundle) {
		if ((bundle.getState() & (Bundle.STARTING | Bundle.ACTIVE)) == 0) {
			// not starting, not active, no need to stop
			return;
		}
		try {
			// Modifies the autostart flag of the bundle, too, so the Bundle might not be started after a framework restart.
			bundle.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// Create AppBundle models for app bundle files.
// File[] appBundleFiles = AppUtil.getAppBundleFiles(this.appFolder, this.appManifest);
// for (File appBundleFile : appBundleFiles) {
// try {
// Manifest manifest = BundleUtil.getManifest(appBundleFile);
// String bundleId = BundleUtil.getBundleId(manifest);
// String bundleVersion = BundleUtil.getBundleVersion(manifest);
//
// AppBundle appBundle = new AppBundle(manifest, bundleId, bundleVersion);
// app.addBundle(appBundle);
//
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// for (AppBundle appBundle : app.getAppBundles()) {
// String bundleId = appBundle.getBundleId();
// String bundleVersion = appBundle.getBundleVersion();
//
// BundleDependency bundleDependency = new BundleDependency(bundleId, bundleVersion);
// app.getDependencySet().addDependency(bundleDependency);
//
// Bundle bundle = findBundle(bundles, appBundle);
// if (bundle != null) {
// appBundle.setBundle(bundle);
// bundleDependency.setBundle(bundle);
//
// } else {
// bundleDependency.setBundle(null);
// }
// }
