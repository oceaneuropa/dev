package org.orbit.os.server.apps;

import java.util.List;

public class InstallerProvider {

	private static InstallerProvider INSTANCE;

	public static InstallerProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InstallerProvider();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param context
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	public Installer getInstaller(Object context, String appId, String appVersion) {
		// 1. Get extension/specialized installers for an app first.
		List<Installer> extensionInstallers = InstallerRegistry.getInstance().getExtensionInstallers();
		if (extensionInstallers != null) {
			for (Installer installer : extensionInstallers) {
				if (installer.isSupported(context, appId, appVersion)) {
					return installer;
				}
			}
		}

		// 2. Get system installer for the app later.
		List<Installer> systemInstallers = InstallerRegistry.getInstance().getSystemInstallers();
		if (systemInstallers != null) {
			for (Installer installer : systemInstallers) {
				if (installer.isSupported(context, appId, appVersion)) {
					return installer;
				}
			}
		}
		return null;
	}

}
