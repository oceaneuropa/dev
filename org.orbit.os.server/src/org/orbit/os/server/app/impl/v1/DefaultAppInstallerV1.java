package org.orbit.os.server.app.impl.v1;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.orbit.app.AppManifest;
import org.orbit.app.BundleManifest;
import org.orbit.app.util.AppManifestUtil;
import org.orbit.app.util.AppUtil;
import org.orbit.component.api.Orbit;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.os.server.app.AppException;
import org.orbit.os.server.app.AppInstaller;
import org.orbit.os.server.app.AppInstallerRegistry;
import org.orbit.os.server.util.SetupUtil;
import org.origin.common.io.IOUtil;
import org.origin.common.osgi.BundleHelper;
import org.origin.common.osgi.BundleUtil;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class DefaultAppInstallerV1 implements AppInstaller {

	public static final String ID = "DefaultAppInstallerV1";

	public static DefaultAppInstallerV1 INSTANCE = null;

	public static DefaultAppInstallerV1 getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultAppInstallerV1();
		}
		return INSTANCE;
	}

	protected boolean debug = false;

	public void activate() {
		AppInstallerRegistry.getInstance().registerSystemInstaller(this);
	}

	public void deactivate() {
		AppInstallerRegistry.getInstance().unregisterSystemInstaller(this);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean isSupported(Object context, String appId, String appVersion) {
		if (context instanceof BundleContext) {
			return true;
		}
		return false;
	}

	protected BundleContext checkContext(Object context) throws AppException {
		if (!(context instanceof BundleContext)) {
			throw new AppException("context '" + context + "' is not supported by '" + ID + "'");
		}
		return (BundleContext) context;
	}

	@Override
	public AppManifest install(Object context, String appId, String appVersion) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".install() context = " + context + ", appId = " + appId + ", appVersion = " + appVersion);
		}
		BundleContext bundleContext = checkContext(context);

		AppStore appStore = Orbit.getInstance().getAppStore();
		if (appStore == null) {
			throw new AppException("App store is not available.");
		}

		String appFolderName = AppUtil.deriveAppFolderName(appId, appVersion);

		// 1. Get app record info from app store and get file name from the app info.
		org.orbit.component.api.tier2.appstore.AppManifest appInfo = null;
		try {
			appInfo = appStore.getApp(appId, appVersion);
		} catch (ClientException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e);
		}
		if (appInfo == null) {
			throw new AppException("App " + appId + " - " + appVersion + " is not found on app store.");
		}

		// 2. Create {TA_HOME}/downloads/apps/<appId>_<appVersion>/
		Path taHome = SetupUtil.getTAHome(bundleContext);
		Path downloadsPath = SetupUtil.getTADownloadsPath(taHome, true);
		Path appFolderDownloadPath = downloadsPath.resolve("apps").resolve(appFolderName);
		if (!Files.exists(appFolderDownloadPath)) {
			try {
				appFolderDownloadPath = Files.createDirectory(appFolderDownloadPath);
			} catch (IOException e) {
				e.printStackTrace();
				throw new AppException("App folder cannot be created in the '" + downloadsPath.toString() + "' folder.", e);
			}
		}

		// 3. Download the app archive file into {TA_HOME}/downloads/apps/<appId>_<appVersion>/
		String fileName = appInfo.getFileName();
		if (fileName == null || fileName.isEmpty()) {
			fileName = AppUtil.deriveAppFileName(appId, appVersion);
		}
		Path appArchiveDownloadPath = appFolderDownloadPath.resolve(fileName);
		try {
			Files.deleteIfExists(appArchiveDownloadPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppException("Existing app archive file '" + appArchiveDownloadPath.toString() + "' cannot be deleted.", e);
		}

		OutputStream output = null;
		try {
			output = Files.newOutputStream(appArchiveDownloadPath);
			appStore.downloadAppArchive(appId, appVersion, output);

		} catch (IOException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e);

		} catch (ClientException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage(), e);

		} finally {
			IOUtil.closeQuietly(output, true);
		}
		if (!Files.exists(appArchiveDownloadPath)) {
			throw new AppException("App archive file '" + appArchiveDownloadPath.getFileName().toString() + "' cannot be downloaded to the '" + appFolderDownloadPath.toString() + "' folder.");
		}

		// 4. Extract the downloaded app archive file to {TA_HOME}/apps/<appId>_<appVersion>/
		Path taAppsPath = SetupUtil.getTAAppsPath(taHome, true);
		Path appFolderPath = taAppsPath.resolve(appFolderName);
		if (!Files.exists(appFolderPath)) {
			try {
				appFolderPath = Files.createDirectory(appFolderPath);
			} catch (IOException e) {
				e.printStackTrace();
				throw new AppException("App folder cannot be created in the '" + taAppsPath.toString() + "' folder.", e);
			}
		}
		try {
			AppUtil.extractToAppFolder(appFolderPath.toFile(), appArchiveDownloadPath.toFile());
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppException("App archive file '" + appArchiveDownloadPath.getFileName().toString() + "' cannot be extracted to the '" + appFolderPath.toString() + "' folder.", e);
		}

		// 5. Install app bundles to OSGi framework.
		AppManifest appManifest = installAppBundles(bundleContext, appFolderPath);
		return appManifest;
	}

	@Override
	public AppManifest install(Object context, Path appArchivePath) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".install() context = " + context + ", appArchivePath = " + appArchivePath.toString());
		}
		BundleContext bundleContext = checkContext(context);

		// 1. Extract AppManifest from the app archive file
		AppManifest appManifestFromArchive = AppUtil.extractAppManifest(appArchivePath);
		if (appManifestFromArchive == null) {
			throw new AppException("App manifest cannot be found in the app archive file '" + appArchivePath.toString() + "'.");
		}
		String appId = appManifestFromArchive.getId();
		String appVersion = appManifestFromArchive.getVersion();
		String appFolderName = AppUtil.deriveAppFolderName(appId, appVersion);

		// 2. Extract the downloaded app archive file into {TA_HOME}/apps/<appId>_<appVersion>/
		Path taHome = SetupUtil.getTAHome(bundleContext);
		Path taAppsPath = SetupUtil.getTAAppsPath(taHome, true);
		Path appFolderPath = taAppsPath.resolve(appFolderName);
		if (!Files.exists(appFolderPath)) {
			try {
				appFolderPath = Files.createDirectory(appFolderPath);
			} catch (IOException e) {
				e.printStackTrace();
				throw new AppException("Cannot create '" + appFolderName + "' folder in the '" + taAppsPath.toString() + "' folder.", e);
			}
		}
		try {
			AppUtil.extractToAppFolder(appFolderPath.toFile(), appArchivePath.toFile());
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppException("App archive file '" + appArchivePath.getFileName().toString() + "' cannot be extracted to the '" + appFolderPath.toString() + "' folder.", e);
		}

		// 3. Install app bundles to OSGi framework.
		AppManifest appManifest = installAppBundles(bundleContext, appFolderPath);
		return appManifest;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param appFolderPath
	 * @return
	 * @throws AppException
	 */
	protected AppManifest installAppBundles(BundleContext bundleContext, Path appFolderPath) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".installAppBundles() appFolderPath = " + appFolderPath.toString());
		}

		AppManifest appManifest = AppManifestUtil.getManifestFromAppFolder(appFolderPath.toFile());
		if (appManifest == null) {
			throw new AppException("App manifest cannot be found in the app folder '" + appFolderPath.toString() + "'.");
		}

		String[] classPaths = appManifest.getClassPaths();
		if (classPaths != null) {
			for (String classPath : classPaths) {
				if (classPath != null && classPath.startsWith("/") && classPath.length() > 1) {
					classPath = classPath.substring(1);
				}
				Path appBundlePath = appFolderPath.resolve(classPath);
				if (Files.exists(appBundlePath)) {
					boolean isBundleFile = BundleUtil.isBundleFile(appBundlePath.toFile());
					if (!isBundleFile) {
						if (debug) {
							System.out.println("'" + appBundlePath.toString() + "' is not a OSGi bundle file.");
						}
						continue;
					}

					try {
						Bundle bundle = BundleUtil.installBundle(bundleContext, appBundlePath.toFile(), 0);
						if (debug) {
							if (bundle != null) {
								System.out.println("OSGi bundle '" + bundle.toString() + "' is installed for '" + appBundlePath.toString() + "'.");
							} else {
								System.out.println("OSGi bundle is not installed for '" + appBundlePath.toString() + "'.");
							}
						}
					} catch (IOException e) {
						e.printStackTrace();

					} catch (BundleException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return appManifest;
	}

	@Override
	public boolean uninstall(Object context, AppManifest appManifest) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".uninstall() context = " + context + ", appManifest = " + appManifest.getSimpleName());
		}
		BundleContext bundleContext = checkContext(context);

		Bundle[] osgiBundles = bundleContext.getBundles();
		BundleManifest[] appBundleManifests = appManifest.getBundles();

		for (BundleManifest appBundleManifest : appBundleManifests) {
			String appBundleName = appBundleManifest.getSymbolicName();
			String appBundleVersion = appBundleManifest.getVersion();

			// Find the corresponding OSGi Bundle
			Bundle osgiBundle = BundleHelper.INSTANCE.findBundle(osgiBundles, appBundleName, appBundleVersion);
			if (osgiBundle != null) {
				try {
					// Uninstall the OSGi Bundle
					BundleUtil.uninstallBundle(osgiBundle);

				} catch (BundleException e) {
					e.printStackTrace();
					throw new AppException(e.getMessage(), e);
				}
			}
		}

		return true;
	}

	public static void main(String[] args) {
		java.io.File taHomeDir = new java.io.File("/Users/oceaneuropa/origin/ta1");
		java.io.File appArchiveDownloadFile = new java.io.File("/Users/oceaneuropa/origin/ta1/apps/abc.editor_1.0.0/abc.editor_1.0.0.app");

		Path taHomePath = taHomeDir.toPath();
		Path appArchiveDownloadPath = appArchiveDownloadFile.toPath();

		Path relativePath1 = appArchiveDownloadPath.relativize(taHomePath);
		Path relativePath2 = taHomePath.relativize(appArchiveDownloadPath);

		System.out.println(taHomePath.toString());
		System.out.println(appArchiveDownloadPath.toString());
		System.out.println(relativePath1.toString());
		System.out.println(relativePath2.toString());
	}

}
