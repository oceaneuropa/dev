package org.origin.common.launch.util;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.extensions.ExtensionActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.launch.LaunchType;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.impl.LaunchTypeImpl;

public class LaunchExtensionHelper {

	public static class LauncherExtension {
		protected String id;
		protected String typeId;
		protected Launcher launcher;

		public LauncherExtension(String id, String typeId, Launcher launcher) {
			this.id = id;
			this.typeId = typeId;
			this.launcher = launcher;
		}

		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTypeId() {
			return this.typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

		public Launcher getLauncher() {
			return this.launcher;
		}

		public void setLauncher(Launcher launcher) {
			this.launcher = launcher;
		}
	}

	public static LaunchExtensionHelper INSTANCE = new LaunchExtensionHelper();

	protected List<LaunchType> launchTypes;
	protected List<LauncherExtension> launcherExtensions;

	protected synchronized List<LaunchType> loadLaunchTypes() {
		List<LaunchType> launchTypes = new ArrayList<LaunchType>();

		ExtensionActivator activator = ExtensionActivator.getDefault();
		if (activator == null) {
			System.err.println("ExtensionActivator is null.");
			return launchTypes;
		}

		IExtensionService service = activator.getExtensionService();
		if (service == null) {
			System.err.println("IExtensionService is null.");
			return launchTypes;
		}

		IExtension[] extensions = service.getExtensions(LaunchType.TYPE_ID);
		for (IExtension extension : extensions) {
			LaunchType launchType = extension.getInterface(LaunchType.class);
			if (launchType == null) {
				launchType = new LaunchTypeImpl(extension);
			}
			launchTypes.add(launchType);
		}
		return launchTypes;
	}

	public synchronized LaunchType[] getLaunchTypes() {
		if (this.launchTypes == null) {
			this.launchTypes = loadLaunchTypes();
		}
		return this.launchTypes.toArray(new LaunchType[this.launchTypes.size()]);
	}

	public synchronized LaunchType getLaunchType(String typeId) {
		if (this.launchTypes == null) {
			this.launchTypes = loadLaunchTypes();
		}
		for (LaunchType launchProvider : this.launchTypes) {
			String currTypeId = launchProvider.getId();
			if (typeId != null && typeId.equals(currTypeId)) {
				return launchProvider;
			}
		}
		return null;
	}

	protected synchronized List<LauncherExtension> loadLaunchers() {
		List<LauncherExtension> launcherExtensions = new ArrayList<LauncherExtension>();
		IExtension[] extensions = ExtensionActivator.getDefault().getExtensionService().getExtensions(Launcher.TYPE_ID);
		for (IExtension extension : extensions) {
			String currId = extension.getId();
			String currTypeId = (String) extension.getProperty(Launcher.PROP_TYPE_ID);
			Launcher launcher = extension.getInterface(Launcher.class);
			if (launcher != null) {
				LauncherExtension launcherExtension = new LauncherExtension(currId, currTypeId, launcher);
				launcherExtensions.add(launcherExtension);
			}
		}
		return launcherExtensions;
	}

	public synchronized LauncherExtension[] getLauncherExtensions() {
		if (this.launcherExtensions == null) {
			this.launcherExtensions = loadLaunchers();
		}
		return this.launcherExtensions.toArray(new LauncherExtension[this.launcherExtensions.size()]);
	}

	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public synchronized LauncherExtension[] getLauncherExtensions(String typeId) {
		List<LauncherExtension> resultLaunchers = new ArrayList<LauncherExtension>();
		if (this.launcherExtensions == null) {
			this.launcherExtensions = loadLaunchers();
		}
		for (LauncherExtension currLauncherExtension : this.launcherExtensions) {
			if (typeId.equals(currLauncherExtension.getTypeId())) {
				resultLaunchers.add(currLauncherExtension);
			}
		}
		return resultLaunchers.toArray(new LauncherExtension[resultLaunchers.size()]);
	}

	public synchronized Launcher[] getLaunchers() {
		List<Launcher> launchers = new ArrayList<Launcher>();
		LauncherExtension[] launcherExtensions = getLauncherExtensions();
		for (LauncherExtension launcherExtension : launcherExtensions) {
			Launcher launcher = launcherExtension.getLauncher();
			if (launcher != null && !launchers.contains(launcher)) {
				launchers.add(launcher);
			}
		}
		return launchers.toArray(new Launcher[launchers.size()]);
	}

	public synchronized Launcher[] getLaunchers(String typeId) {
		List<Launcher> launchers = new ArrayList<Launcher>();
		LauncherExtension[] launcherExtensions = getLauncherExtensions(typeId);
		for (LauncherExtension launcherExtension : launcherExtensions) {
			Launcher launcher = launcherExtension.getLauncher();
			if (launcher != null && !launchers.contains(launcher)) {
				launchers.add(launcher);
			}
		}
		return launchers.toArray(new Launcher[launchers.size()]);
	}

}
