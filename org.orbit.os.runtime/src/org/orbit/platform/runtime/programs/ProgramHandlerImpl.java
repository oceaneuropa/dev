package org.orbit.platform.runtime.programs;

import java.util.ArrayList;
import java.util.List;

import org.orbit.os.api.apps.BundleManifest;
import org.orbit.os.api.apps.ProgramManifest;
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

public class ProgramHandlerImpl implements ProgramHandler {

	protected static Logger LOG = LoggerFactory.getLogger(ProgramHandlerImpl.class);

	protected ProgramHandler.RUNTIME_STATE runtimeState = ProgramHandler.RUNTIME_STATE.DEACTIVATED;
	protected BundleContext bundleContext;
	protected ProgramManifest programManifest;
	protected Program program;
	protected BundleTracker<ProgramBundle> programBundleTracker;
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param bundleContext
	 * @param programManifest
	 */
	public ProgramHandlerImpl(BundleContext bundleContext, ProgramManifest programManifest) {
		this.bundleContext = bundleContext;
		this.programManifest = programManifest;
	}

	@Override
	public ProgramManifest getManifest() {
		return this.programManifest;
	}

	@Override
	public Program getProgram() {
		return this.program;
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
			throw new RuntimeException("ProgramHandler is not activated.");
		}
		if (this.program == null) {
			throw new RuntimeException("Program is alredy disposed.");
		}
	}

	@Override
	public synchronized void activate() throws ProgramException {
		LOG.info("activate()");

		if (this.runtimeState.isActivated()) {
			LOG.info("ProgramHandler is readly activated.");
			return;
		}
		this.problems.clear();

		// 1. Create Program.
		createProgram(this.bundleContext, this.programManifest);

		// 2. Start tracking OSGi bundles of the Program.
		int bundleState = Bundle.INSTALLED | Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE;
		this.programBundleTracker = new BundleTracker<ProgramBundle>(bundleContext, bundleState, new BundleTrackerCustomizer<ProgramBundle>() {
			@Override
			public ProgramBundle addingBundle(Bundle bundle, BundleEvent event) {
				try {
					if (isProgramBundle(bundle, event)) {
						LOG.info("programBundleTracker.addingBundle() " + bundle);

						ProgramBundle programBundle = handleAddingProgramBundle(bundle, event);
						if (programBundle != null) {
							return programBundle;
						}
					}

				} catch (ProgramException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
				return null;
			}

			@Override
			public void modifiedBundle(Bundle bundle, BundleEvent event, ProgramBundle programBundle) {
				try {
					LOG.info("programBundleTracker.modifiedBundle() " + programBundle);

					handleModifiedProgramBundle(bundle, event, programBundle);

				} catch (ProgramException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
			}

			@Override
			public void removedBundle(Bundle bundle, BundleEvent event, ProgramBundle programBundle) {
				try {
					LOG.info("programBundleTracker.removedBundle() " + programBundle);

					handleRemovedProgramBundle(bundle, event, programBundle);

				} catch (ProgramException e) {
					e.printStackTrace();
					getProblems().add(new Problem(e));
				}
			}
		});
		this.programBundleTracker.open();

		setRuntimeState(ProgramHandler.RUNTIME_STATE.ACTIVATED);
	}

	@Override
	public synchronized void deactivate() throws ProgramException {
		LOG.info("deactivate()");

		if (this.runtimeState.isDeactivated()) {
			LOG.info("ProgramHandler is readly deactivated.");
			return;
		}

		// 1. Stop program bundles
		stop();

		// 2. Dispose Program.
		disposeProgram();

		// 3. Stop tracking Bundles of the Program.
		// Closing bundle tracker will trigger BundleTracker<ProgramBundle>.removedBundle(Bundle bundle, BundleEvent event, ProgramBundle programBundle) method.
		if (this.programBundleTracker != null) {
			this.programBundleTracker.close();
			this.programBundleTracker = null;
		}

		setRuntimeState(ProgramHandler.RUNTIME_STATE.DEACTIVATED);
	}

	/**
	 * Check whether one of the ProgramBundle of this Program matches the added Bundle.
	 * 
	 * @param bundle
	 * @param event
	 * @return
	 */
	protected boolean isProgramBundle(Bundle bundle, BundleEvent event) {
		String bundleName = bundle.getSymbolicName();
		String bundleVersion = bundle.getVersion().toString();
		for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
			String currBundleName = currProgramBundle.getBundleName();
			String currBundleVersion = currProgramBundle.getBundleVersion();

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
	 * @throws ProgramException
	 */
	protected ProgramBundle handleAddingProgramBundle(Bundle bundle, BundleEvent event) throws ProgramException {
		LOG.info("handleAddingProgramBundle()");

		ProgramBundle programBundle = null;

		// 1. Check whether one of the ProgramBundle of this program matches the added Bundle.
		String bundleName = bundle.getSymbolicName();
		String bundleVersion = bundle.getVersion().toString();
		for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
			String currBundleName = currProgramBundle.getBundleName();
			String currBundleVersion = currProgramBundle.getBundleVersion();

			if (bundleName.equals(currBundleName) && bundleVersion.equals(currBundleVersion)) {
				programBundle = currProgramBundle;
				break;
			}
		}

		// 2. If a ProgramBundle is found, set the OSGi Bundle to it.
		// The ProgramBundle's Dependency will be resolved. When all Dependencies are resolved, the DependencySet will be resolved.
		if (programBundle != null) {
			LOG.info("handleAddingProgramBundle() " + BundleHelper.INSTANCE.getSimpleName(event));

			programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);
			programBundle.setBundle(bundle);
		}

		return programBundle;
	}

	/**
	 * 
	 * @param bundle
	 * @param event
	 * @param programBundle
	 * @throws ProgramException
	 */
	protected void handleModifiedProgramBundle(Bundle bundle, BundleEvent event, ProgramBundle programBundle) throws ProgramException {
		LOG.info("handleModifiedProgramBundle() " + BundleHelper.INSTANCE.getSimpleName(event));

		String bundleName = BundleHelper.INSTANCE.getSimpleName(bundle);

		if (BundleUtil.isBundleInstalledEvent(event)) {
			LOG.info(bundleName + " is installed.");

		} else if (BundleUtil.isBundleResolvedEvent(event)) {
			LOG.info(bundleName + " is resolved.");

		} else if (BundleUtil.isBundleStartingEvent(event)) {
			LOG.info(bundleName + " is starting.");

		} else if (BundleUtil.isBundleStartedEvent(event)) {
			LOG.info(bundleName + " is started.");

			// Update ProgramBundle state
			programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STARTED);

			// Update Program state
			boolean doMarkStartedIfAllStarted = true;
			if (this.program.getRuntimeState().isDamaged()) {
				// program state remain damaged

			} else if (this.program.getRuntimeState().isStarted()) {
				// program state remain started

			} else if (this.program.getRuntimeState().isStartImperfect()) {
				// mark program as started if all program bundles are started
				doMarkStartedIfAllStarted = true;

			} else if (this.program.getRuntimeState().isStartFailed()) {
				// mark program as started if all program bundles are started
				doMarkStartedIfAllStarted = true;

			} else if (this.program.getRuntimeState().isStopped()) {
				// mark the program as stop imperfect
				this.program.setRuntimeState(Program.RUNTIME_STATE.STOP_IMPERFECT);

			} else if (this.program.getRuntimeState().isStopImperfect()) {
				// program state remain stop imperfect

			} else if (this.program.getRuntimeState().isStopFailed()) {
				// program state remain stop failed
			}

			if (doMarkStartedIfAllStarted) {
				boolean allProgramBundlesStarted = false;
				for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
					if (currProgramBundle.getRuntimeState().isStarted()) {
						allProgramBundlesStarted = true;
					} else {
						allProgramBundlesStarted = false;
						break;
					}
				}
				if (allProgramBundlesStarted) {
					this.program.setRuntimeState(Program.RUNTIME_STATE.STARTED);
				}
			}

		} else if (BundleUtil.isBundleStoppingEvent(event)) {
			LOG.info(bundleName + " is stopping.");

		} else if (BundleUtil.isBundleStoppedEvent(event)) {
			LOG.info(bundleName + " is stopped.");

			// Update ProgramBundle state
			programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);

			// Update Program state
			boolean doMarkStoppedIfAllStopped = true;
			if (this.program.getRuntimeState().isDamaged()) {
				// program state remain damaged

			} else if (this.program.getRuntimeState().isStarted()) {
				// mark the program as start imperfect
				this.program.setRuntimeState(Program.RUNTIME_STATE.START_IMPERFECT);

			} else if (this.program.getRuntimeState().isStartImperfect()) {
				// mark program as stopped if all program bundles are stopped
				doMarkStoppedIfAllStopped = true;

			} else if (this.program.getRuntimeState().isStartFailed()) {
				// program state remain start failed

			} else if (this.program.getRuntimeState().isStopped()) {
				// program state remain stopped

			} else if (this.program.getRuntimeState().isStopImperfect()) {
				// mark program as stopped if all program bundles are stopped
				doMarkStoppedIfAllStopped = true;

			} else if (this.program.getRuntimeState().isStopFailed()) {
				// mark program as stopped if all program bundles are stopped
				doMarkStoppedIfAllStopped = true;
			}

			if (doMarkStoppedIfAllStopped) {
				boolean allProgramBundlesStopped = false;
				for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
					if (currProgramBundle.getRuntimeState().isStopped()) {
						allProgramBundlesStopped = true;
					} else {
						allProgramBundlesStopped = false;
						break;
					}
				}
				if (allProgramBundlesStopped) {
					this.program.setRuntimeState(Program.RUNTIME_STATE.STOPPED);
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
	 * @param programBundle
	 * @throws ProgramException
	 */
	protected void handleRemovedProgramBundle(Bundle bundle, BundleEvent event, ProgramBundle programBundle) throws ProgramException {
		LOG.info("handleRemovedProgramBundle() " + BundleHelper.INSTANCE.getSimpleName(event));
		LOG.info("handleRemovedProgramBundle() Remove OSGi bundle from " + programBundle.getSimpleName());

		programBundle.setBundle(null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param programManifest
	 */
	protected void createProgram(BundleContext bundleContext, ProgramManifest programManifest) {
		LOG.info("createProgram()");

		String appId = programManifest.getId();
		String appVersion = programManifest.getVersion();
		this.program = new Program(programManifest);

		// (1) Create DependencySet for the program.
		DependencySet dependencySet = new DependencySet();
		this.program.setDependencySet(dependencySet);
		dependencySet.addListener(new DependencySetListener() {
			@Override
			public void onDependencySetChange(DependencySetEvent event) {
				DependencySet.STATE newState = event.getNewState();
				if (newState.isAllResolved()) {
					// All dependencies are resolved.
					// - start all program bundles
					try {
						if (runtimeState.isActivated()) {
							start();
						}
					} catch (ProgramException e) {
						e.printStackTrace();
					}

				} else if (newState.isNotAllResolved()) {
					// One or more dependencies are unresolved.
					// - stop all program bundles
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

		// (2) Create ProgramBundle for the program itself.
		Dependency applicationdependency = new Dependency(appId + "_" + appVersion, Bundle.class);
		this.program.getDependencySet().addDependency(applicationdependency);
		ProgramBundle applicationBundle = new ProgramBundle(appId, appVersion);
		applicationBundle.setDependency(applicationdependency);
		applicationBundle.setIsApplication(true);
		this.program.addProgramBundle(applicationBundle);

		// (3) Create ProgramBundles and create one Dependency to each ProgramBundle.
		BundleManifest[] bundleManifests = programManifest.getBundles();
		for (BundleManifest currBundleManifest : bundleManifests) {
			String bundleName = currBundleManifest.getSymbolicName();
			String bundleVersion = currBundleManifest.getVersion();

			Dependency currDependency = new Dependency(bundleName + "_" + bundleVersion, Bundle.class);
			this.program.getDependencySet().addDependency(currDependency);

			ProgramBundle programBundle = new ProgramBundle(bundleName, bundleVersion);
			programBundle.setIsModule(true);
			programBundle.setDependency(currDependency);

			this.program.addProgramBundle(programBundle);
		}

		// (4) Find OSGi bundle for each AppBundle
		Bundle[] osgiBundles = bundleContext.getBundles();
		for (ProgramBundle programBundle : this.program.getProgramBundles()) {
			String currBundleName = programBundle.getBundleName();
			String currBundleVersion = programBundle.getBundleVersion();

			Bundle osgiBundle = BundleHelper.INSTANCE.findBundle(osgiBundles, currBundleName, currBundleVersion);
			programBundle.setBundle(osgiBundle);
		}
	}

	@Override
	public synchronized void start() throws ProgramException {
		LOG.info("start()");

		checkActivated();

		if (this.program.getRuntimeState().isStarted()) {
			LOG.info("start() " + getManifest().getSimpleName() + " is already fully started.");
			return;
		}

		// 1. Start OSGi bundle of the ProgramBundles.
		boolean hasDamagedProgramBundle = false;
		boolean hasFailedToStartProgramBundle = false;
		for (ProgramBundle programBundle : this.program.getProgramBundles()) {
			try {
				if (programBundle.isReady()) {
					startOSGiBundle(programBundle);
					programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STARTED);

				} else {
					programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedProgramBundle = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				programBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.START_FAILED);
				hasFailedToStartProgramBundle = true;
			}
		}

		// 2. Update Program runtime state
		if (hasDamagedProgramBundle) {
			this.program.setRuntimeState(Program.RUNTIME_STATE.DAMAGED);
			LOG.info("start() " + this.program.getSimpleName() + " has damaged app bundle.");

		} else if (hasFailedToStartProgramBundle) {
			this.program.setRuntimeState(Program.RUNTIME_STATE.START_FAILED);
			LOG.info("start() " + this.program.getSimpleName() + " has app bundle(s) failed to start.");

		} else {
			boolean hasUnstartedAppBundle = false;
			for (ProgramBundle appBundle : this.program.getProgramBundles()) {
				if (!appBundle.getRuntimeState().isStarted()) {
					hasUnstartedAppBundle = true;
					break;
				}
			}

			if (hasUnstartedAppBundle) {
				this.program.setRuntimeState(Program.RUNTIME_STATE.START_IMPERFECT);
				LOG.info("start() " + this.program.getSimpleName() + " is imperfectly started.");

			} else {
				this.program.setRuntimeState(Program.RUNTIME_STATE.STARTED);
				LOG.info("start() " + this.program.getSimpleName() + " is fully started.");
			}
		}
	}

	@Override
	public synchronized void stop() throws ProgramException {
		LOG.info("stop()");
		checkActivated();

		if (this.program.getRuntimeState().isStopped()) {
			LOG.info("stop() " + getManifest().getSimpleName() + " is already fully stopped.");
			return;
		}

		// 1. Stop the OSGi bundle of ProgramBundles.
		boolean hasDamagedProgramBundle = false;
		boolean hasFailedToStopProgramBundle = false;
		for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
			try {
				if (currProgramBundle.isReady()) {
					stopOSGiBundle(currProgramBundle);
					currProgramBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOPPED);

				} else {
					currProgramBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.DAMAGED);
					hasDamagedProgramBundle = true;
				}

			} catch (ProgramException e) {
				e.printStackTrace();
				this.problems.add(new Problem(e));
				currProgramBundle.setRuntimeState(ProgramBundle.RUNTIME_STATE.STOP_FAILED);
				hasFailedToStopProgramBundle = true;
			}
		}

		// 2. Update Program runtime state
		if (hasDamagedProgramBundle) {
			this.program.setRuntimeState(Program.RUNTIME_STATE.DAMAGED);
			LOG.info("stop() " + this.program.getSimpleName() + " has damaged app bundle.");

		} else if (hasFailedToStopProgramBundle) {
			this.program.setRuntimeState(Program.RUNTIME_STATE.STOP_FAILED);
			LOG.info("stop() " + this.program.getSimpleName() + " has app bundle(s) failed to stop.");

		} else {
			boolean hasUnstoppedProgramBundle = false;
			for (ProgramBundle programBundle : this.program.getProgramBundles()) {
				if (!programBundle.getRuntimeState().isStopped()) {
					hasUnstoppedProgramBundle = true;
					break;
				}
			}

			if (hasUnstoppedProgramBundle) {
				this.program.setRuntimeState(Program.RUNTIME_STATE.STOP_IMPERFECT);
				LOG.info("stop() " + this.program.getSimpleName() + " is imperfectly stopped.");

			} else {
				this.program.setRuntimeState(Program.RUNTIME_STATE.STOPPED);
				LOG.info("stop() () " + this.program.getSimpleName() + " is full stopped.");
			}
		}
	}

	protected synchronized void disposeProgram() {
		LOG.info("disposeProgram()");

		if (this.program == null) {
			LOG.info("Program is alredy disposed.");
			return;
		}

		// No more DependencySet change event.
		if (this.program.getDependencySet() != null) {
			this.program.getDependencySet().reset();
		}

		// No more Dependency change event.
		for (ProgramBundle currProgramBundle : this.program.getProgramBundles()) {
			if (currProgramBundle.getDependency() != null) {
				currProgramBundle.getDependency().reset();
			}
		}

		// No more program bundles
		this.program.getProgramBundles().clear();

		// Reset to the default stopped state
		this.program.setRuntimeState(Program.RUNTIME_STATE.STOPPED);

		// No more Program
		this.program = null;
	}

	/**
	 * Start OSGi bundle of ProgramBundle.
	 * 
	 * @param programBundle
	 * @throws ProgramException
	 */
	protected synchronized void startOSGiBundle(ProgramBundle programBundle) throws ProgramException {
		LOG.info("startOSGiBundle() " + programBundle.getSimpleName());

		if (programBundle.isReady()) {
			try {
				Bundle bundle = programBundle.getBundle(Bundle.class);
				bundle.start(Bundle.START_TRANSIENT);

			} catch (BundleException e) {
				throw new ProgramException(e.getMessage(), e);
			}

		} else {
			LOG.info(programBundle.getSimpleName() + " is not ready.");
		}
	}

	/**
	 * Stop OSGi bundle of ProgramBundle.
	 * 
	 * @param programBundle
	 * @throws ProgramException
	 */
	protected synchronized void stopOSGiBundle(ProgramBundle programBundle) throws ProgramException {
		LOG.info("stopOSGiBundle() " + programBundle.getSimpleName());

		if (programBundle.isReady()) {
			// Stop the bundle only when it is either started or being started.
			Bundle bundle = programBundle.getBundle(Bundle.class);
			if (BundleUtil.isBundleActive(bundle) | BundleUtil.isBundleStarting(bundle)) {
				try {
					// Bundle.STOP_TRANSIENT: Does not modify the autostart flag of the bundle
					// https://stackoverflow.com/questions/36940647/what-are-the-available-options-for-org-osgi-framework-bundle-stopint

					// Bundle will not auto start next time when stopped this way.
					// Modifies the auto start flag of the bundle, too, so the Bundle will not be auto-started after a framework restart.
					bundle.stop();

				} catch (BundleException e) {
					throw new ProgramException(e.getMessage(), e);
				}
			}

		} else {
			LOG.info(programBundle.getSimpleName() + " is not ready.");
		}
	}

	@Override
	public List<Problem> getProblems() {
		return this.problems;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [programManifest=" + this.programManifest.getSimpleName() + "]";
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
