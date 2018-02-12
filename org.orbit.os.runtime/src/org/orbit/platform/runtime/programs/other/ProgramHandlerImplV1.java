package org.orbit.platform.runtime.programs.other;

import java.util.ArrayList;
import java.util.List;

import org.orbit.platform.api.apps.BundleManifest;
import org.orbit.platform.api.apps.ProgramManifest;
import org.orbit.platform.runtime.programs.Program;
import org.orbit.platform.runtime.programs.ProgramBundle;
import org.orbit.platform.runtime.programs.ProgramException;
import org.orbit.platform.runtime.programs.ProgramHandler;
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

public class ProgramHandlerImplV1 implements ProgramHandler {

	protected boolean debug = true;
	protected ProgramHandler.RUNTIME_STATE runtimeState = ProgramHandler.RUNTIME_STATE.DEACTIVATED;
	protected ProgramManifest appManifest;
	protected BundleContext bundleContext;
	protected Program app;
	protected BundleTracker<ProgramBundle> appModuleBundleTracker;
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param appManifest
	 * @param bundleContext
	 */
	public ProgramHandlerImplV1(ProgramManifest appManifest, BundleContext bundleContext) {
		this.appManifest = appManifest;
		this.bundleContext = bundleContext;
	}

	@Override
	public ProgramManifest getManifest() {
		return this.appManifest;
	}

	@Override
	public Program getProgram() {
		return this.app;
	}

	@Override
	public ProgramHandler.RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	protected void setRuntimeState(ProgramHandler.RUNTIME_STATE runtimeState) {
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

	/**
	 * 
	 * @throws ProgramException
	 */
	@Override
	public synchronized void activate() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activate()");
		}
		if (this.runtimeState.isActivated()) {
			System.out.println("AppHandler is readly activated.");
			return;
		}
		this.problems.clear();

		// (1) Create runtime App.
		createApp(bundleContext, this.appManifest);

		// (2) Start tracking OSGi bundles for the App.
		this.appModuleBundleTracker = new BundleTracker<ProgramBundle>(bundleContext, Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE, new BundleTrackerCustomizer<ProgramBundle>() {
			@Override
			public ProgramBundle addingBundle(Bundle bundle, BundleEvent event) {
				return handleAddingAppModule(bundle, event);
			}

			@Override
			public void modifiedBundle(Bundle bundle, BundleEvent event, ProgramBundle appModule) {
				handleModifiedAppModule(bundle, event, appModule);
			}

			@Override
			public void removedBundle(Bundle bundle, BundleEvent event, ProgramBundle appModule) {
				handleRemovedAppModule(bundle, event, appModule);
			}
		});
		this.appModuleBundleTracker.open();

		setRuntimeState(ProgramHandler.RUNTIME_STATE.ACTIVATED);
	}

	/**
	 * 
	 * @throws ProgramException
	 */
	@Override
	public synchronized void deactivate() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivate()");
		}
		if (this.runtimeState.isDeactivated()) {
			if (debug) {
				System.out.println("AppHandler is readly deactivated.");
			}
			return;
		}

		// (1) Stop app bundles
		if (isAppStarted()) {
			stop();
		}

		// (2) Dispose runtime App.
		disposeApp();

		// (3) Stop tracking Bundles of the App.
		// Closing bundle tracker will trigger BundleTracker<AppModule>.removedBundle(Bundle bundle, BundleEvent event, AppModule appModule) method.
		if (this.appModuleBundleTracker != null) {
			this.appModuleBundleTracker.close();
			this.appModuleBundleTracker = null;
		}

		setRuntimeState(ProgramHandler.RUNTIME_STATE.DEACTIVATED);
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @return
	 */
	protected ProgramBundle handleAddingAppModule(Bundle bundle, BundleEvent event) {
		if (debug) {
			// System.out.print(getClass().getSimpleName() + ".handleAddingAppModule() ");
			// BundleHelper.INSTANCE.debugBundle(bundle);
			// BundleHelper.INSTANCE.debugBundleEvent(event);
		}

		ProgramBundle appModule = null;

		// 1. Check whether one of the AppModule of this app matches the added Bundle.
		String bundleName = bundle.getSymbolicName();
		String bundleVersion = bundle.getVersion().toString();
		for (ProgramBundle currAppModule : this.app.getProgramBundles()) {
			String currModuleName = currAppModule.getBundleName();
			String currModuleVersion = currAppModule.getBundleVersion();

			if (bundleName.equals(currModuleName) && bundleVersion.equals(currModuleVersion)) {
				appModule = currAppModule;
				break;
			}
		}

		// 2. If a AppModule is found, set the OSgi Bundle to it.
		// The AppModule's Dependency will be resolved. When all Dependencies are resolved, the DependencySet will be resolved.
		if (appModule != null) {
			if (debug) {
				System.out.print(getClass().getSimpleName() + ".handleAddingAppModule() ");
				// BundleHelper.INSTANCE.debugBundle(bundle);
				BundleHelper.INSTANCE.debugBundleEvent(event);
			}

			appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);
			appModule.setBundle(bundle);
		}

		return appModule;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appModule
	 */
	protected void handleModifiedAppModule(Bundle bundle, BundleEvent event, ProgramBundle appModule) {
		if (debug) {
			System.out.print(getClass().getSimpleName() + ".handleModifiedAppModule() ");
			// BundleHelper.INSTANCE.debugBundle(bundle);
			// BundleHelper.INSTANCE.debugBundleEvent(event);
		}

		String bundleName = BundleHelper.INSTANCE.getSimpleName(bundle);

		if (BundleUtil.isBundleInstalledEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is installed.");
			}

		} else if (BundleUtil.isBundleResolvedEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is resolved.");
			}

		} else if (BundleUtil.isBundleStartingEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is starting.");
			}

		} else if (BundleUtil.isBundleStartedEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is started.");
			}

			// Update AppModule state
			appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STARTED);

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
				this.app.setRuntimeState(Program.RUNTIME_STATE.STOP_IMPERFECT);

			} else if (this.app.getRuntimeState().isStopImperfect()) {
				// app state remain stop imperfect

			} else if (this.app.getRuntimeState().isStopFailed()) {
				// app state remain stop failed
			}

			if (doMarkStartedIfAllStarted) {
				boolean allAppModulesStarted = false;
				for (ProgramBundle currAppModule : this.app.getProgramBundles()) {
					if (currAppModule.getRuntimeState().isStarted()) {
						allAppModulesStarted = true;
					} else {
						allAppModulesStarted = false;
						break;
					}
				}
				if (allAppModulesStarted) {
					this.app.setRuntimeState(Program.RUNTIME_STATE.STARTED);
				}
			}

		} else if (BundleUtil.isBundleStoppingEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is stopping.");
			}

		} else if (BundleUtil.isBundleStoppedEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is stopped.");
			}

			// Update AppModule state
			appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);

			// Update App state
			boolean doMarkStoppedIfAllStopped = true;
			if (this.app.getRuntimeState().isDamaged()) {
				// app state remain damaged

			} else if (this.app.getRuntimeState().isStarted()) {
				// mark the app as start imperfect
				this.app.setRuntimeState(Program.RUNTIME_STATE.START_IMPERFECT);

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
				boolean allAppModulesStopped = false;
				for (ProgramBundle currAppModule : this.app.getProgramBundles()) {
					if (currAppModule.getRuntimeState().isStopped()) {
						allAppModulesStopped = true;
					} else {
						allAppModulesStopped = false;
						break;
					}
				}
				if (allAppModulesStopped) {
					this.app.setRuntimeState(Program.RUNTIME_STATE.STOPPED);
				}
			}

		} else if (BundleUtil.isBundleUnresolvedEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is unresolved.");
			}

		} else if (BundleUtil.isBundleUninstalledEvent(event)) {
			if (debug) {
				System.out.println(bundleName + " is uninstalled.");
			}
		}
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param appModule
	 */
	protected void handleRemovedAppModule(Bundle bundle, BundleEvent event, ProgramBundle appModule) {
		if (debug) {
			System.out.print(getClass().getSimpleName() + ".handleRemovedAppModule() ");
			// BundleHelper.INSTANCE.debugBundle(bundle);
			// BundleHelper.INSTANCE.debugBundleEvent(event);
		}

		if (debug) {
			System.out.println("Remove OSGi bundle from " + appModule.getSimpleName());
		}
		appModule.setBundle(null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param appManifest
	 */
	protected void createApp(BundleContext bundleContext, ProgramManifest appManifest) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".createApp()");
		}

		this.app = new Program(appManifest);

		// 1. Create DependencySet for the app.
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
							start();
						}
					} catch (ProgramException e) {
						e.printStackTrace();
					}

				} else if (newState.isNotAllResolved()) {
					// One or more dependencies are unresolved.
					// - stop all app bundles
					try {
						if (runtimeState.isActivated()) {
							stop();
						}
					} catch (ProgramException e) {
						e.printStackTrace();
					}
				}
			}
		});

		// 2. Create AppModules and create Dependency to each AppModule.
		BundleManifest[] appBundleManifests = appManifest.getBundles();
		for (BundleManifest appBundleManifest : appBundleManifests) {
			String bundleName = appBundleManifest.getSymbolicName();
			String bundleVersion = appBundleManifest.getVersion();

			Dependency dependency = new Dependency(bundleName + "_" + bundleVersion, Bundle.class);
			this.app.getDependencySet().addDependency(dependency);

			ProgramBundle appModule = new ProgramBundle(bundleName, bundleVersion);
			appModule.setDependency(dependency);

			this.app.addProgramBundle(appModule);
		}

		// (3) Find OSGi bundle for each AppModule
		Bundle[] osgiBundles = bundleContext.getBundles();
		for (ProgramBundle appModule : this.app.getProgramBundles()) {
			String currModuleName = appModule.getBundleName();
			String currModuleVersion = appModule.getBundleVersion();

			Bundle osgiBundle = BundleHelper.INSTANCE.findBundle(osgiBundles, currModuleName, currModuleVersion);
			appModule.setBundle(osgiBundle);
		}
	}

	public boolean isAppStarted() {
		if (this.app != null && this.app.getRuntimeState().isStarted()) {
			return true;
		}
		return false;
	}

	protected void checkAppStarted() {
		if (!isAppStarted()) {
			throw new RuntimeException("App is not started.");
		}
	}

	@Override
	public synchronized void start() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".startApp()");
		}
		checkActivated();

		if (isAppStarted()) {
			if (debug) {
				System.out.println(getManifest().getSimpleName() + " is ready started.");
			}
		}

		// 1. Start all app modules
		boolean hasDamagedAppModule = false;
		boolean hasFailedToStartAppModule = false;
		for (ProgramBundle appModule : this.app.getProgramBundles()) {
			try {
				if (appModule.isReady()) {
					startAppModule(appModule);
					appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STARTED);

				} else {
					appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedAppModule = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.START_FAILED);
				hasFailedToStartAppModule = true;
			}
		}

		// 2. Update App runtime state
		if (hasDamagedAppModule) {
			this.app.setRuntimeState(Program.RUNTIME_STATE.DAMAGED);
			if (debug) {
				System.out.println(this.app.getSimpleName() + " has damaged app bundle.");
			}

		} else if (hasFailedToStartAppModule) {
			this.app.setRuntimeState(Program.RUNTIME_STATE.START_FAILED);
			if (debug) {
				System.out.println(this.app.getSimpleName() + " has app module(s) failed to start.");
			}

		} else {
			boolean hasUnstartedAppModule = false;
			for (ProgramBundle appModule : this.app.getProgramBundles()) {
				if (!appModule.getRuntimeState().isStarted()) {
					hasUnstartedAppModule = true;
					break;
				}
			}

			if (hasUnstartedAppModule) {
				this.app.setRuntimeState(Program.RUNTIME_STATE.START_IMPERFECT);
				if (debug) {
					System.out.println(this.app.getSimpleName() + " is imperfectly started.");
				}

			} else {
				this.app.setRuntimeState(Program.RUNTIME_STATE.STARTED);
				if (debug) {
					System.out.println(this.app.getSimpleName() + " is fully started.");
				}
			}
		}
	}

	@Override
	public synchronized void stop() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stopApp()");
		}
		checkActivated();

		if (!isAppStarted()) {
			if (debug) {
				System.out.println(getManifest().getSimpleName() + " is ready stopped.");
			}
		}

		// (1) Stop all app bundles
		boolean hasDamagedAppModule = false;
		boolean hasFailedToStopAppModule = false;
		for (ProgramBundle appModule : this.app.getProgramBundles()) {
			try {
				if (appModule.isReady()) {
					stopAppModule(appModule);
					appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);

				} else {
					appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedAppModule = true;
				}

			} catch (ProgramException e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				appModule.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOP_FAILED);
				hasFailedToStopAppModule = true;
			}
		}

		// (2) Update App runtime state
		if (hasDamagedAppModule) {
			this.app.setRuntimeState(Program.RUNTIME_STATE.DAMAGED);
			if (debug) {
				System.out.println(this.app.getSimpleName() + " has damaged app bundle.");
			}

		} else if (hasFailedToStopAppModule) {
			this.app.setRuntimeState(Program.RUNTIME_STATE.STOP_FAILED);
			if (debug) {
				System.out.println(this.app.getSimpleName() + " has app module(s) failed to stop.");
			}

		} else {
			boolean hasUnstoppedAppModule = false;
			for (ProgramBundle appModule : this.app.getProgramBundles()) {
				if (!appModule.getRuntimeState().isStopped()) {
					hasUnstoppedAppModule = true;
					break;
				}
			}

			if (hasUnstoppedAppModule) {
				this.app.setRuntimeState(Program.RUNTIME_STATE.STOP_IMPERFECT);
				if (debug) {
					System.out.println(this.app.getSimpleName() + " is imperfectly stopped.");
				}

			} else {
				this.app.setRuntimeState(Program.RUNTIME_STATE.STOPPED);
				if (debug) {
					System.out.println(this.app.getSimpleName() + " is full stopped.");
				}
			}
		}
	}

	protected synchronized void disposeApp() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".disposeApp()");
		}

		if (this.app == null) {
			if (debug) {
				System.out.println("App is alredy disposed.");
			}
			return;
		}

		// No more DependencySet change event.
		if (this.app.getDependencySet() != null) {
			this.app.getDependencySet().reset();
		}

		// No more Dependency change event.
		for (ProgramBundle appModule : this.app.getProgramBundles()) {
			if (appModule.getDependency() != null) {
				appModule.getDependency().reset();
			}
		}

		// No more AppModules
		this.app.getProgramBundles().clear();

		// Reset to the default stopped state
		this.app.setRuntimeState(Program.RUNTIME_STATE.STOPPED);

		// No more App
		this.app = null;
	}

	/**
	 * Start app bundle.
	 * 
	 * @throws ProgramException
	 */
	public synchronized void startAppModule(ProgramBundle appModule) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getName() + ".startAppModule()");
		}

		if (appModule.isReady()) {
			Bundle bundle = appModule.getBundle(Bundle.class);
			if (bundle == null) {
				throw new RuntimeException(appModule.getSimpleName() + " doesn't have associated OSGi bundle.");
			}

			try {
				bundle.start(Bundle.START_TRANSIENT);
				if (debug) {
					System.out.println(appModule.getSimpleName() + " is started.");
				}

			} catch (BundleException e) {
				e.printStackTrace();
				throw new ProgramException(e.getMessage(), e);
			}

		} else {
			if (debug) {
				System.out.println(appModule.getSimpleName() + " is not ready.");
			}
		}
	}

	/**
	 * Stop app bundle.
	 * 
	 * https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint
	 * 
	 * Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
	 */
	public synchronized void stopAppModule(ProgramBundle appModule) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getName() + ".stopAppModule()");
		}

		if (appModule.isReady()) {
			Bundle bundle = appModule.getBundle(Bundle.class);
			if (bundle == null) {
				throw new RuntimeException(appModule.getSimpleName() + " doesn't have associated OSGi bundle.");
			}

			// Stop the bundle only when it is either starting or is started.
			if (BundleUtil.isBundleStarting(bundle) || BundleUtil.isBundleActive(bundle)) {
				try {
					// Bundle will not auto start next time when stopped this way.
					// Modifies the auto start flag of the bundle, too, so the Bundle will not be auto-started after a framework restart.
					bundle.stop();
					if (debug) {
						System.out.println(appModule.getSimpleName() + " is stopped.");
					}

				} catch (BundleException e) {
					e.printStackTrace();
					throw new ProgramException(e.getMessage(), e);
				}
			}

		} else {
			if (debug) {
				System.out.println(appModule.getSimpleName() + " is not ready.");
			}
		}
	}

	@Override
	public List<Problem> getProblems() {
		return this.problems;
	}

}

/// **
// * https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint
// *
// * Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
// *
// * @param bundle
// */
// protected void stopBundle(Bundle bundle) {
// if ((bundle.getState() & (Bundle.STARTING | Bundle.ACTIVE)) == 0) {
// // not starting, not active, no need to stop
// return;
// }
// try {
// // Modifies the autostart flag of the bundle, too, so the Bundle might not be started after a framework restart.
// bundle.stop();
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

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
