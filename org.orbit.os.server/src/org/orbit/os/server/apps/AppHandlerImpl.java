package org.orbit.os.server.apps;

import java.util.ArrayList;
import java.util.List;

import org.orbit.app.AppManifest;
import org.orbit.app.BundleManifest;
import org.origin.common.osgi.BundleHelper;
import org.origin.common.osgi.BundleUtil;
import org.origin.common.osgi.Dependency;
import org.origin.common.osgi.DependencySet;
import org.origin.common.osgi.DependencySetEvent;
import org.origin.common.osgi.DependencySetListener;
import org.origin.common.runtime.Problem;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppHandlerImpl implements AppHandler {

	protected static Logger LOG = LoggerFactory.getLogger(AppHandlerImpl.class);

	protected AppHandler.RUNTIME_STATE runtimeState = AppHandler.RUNTIME_STATE.DEACTIVATED;
	protected AppManifest appManifest;
	protected BundleContext bundleContext;
	protected App app;
	protected BundleTracker<AppBundle> appBundleTracker;
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param appManifest
	 * @param bundleContext
	 */
	public AppHandlerImpl(AppManifest appManifest, BundleContext bundleContext) {
		this.appManifest = appManifest;
		this.bundleContext = bundleContext;
	}

	@Override
	public AppManifest getAppManifest() {
		return this.appManifest;
	}

	@Override
	public App getApp() {
		return this.app;
	}

	@Override
	public AppHandler.RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	protected void setRuntimeState(AppHandler.RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	protected void checkActivated() {
		if (!this.runtimeState.isActivated()) {
			throw new RuntimeException("AppHandler is not activated.");
		}
		if (this.app == null) {
			throw new RuntimeException("App is alredy disposed.");
		}
	}

	@Override
	public synchronized void activate() throws AppException {
		LOG.info("activate()");

		if (this.runtimeState.isActivated()) {
			LOG.info("AppHandler is readly activated.");
			return;
		}
		this.problems.clear();

		// 1. Create App.
		createApp(this.bundleContext, this.appManifest);

		// 2. Start tracking OSGi bundles of the App.
		int bundleState = Bundle.INSTALLED | Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE;
		this.appBundleTracker = new BundleTracker<AppBundle>(bundleContext, bundleState, new BundleTrackerCustomizer<AppBundle>() {
			@Override
			public AppBundle addingBundle(Bundle bundle, BundleEvent event) {
				try {
					if (isAppBundle(bundle, event)) {
						LOG.info("appBundleTracker.addingBundle() " + bundle);

						AppBundle appBundle = handleAddingAppBundle(bundle, event);
						if (appBundle != null) {
							return appBundle;
						}
					}

				} catch (AppException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
				return null;
			}

			@Override
			public void modifiedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
				try {
					LOG.info("appBundleTracker.modifiedBundle() " + appBundle);

					handleModifiedAppBundle(bundle, event, appBundle);

				} catch (AppException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
			}

			@Override
			public void removedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) {
				try {
					LOG.info("appBundleTracker.removedBundle() " + appBundle);

					handleRemovedAppBundle(bundle, event, appBundle);

				} catch (AppException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
			}
		});
		this.appBundleTracker.open();

		setRuntimeState(AppHandler.RUNTIME_STATE.ACTIVATED);
	}

	@Override
	public synchronized void deactivate() throws AppException {
		LOG.info("deactivate()");

		if (this.runtimeState.isDeactivated()) {
			LOG.info("AppHandler is readly deactivated.");
			return;
		}

		// 1. Stop app bundles
		stopApp();

		// 2. Dispose App.
		disposeApp();

		// 3. Stop tracking Bundles of the App.
		// Closing bundle tracker will trigger BundleTracker<AppBundle>.removedBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) method.
		if (this.appBundleTracker != null) {
			this.appBundleTracker.close();
			this.appBundleTracker = null;
		}

		setRuntimeState(AppHandler.RUNTIME_STATE.DEACTIVATED);
	}

	/**
	 * Check whether one of the AppBundle of this app matches the added Bundle.
	 * 
	 * @param bundle
	 * @param event
	 * @return
	 */
	protected boolean isAppBundle(Bundle bundle, BundleEvent event) {
		String bundleName = bundle.getSymbolicName();
		String bundleVersion = bundle.getVersion().toString();
		for (AppBundle currAppBundle : this.app.getAppBundles()) {
			String currBundleName = currAppBundle.getBundleName();
			String currBundleVersion = currAppBundle.getBundleVersion();

			if (bundleName.equals(currBundleName) && bundleVersion.equals(currBundleVersion)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @return
	 * @throws AppException
	 */
	protected AppBundle handleAddingAppBundle(Bundle bundle, BundleEvent event) throws AppException {
		LOG.info("handleAddingAppBundle()");

		AppBundle appBundle = null;

		// 1. Check whether one of the AppBundle of this app matches the added Bundle.
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

		// 2. If a AppBundle is found, set the OSGi Bundle to it.
		// The AppBundle's Dependency will be resolved. When all Dependencies are resolved, the DependencySet will be resolved.
		if (appBundle != null) {
			LOG.info("handleAddingAppBundle() " + BundleHelper.INSTANCE.getSimpleName(event));

			appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOPPED);
			appBundle.setBundle(bundle);
		}

		return appBundle;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appBundle
	 * @throws AppException
	 */
	protected void handleModifiedAppBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) throws AppException {
		LOG.info("handleModifiedAppBundle() " + BundleHelper.INSTANCE.getSimpleName(event));

		String bundleName = BundleHelper.INSTANCE.getSimpleName(bundle);

		if (BundleUtil.isBundleInstalledEvent(event)) {
			LOG.info(bundleName + " is installed.");

		} else if (BundleUtil.isBundleResolvedEvent(event)) {
			LOG.info(bundleName + " is resolved.");

		} else if (BundleUtil.isBundleStartingEvent(event)) {
			LOG.info(bundleName + " is starting.");

		} else if (BundleUtil.isBundleStartedEvent(event)) {
			LOG.info(bundleName + " is started.");

			// Update AppBundle state
			appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STARTED);

			// Update App state
			boolean doMarkStartedIfAllStarted = true;
			if (this.app.getRuntimeState().isDamaged()) {
				// app state remain damaged

			} else if (this.app.getRuntimeState().isStarted()) {
				// app state remain started

			} else if (this.app.getRuntimeState().isStartImperfect()) {
				// mark app as started if all app bundles are started
				doMarkStartedIfAllStarted = true;

			} else if (this.app.getRuntimeState().isStartFailed()) {
				// mark app as started if all app bundles are started
				doMarkStartedIfAllStarted = true;

			} else if (this.app.getRuntimeState().isStopped()) {
				// mark the app as stop imperfect
				this.app.setRuntimeState(App.RUNTIME_STATE.STOP_IMPERFECT);

			} else if (this.app.getRuntimeState().isStopImperfect()) {
				// app state remain stop imperfect

			} else if (this.app.getRuntimeState().isStopFailed()) {
				// app state remain stop failed
			}

			if (doMarkStartedIfAllStarted) {
				boolean allAppBundlesStarted = false;
				for (AppBundle currAppBundle : this.app.getAppBundles()) {
					if (currAppBundle.getRuntimeState().isStarted()) {
						allAppBundlesStarted = true;
					} else {
						allAppBundlesStarted = false;
						break;
					}
				}
				if (allAppBundlesStarted) {
					this.app.setRuntimeState(App.RUNTIME_STATE.STARTED);
				}
			}

		} else if (BundleUtil.isBundleStoppingEvent(event)) {
			LOG.info(bundleName + " is stopping.");

		} else if (BundleUtil.isBundleStoppedEvent(event)) {
			LOG.info(bundleName + " is stopped.");

			// Update AppBundle state
			appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOPPED);

			// Update App state
			boolean doMarkStoppedIfAllStopped = true;
			if (this.app.getRuntimeState().isDamaged()) {
				// app state remain damaged

			} else if (this.app.getRuntimeState().isStarted()) {
				// mark the app as start imperfect
				this.app.setRuntimeState(App.RUNTIME_STATE.START_IMPERFECT);

			} else if (this.app.getRuntimeState().isStartImperfect()) {
				// mark app as stopped if all app bundles are stopped
				doMarkStoppedIfAllStopped = true;

			} else if (this.app.getRuntimeState().isStartFailed()) {
				// app state remain start failed

			} else if (this.app.getRuntimeState().isStopped()) {
				// app state remain stopped

			} else if (this.app.getRuntimeState().isStopImperfect()) {
				// mark app as stopped if all app bundles are stopped
				doMarkStoppedIfAllStopped = true;

			} else if (this.app.getRuntimeState().isStopFailed()) {
				// mark app as stopped if all app bundles are stopped
				doMarkStoppedIfAllStopped = true;
			}

			if (doMarkStoppedIfAllStopped) {
				boolean allAppBundlesStopped = false;
				for (AppBundle currAppBundle : this.app.getAppBundles()) {
					if (currAppBundle.getRuntimeState().isStopped()) {
						allAppBundlesStopped = true;
					} else {
						allAppBundlesStopped = false;
						break;
					}
				}
				if (allAppBundlesStopped) {
					this.app.setRuntimeState(App.RUNTIME_STATE.STOPPED);
				}
			}

		} else if (BundleUtil.isBundleUnresolvedEvent(event)) {
			LOG.info(bundleName + " is unresolved.");

		} else if (BundleUtil.isBundleUninstalledEvent(event)) {
			LOG.info(bundleName + " is uninstalled.");
		}
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appBundle
	 * @throws AppException
	 */
	protected void handleRemovedAppBundle(Bundle bundle, BundleEvent event, AppBundle appBundle) throws AppException {
		LOG.info("handleRemovedAppBundle() " + BundleHelper.INSTANCE.getSimpleName(event));
		LOG.info("handleRemovedAppBundle() Remove OSGi bundle from " + appBundle.getSimpleName());

		appBundle.setBundle(null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param appManifest
	 */
	protected void createApp(BundleContext bundleContext, AppManifest appManifest) {
		LOG.info("createApp()");

		String appId = appManifest.getId();
		String appVersion = appManifest.getVersion();
		this.app = new App(appManifest);

		// (1) Create DependencySet for the app.
		DependencySet dependencySet = new DependencySet();
		this.app.setDependencySet(dependencySet);
		dependencySet.addListener(new DependencySetListener() {
			@Override
			public void onDependencySetChange(DependencySetEvent event) {
				DependencySet.STATE newState = event.getNewState();
				if (newState.isAllResolved()) {
					// All dependencies are resolved.
					// - start all app bundles
					try {
						if (runtimeState.isActivated()) {
							startApp();
						}
					} catch (AppException e) {
						e.printStackTrace();
					}

				} else if (newState.isNotAllResolved()) {
					// One or more dependencies are unresolved.
					// - stop all app bundles
					try {
						if (runtimeState.isActivated()) {
							stopApp();
						}
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// (2) Create AppBundle for the application itself.
		Dependency appDependency = new Dependency(appId + "_" + appVersion, Bundle.class);
		this.app.getDependencySet().addDependency(appDependency);
		AppBundle applicationBundle = new AppBundle(appId, appVersion);
		applicationBundle.setDependency(appDependency);
		applicationBundle.setIsApplication(true);
		this.app.addAppBundle(applicationBundle);

		// (3) Create AppBundles and create one Dependency to each AppBundle.
		BundleManifest[] appBundleManifests = appManifest.getBundles();
		for (BundleManifest appBundleManifest : appBundleManifests) {
			String bundleName = appBundleManifest.getSymbolicName();
			String bundleVersion = appBundleManifest.getVersion();

			Dependency currDependency = new Dependency(bundleName + "_" + bundleVersion, Bundle.class);
			this.app.getDependencySet().addDependency(currDependency);

			AppBundle appBundle = new AppBundle(bundleName, bundleVersion);
			appBundle.setIsModule(true);
			appBundle.setDependency(currDependency);

			this.app.addAppBundle(appBundle);
		}

		// (4) Find OSGi bundle for each AppBundle
		Bundle[] osgiBundles = bundleContext.getBundles();
		for (AppBundle appBundle : this.app.getAppBundles()) {
			String currBundleName = appBundle.getBundleName();
			String currBundleVersion = appBundle.getBundleVersion();

			Bundle osgiBundle = BundleHelper.INSTANCE.findBundle(osgiBundles, currBundleName, currBundleVersion);
			appBundle.setBundle(osgiBundle);
		}
	}

	@Override
	public synchronized void startApp() throws AppException {
		LOG.info("startApp()");

		checkActivated();

		if (this.app.getRuntimeState().isStarted()) {
			LOG.info("startApp() " + getAppManifest().getSimpleName() + " is already fully started.");
			return;
		}

		// 1. Start OSGi bundle of the AppBundles.
		boolean hasDamagedAppBundle = false;
		boolean hasFailedToStartAppBundle = false;
		for (AppBundle appBundle : this.app.getAppBundles()) {
			try {
				if (appBundle.isReady()) {
					startAppBundle(appBundle);
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STARTED);

				} else {
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedAppBundle = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.START_FAILED);
				hasFailedToStartAppBundle = true;
			}
		}

		// 2. Update App runtime state
		if (hasDamagedAppBundle) {
			this.app.setRuntimeState(App.RUNTIME_STATE.DAMAGED);
			LOG.info("startApp() " + this.app.getSimpleName() + " has damaged app bundle.");

		} else if (hasFailedToStartAppBundle) {
			this.app.setRuntimeState(App.RUNTIME_STATE.START_FAILED);
			LOG.info("startApp() " + this.app.getSimpleName() + " has app bundle(s) failed to start.");

		} else {
			boolean hasUnstartedAppBundle = false;
			for (AppBundle appBundle : this.app.getAppBundles()) {
				if (!appBundle.getRuntimeState().isStarted()) {
					hasUnstartedAppBundle = true;
					break;
				}
			}

			if (hasUnstartedAppBundle) {
				this.app.setRuntimeState(App.RUNTIME_STATE.START_IMPERFECT);
				LOG.info("startApp() " + this.app.getSimpleName() + " is imperfectly started.");

			} else {
				this.app.setRuntimeState(App.RUNTIME_STATE.STARTED);
				LOG.info("startApp() " + this.app.getSimpleName() + " is fully started.");
			}
		}
	}

	@Override
	public synchronized void stopApp() throws AppException {
		LOG.info("stopApp()");
		checkActivated();

		if (this.app.getRuntimeState().isStopped()) {
			LOG.info("stopApp() " + getAppManifest().getSimpleName() + " is already fully stopped.");
			return;
		}

		// 1. Stop the OSGi bundle of AppBundles.
		boolean hasDamagedAppBundle = false;
		boolean hasFailedToStopAppBundle = false;
		for (AppBundle appBundle : this.app.getAppBundles()) {
			try {
				if (appBundle.isReady()) {
					stopAppBundle(appBundle);
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOPPED);

				} else {
					appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedAppBundle = true;
				}

			} catch (AppException e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				appBundle.setRuntimeState(AppBundle.RUNTIME_STATE.STOP_FAILED);
				hasFailedToStopAppBundle = true;
			}
		}

		// 2. Update App runtime state
		if (hasDamagedAppBundle) {
			this.app.setRuntimeState(App.RUNTIME_STATE.DAMAGED);
			LOG.info("stopApp() " + this.app.getSimpleName() + " has damaged app bundle.");

		} else if (hasFailedToStopAppBundle) {
			this.app.setRuntimeState(App.RUNTIME_STATE.STOP_FAILED);
			LOG.info("stopApp() " + this.app.getSimpleName() + " has app bundle(s) failed to stop.");

		} else {
			boolean hasUnstoppedAppBundle = false;
			for (AppBundle appBundle : this.app.getAppBundles()) {
				if (!appBundle.getRuntimeState().isStopped()) {
					hasUnstoppedAppBundle = true;
					break;
				}
			}

			if (hasUnstoppedAppBundle) {
				this.app.setRuntimeState(App.RUNTIME_STATE.STOP_IMPERFECT);
				LOG.info("stopApp() " + this.app.getSimpleName() + " is imperfectly stopped.");

			} else {
				this.app.setRuntimeState(App.RUNTIME_STATE.STOPPED);
				LOG.info("stopApp() " + this.app.getSimpleName() + " is full stopped.");
			}
		}
	}

	protected synchronized void disposeApp() {
		LOG.info("disposeApp()");

		if (this.app == null) {
			LOG.info("App is alredy disposed.");
			return;
		}

		// No more DependencySet change event.
		if (this.app.getDependencySet() != null) {
			this.app.getDependencySet().reset();
		}

		// No more Dependency change event.
		for (AppBundle appBundle : this.app.getAppBundles()) {
			if (appBundle.getDependency() != null) {
				appBundle.getDependency().reset();
			}
		}

		// No more app bundles
		this.app.getAppBundles().clear();

		// Reset to the default stopped state
		this.app.setRuntimeState(App.RUNTIME_STATE.STOPPED);

		// No more App
		this.app = null;
	}

	/**
	 * Start OSGi bundle of AppBundle.
	 * 
	 * @param appBundle
	 * @throws AppException
	 */
	protected synchronized void startAppBundle(AppBundle appBundle) throws AppException {
		LOG.info("startAppBundle() " + appBundle.getSimpleName());

		if (appBundle.isReady()) {
			try {
				Bundle bundle = appBundle.getBundle(Bundle.class);
				bundle.start(Bundle.START_TRANSIENT);

			} catch (BundleException e) {
				throw new AppException(e.getMessage(), e);
			}

		} else {
			LOG.info(appBundle.getSimpleName() + " is not ready.");
		}
	}

	/**
	 * Stop OSGi bundle of AppBundle.
	 * 
	 * @param appBundle
	 * @throws AppException
	 */
	protected synchronized void stopAppBundle(AppBundle appBundle) throws AppException {
		LOG.info("stopBundle() " + appBundle.getSimpleName());

		if (appBundle.isReady()) {
			// Stop the bundle only when it is either started or being started.
			Bundle bundle = appBundle.getBundle(Bundle.class);
			if (BundleUtil.isBundleActive(bundle) | BundleUtil.isBundleStarting(bundle)) {
				try {
					// Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
					// https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint

					// Bundle will not auto start next time when stopped this way.
					// Modifies the auto start flag of the bundle, too, so the Bundle will not be auto-started after a framework restart.
					bundle.stop();

				} catch (BundleException e) {
					throw new AppException(e.getMessage(), e);
				}
			}

		} else {
			LOG.info(appBundle.getSimpleName() + " is not ready.");
		}
	}

	@Override
	public List<Problem> getProblems() {
		return this.problems;
	}

	@Override
	public String toString() {
		return "AppHandlerImpl [appManifest=" + this.appManifest.getSimpleName() + "]";
	}

}

// /**
// * Start OSGi bundle of the Application.
// *
// * @param appModule
// * @throws AppException
// */
// protected synchronized void startBundle(App app) throws AppException {
// if (debug) {
// System.out.println(getClass().getName() + ".startBundle() " + app.getSimpleName());
// }
//
// if (app.isReady()) {
// try {
// Bundle bundle = app.getBundle(Bundle.class);
// bundle.start(Bundle.START_TRANSIENT);
//
// } catch (BundleException e) {
// throw new AppException(e.getMessage(), e);
// }
//
// } else {
// if (debug) {
// System.out.println(app.getSimpleName() + " is not ready.");
// }
// }
// }

// /**
// * Stop OSGi bundle of the Application.
// *
// * @param appModule
// * @throws AppException
// */
// protected synchronized void stopBundle(App app) throws AppException {
// if (debug) {
// System.out.println(getClass().getName() + ".stopBundle() " + app.getSimpleName());
// }
//
// if (app.isReady()) {
// // Stop the bundle only when it is either started or being started.
// Bundle bundle = app.getBundle(Bundle.class);
// if (BundleUtil.isBundleActive(bundle) | BundleUtil.isBundleStarting(bundle)) {
// try {
// bundle.stop();
//
// } catch (BundleException e) {
// throw new AppException(e.getMessage(), e);
// }
// }
//
// } else {
// if (debug) {
// System.out.println(app.getSimpleName() + " is not ready.");
// }
// }
// }
