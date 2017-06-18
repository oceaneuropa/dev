package org.orbit.os.server.app;

import java.util.ArrayList;
import java.util.List;

public class AppInstallerRegistry {

	private static AppInstallerRegistry INSTANCE;

	public static AppInstallerRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AppInstallerRegistry();
		}
		return INSTANCE;
	}

	protected List<AppInstaller> systemInstallers = new ArrayList<AppInstaller>();
	protected List<AppInstaller> extensionInstallers = new ArrayList<AppInstaller>();

	/**
	 * 
	 * @return
	 */
	public List<AppInstaller> getSystemInstallers() {
		return this.systemInstallers;
	}

	/**
	 * 
	 * @return
	 */
	public List<AppInstaller> getExtensionInstallers() {
		return this.extensionInstallers;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean registerSystemInstaller(AppInstaller installer) {
		if (installer != null && !this.systemInstallers.contains(installer)) {
			return this.systemInstallers.add(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean unregisterSystemInstaller(AppInstaller installer) {
		if (installer != null && this.systemInstallers.contains(installer)) {
			return this.systemInstallers.remove(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean registerExtensionInstaller(AppInstaller installer) {
		if (installer != null && !this.extensionInstallers.contains(installer)) {
			return this.extensionInstallers.add(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean unregisterExtensionInstaller(AppInstaller installer) {
		if (installer != null && this.extensionInstallers.contains(installer)) {
			return this.extensionInstallers.remove(installer);
		}
		return false;
	}

}
