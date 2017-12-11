package org.orbit.os.runtime.programs;

import java.util.ArrayList;
import java.util.List;

public class InstallerRegistry {

	private static InstallerRegistry INSTANCE;

	public static InstallerRegistry getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InstallerRegistry();
		}
		return INSTANCE;
	}

	protected List<Installer> systemInstallers = new ArrayList<Installer>();
	protected List<Installer> extensionInstallers = new ArrayList<Installer>();

	/**
	 * 
	 * @return
	 */
	public List<Installer> getSystemInstallers() {
		return this.systemInstallers;
	}

	/**
	 * 
	 * @return
	 */
	public List<Installer> getExtensionInstallers() {
		return this.extensionInstallers;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean registerSystemInstaller(Installer installer) {
		if (installer != null && !this.systemInstallers.contains(installer)) {
			return this.systemInstallers.add(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean unregisterSystemInstaller(Installer installer) {
		if (installer != null && this.systemInstallers.contains(installer)) {
			return this.systemInstallers.remove(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean registerExtensionInstaller(Installer installer) {
		if (installer != null && !this.extensionInstallers.contains(installer)) {
			return this.extensionInstallers.add(installer);
		}
		return false;
	}

	/**
	 * 
	 * @param installer
	 */
	public boolean unregisterExtensionInstaller(Installer installer) {
		if (installer != null && this.extensionInstallers.contains(installer)) {
			return this.extensionInstallers.remove(installer);
		}
		return false;
	}

}
