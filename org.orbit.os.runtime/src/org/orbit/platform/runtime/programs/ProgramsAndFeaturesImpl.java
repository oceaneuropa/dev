/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.programs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.os.api.apps.ProgramManifest;
import org.orbit.os.api.util.AppManifestUtil;
import org.orbit.os.api.util.ProgramUtil;
import org.origin.common.runtime.Problem;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Functions for apps:
 * (1) Get installed apps manifests.
 * (2) Install app from a app zip file.
 * (3) Uninstall an app by id and version.
 * 
 * FrameworkFactory example code:
 * http://www.programcreek.com/java-api-examples/index.php?source_dir=arkadiko-master/src/osgi/com/liferay/arkadiko/osgi/OSGiFrameworkFactory.java
 * http://www.javased.com/index.php?api=org.osgi.framework.launch.FrameworkFactory
 * http://www.javased.com/index.php?source_dir=jbosgi-framework/itest/src/test/java/org/jboss/test/osgi/framework/launch/PersistentBundlesTestCase.java
 * 
 */
public class ProgramsAndFeaturesImpl implements ProgramsAndFeatures {

	protected static Logger LOG = LoggerFactory.getLogger(ProgramsAndFeaturesImpl.class);

	public static final String APPLICATION_HEADER = "Application";

	protected BundleContext bundleContext;
	protected List<ProgramManifest> installedPrograms = new ArrayList<ProgramManifest>();
	protected Map<ProgramManifest, ProgramHandler> programHandlersMap = new HashMap<ProgramManifest, ProgramHandler>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param bundleContext
	 */
	public ProgramsAndFeaturesImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	protected synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	protected void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	@Override
	public synchronized void start() throws ProgramException {
		LOG.info("start()");

		if (isStarted()) {
			LOG.info("Already started.");
			return;
		}
		this.isStarted.set(true);

		InstallerImpl.getInstance().activate();

		loadInstalledPrograms();
		initProgramHandlers();
		activateProgramHandlers();
		autoStartProgramHandlers();
	}

	protected synchronized void loadInstalledPrograms() {
		LOG.info("loadInstalledApps()");

		this.installedPrograms.clear();

		Bundle[] osgiBundles = this.bundleContext.getBundles();
		for (Bundle osgiBundle : osgiBundles) {
			ProgramManifest appManifest = AppManifestUtil.getAppManifest(osgiBundle);
			if (appManifest != null) {
				this.installedPrograms.add(appManifest);
			}
		}
	}

	protected void initProgramHandlers() {
		LOG.info("initProgramHandlers()");

		this.programHandlersMap.clear();
		for (ProgramManifest installedProgram : this.installedPrograms) {
			ProgramHandler programHandler = new ProgramHandlerImpl(this.bundleContext, installedProgram);
			this.programHandlersMap.put(installedProgram, programHandler);
		}
	}

	protected void activateProgramHandlers() throws ProgramException {
		LOG.info("activateProgramHandlers()");

		for (Iterator<ProgramManifest> programManifestItor = this.programHandlersMap.keySet().iterator(); programManifestItor.hasNext();) {
			ProgramManifest programManifest = programManifestItor.next();
			ProgramHandler programHandler = this.programHandlersMap.get(programManifest);
			activateProgramHandler(programHandler);
		}
	}

	protected void activateProgramHandler(ProgramHandler programHandler) throws ProgramException {
		LOG.info("activateProgramHandler() programHandler = " + programHandler);

		if (programHandler != null) {
			programHandler.activate();
		}
	}

	/**
	 * 
	 * @throws ProgramException
	 */
	protected void autoStartProgramHandlers() throws ProgramException {
		for (Iterator<ProgramManifest> programManifestItor = this.programHandlersMap.keySet().iterator(); programManifestItor.hasNext();) {
			ProgramManifest currProgramManifest = programManifestItor.next();
			ProgramHandler currProgramHandler = this.programHandlersMap.get(currProgramManifest);
			currProgramHandler.start();
		}
	}

	@Override
	public synchronized void stop() throws ProgramException {
		LOG.info("stop()");

		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		deactivateProgramHandlers();

		this.programHandlersMap.clear();
		this.installedPrograms.clear();

		InstallerImpl.getInstance().deactivate();
	}

	protected void deactivateProgramHandlers() throws ProgramException {
		LOG.info("deactivateProgramHandlers()");

		for (Iterator<ProgramManifest> ProgramManifestItor = this.programHandlersMap.keySet().iterator(); ProgramManifestItor.hasNext();) {
			ProgramManifest currProgramManifest = ProgramManifestItor.next();
			ProgramHandler currProgramHandler = this.programHandlersMap.get(currProgramManifest);
			deactivateProgramHandler(currProgramHandler);
		}
	}

	protected void deactivateProgramHandler(ProgramHandler programHandler) throws ProgramException {
		LOG.info("deactivateProgramHandler() programHandler = " + programHandler);

		if (programHandler != null) {
			// What is going to happen when ProgramHandler is closed.
			// (1) Stop tracking Bundles of the App.
			// (2) Dispose runtime App.
			programHandler.deactivate();
		}
	}

	@Override
	public synchronized ProgramManifest[] getInstalledApps() {
		return this.installedPrograms.toArray(new ProgramManifest[this.installedPrograms.size()]);
	}

	@Override
	public synchronized boolean isInstalled(String appId, String appVersion) {
		LOG.info("isInstalled() appId = " + appId + ", appVersion = " + appVersion);

		for (Iterator<ProgramManifest> programItor = this.installedPrograms.iterator(); programItor.hasNext();) {
			ProgramManifest currProgramManifest = programItor.next();
			String currAppId = currProgramManifest.getId();
			String currAppVersion = currProgramManifest.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized ProgramManifest getInstalledPrograms(String appId, String appVersion) {
		ProgramManifest installedProgramManifest = null;
		for (Iterator<ProgramManifest> appItor = this.installedPrograms.iterator(); appItor.hasNext();) {
			ProgramManifest currInstalledApp = appItor.next();
			String currAppId = currInstalledApp.getId();
			String currAppVersion = currInstalledApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				installedProgramManifest = currInstalledApp;
				break;
			}
		}
		return installedProgramManifest;
	}

	/**
	 * 
	 * @param programManifest
	 * @return
	 */
	protected synchronized ProgramHandler registerProgram(ProgramManifest programManifest) throws ProgramException {
		LOG.info("registerProgram() programManifest = " + programManifest);

		// Stopping processing apps once the app manager is stopped.
		checkStarted();

		if (isInstalled(programManifest.getId(), programManifest.getVersion())) {
			return getProgramHandler(programManifest.getId(), programManifest.getVersion());
		}

		// 1. Create ProgramHandler for the AppManifest and open the AppHandler.
		ProgramHandler programHandler = new ProgramHandlerImpl(this.bundleContext, programManifest);
		activateProgramHandler(programHandler);

		// 2. Add ProgramManifest to the list of installed apps.
		this.programHandlersMap.put(programManifest, programHandler);
		this.installedPrograms.add(programManifest);

		return programHandler;
	}

	/**
	 * 
	 * @param programManifest
	 * @return
	 * @throws ProgramException
	 */
	protected synchronized boolean unregisterProgram(ProgramManifest programManifest) throws ProgramException {
		LOG.info("unregisterProgram() programManifest = " + programManifest.getSimpleName());

		// stopping processing apps once the app manager is stopped.
		checkStarted();

		boolean removed = this.installedPrograms.remove(programManifest);
		ProgramHandler programHandler = this.programHandlersMap.remove(programManifest);
		if (removed && programHandler != null) {
			deactivateProgramHandler(programHandler);
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param appId
	 * @param appVersion
	 * @param expectedToBeInstalled
	 * @throws ProgramException
	 */
	protected void checkInstalled(String appId, String appVersion, boolean expectedToBeInstalled) throws ProgramException {
		boolean isAppInstalled = isInstalled(appId, appVersion);
		if (isAppInstalled) {
			// app is installed
			if (!expectedToBeInstalled) {
				// app is expected to be not installed.
				throw new ProgramException("App " + appId + "(" + appVersion + ") is already installed.");
			}

		} else {
			// app is not installed
			if (expectedToBeInstalled) {
				// app is expected to be installed.
				throw new ProgramException("App " + appId + "(" + appVersion + ") is not installed.");
			}
		}
	}

	@Override
	public ProgramManifest install(Path programArchivePath) throws ProgramException {
		LOG.info("install() programArchivePath = " + programArchivePath.toString());

		checkStarted();

		ProgramManifest programManifestFromArchive = ProgramUtil.extractProgramManifest(programArchivePath.toFile());
		if (programManifestFromArchive == null) {
			LOG.info("Program manifest cannot be found in the app archive file '" + programArchivePath.toString() + "'.");
			return null;
		}
		String appId = programManifestFromArchive.getId();
		String appVersion = programManifestFromArchive.getVersion();

		// app is expected to be not installed
		checkInstalled(appId, appVersion, false);

		Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		ProgramManifest programManifest = installer.install(this.bundleContext, programArchivePath);
		if (programManifest == null) {
			LOG.info("ProgramManifest is not returned by installer.");
			return null;
		}

		ProgramHandler newProgramHandler = registerProgram(programManifest);
		if (newProgramHandler != null) {
			return programManifest;
		}
		return null;
	}

	@Override
	public ProgramManifest uninstall(String appId, String appVersion) throws ProgramException {
		LOG.info("uninstall() appId = " + appId + ", appVersion = " + appVersion);
		checkStarted();

		// app is expected to be installed
		checkInstalled(appId, appVersion, true);

		ProgramManifest appManifest = getInstalledPrograms(appId, appVersion);
		if (appManifest == null) {
			LOG.info("App manifest cannot be found in installed apps.");
			return null;
		}

		Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		boolean uninstalled = installer.uninstall(this.bundleContext, appManifest);
		if (uninstalled) {
			boolean unregistered = unregisterProgram(appManifest);
			if (unregistered) {
				return appManifest;
			}
		}
		return null;
	}

	@Override
	public synchronized ProgramHandler[] getProgramHandlers() {
		LOG.info("getAppHandlers()");

		List<ProgramHandler> appHandlers = new ArrayList<ProgramHandler>();
		for (Iterator<ProgramManifest> appManifestItor = this.programHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			ProgramManifest currAppManifest = appManifestItor.next();
			ProgramHandler currAppHandler = this.programHandlersMap.get(currAppManifest);
			appHandlers.add(currAppHandler);
		}
		return appHandlers.toArray(new ProgramHandler[appHandlers.size()]);
	}

	@Override
	public ProgramHandler getProgramHandler(String appId, String appVersion) {
		LOG.info("getAppHandler() appId = " + appId + ", appVersion = " + appVersion);

		ProgramHandler appHandler = null;
		for (Iterator<ProgramManifest> appItor = this.programHandlersMap.keySet().iterator(); appItor.hasNext();) {
			ProgramManifest currAppManifest = appItor.next();
			ProgramHandler currAppHandler = this.programHandlersMap.get(currAppManifest);

			String currAppId = currAppHandler.getManifest().getId();
			String currAppVersion = currAppHandler.getManifest().getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				appHandler = currAppHandler;
				break;
			}
		}
		return appHandler;
	}

	@Override
	public List<Problem> getProblems() {
		return this.problems;
	}

}

// protected BundleTracker<AppHandler> applicationBundleTracker;

// Start tracking OSGi bundles of the App.
// int bundleState = Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE;
// this.applicationBundleTracker = new BundleTracker<AppHandler>(this.bundleContext, bundleState, new BundleTrackerCustomizer<AppHandler>() {
// @Override
// public AppHandler addingBundle(Bundle bundle, BundleEvent event) {
// try {
// AppHandler appHandler = handleAddingApplication(bundle, event);
// if (appHandler != null) {
// return appHandler;
// }
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// return null;
// }
//
// @Override
// public void modifiedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) {
// try {
// System.out.println("applicationBundleTracker.modifiedBundle() " + appHandler);
// handleModifiedApplication(bundle, event, appHandler);
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// }
//
// @Override
// public void removedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) {
// try {
// System.out.println("applicationBundleTracker.removedBundle() " + appHandler);
// handleRemovedApplication(bundle, event, appHandler);
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// }
// });
// this.applicationBundleTracker.open();

// Stop tracking Bundles of applications
// Closing bundle tracker will trigger BundleTracker<AppHandler>.removedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) method.
// if (this.applicationBundleTracker != null) {
// this.applicationBundleTracker.close();
// this.applicationBundleTracker = null;
// }

// /**
// *
// * @param bundle
// * @param event
// * @return
// * @throws AppException
// */
// protected AppHandler handleAddingApplication(Bundle bundle, BundleEvent event) throws AppException {
// if (debug) {
// // System.out.print(getClass().getSimpleName() + ".handleAddingApplication() ");
// }
//
// // Load META-INF/manifest.json from OSGi bundle.
// // If AppManifest can be retrieved, it means the OSGi Bundle is for the application itself.
// AppManifest appManifestFromBundle = AppManifestUtil.getAppManifest(bundle);
// if (appManifestFromBundle != null) {
// // if (debug) {
// // System.out.print(getClass().getSimpleName() + ".handleAddingApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// // System.out.println("appManifest = " + appManifestFromBundle);
// // }
//
// // String appId = appManifestFromBundle.getId();
// // String appVersion = appManifestFromBundle.getVersion();
// // boolean isAppInstalled = isAppInstalled(appId, appVersion);
// // if (isAppInstalled) {
// // AppHandler existingAppHandler = getAppHandler(appId, appVersion);
// // existingAppHandler.getApp().setBundle(bundle);
// // return existingAppHandler;
// //
// // } else {
// // AppHandler newAppHandler = registerApp(appManifestFromBundle);
// // if (newAppHandler != null) {
// // newAppHandler.getApp().setBundle(bundle);
// // }
// // return newAppHandler;
// // }
// }
// return null;
// }
//
// /**
// *
// * @param bundle
// * @param event
// * @param appHandler
// * @throws AppException
// */
// protected void handleModifiedApplication(Bundle bundle, BundleEvent event, AppHandler appHandler) throws AppException {
// if (debug) {
// System.out.print(getClass().getSimpleName() + ".handleModifiedApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// }
//
// String bundleName = BundleHelper.INSTANCE.getSimpleName(bundle);
//
// if (BundleUtil.isBundleInstalledEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is installed.");
// }
//
// } else if (BundleUtil.isBundleResolvedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is resolved.");
// }
//
// } else if (BundleUtil.isBundleStartingEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is starting.");
// }
//
// } else if (BundleUtil.isBundleStartedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is started.");
// }
// // appHandler.startApp();
//
// } else if (BundleUtil.isBundleStoppingEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is stopping.");
// }
//
// } else if (BundleUtil.isBundleStoppedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is stopped.");
// }
// // appHandler.stopApp();
//
// } else if (BundleUtil.isBundleUnresolvedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is unresolved.");
// }
//
// } else if (BundleUtil.isBundleUninstalledEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is uninstalled.");
// }
// }
// }
//
// /**
// *
// * @param bundle
// * @param event
// * @param appHandler
// * @throws AppException
// */
// protected void handleRemovedApplication(Bundle bundle, BundleEvent event, AppHandler appHandler) throws AppException {
// if (debug) {
// System.out.print(getClass().getSimpleName() + ".handleRemovedApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// }
//
// // Question:
// // Should the application be uninstalled when the OSGi bundle for the application itself is uninstalled?
// //
// // Answer:
// // Probably no. So that OSGi bundle (which can have configurations) for the application can be re-installed/updated.
// // That also means uninstallApp() should actively unregisterApp() after all the OSGi bundles of the application have been uninstalled.
//
// // AppManifest appManifest = appHandler.getAppManifest();
// // String appId = appManifest.getId();
// // String appVersion = appManifest.getVersion();
// // unregisterApp(appId, appVersion);
// }

// @Override
// public AppManifest install(String appId, String appVersion) throws AppException {
// LOG.info("install() appId = " + appId + ", appVersion = " + appVersion);
//
// checkStarted();
//
// // app is expected to be not installed
// checkAppInstalled(appId, appVersion, false);
//
// Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
// AppManifest appManifest = installer.install(this.bundleContext, appId, appVersion);
// if (appManifest == null) {
// LOG.info("AppManifest is not returned by installer.");
// return null;
// }
//
// AppHandler newAppHandler = registerApp(appManifest);
// if (newAppHandler != null) {
// return appManifest;
// }
// return null;
// }
