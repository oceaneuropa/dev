package org.origin.common.launch;

import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.launch.launcher.OSGiLauncher;
import org.origin.common.launch.launcher.ScriptLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.origin.common.launch");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createLaunchTypeExtensions();
		createLauncherExtensions();
	}

	protected void createLaunchTypeExtensions() {
		Extension extension = new Extension(LaunchType.TYPE_ID, LaunchType.Types.NODE.getId(), LaunchType.Types.NODE.getName());
		addExtension(extension);
	}

	protected void createLauncherExtensions() {
		Extension extension1 = new Extension(Launcher.TYPE_ID, OSGiLauncher.ID, "OSGi Launcher");
		extension1.setProperty(Launcher.PROP_TYPE_ID, LaunchType.Types.NODE.getId());
		InterfaceDescription desc1 = new InterfaceDescription(Launcher.class, OSGiLauncher.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(Launcher.TYPE_ID, ScriptLauncher.ID, "Script Launcher");
		extension2.setProperty(Launcher.PROP_TYPE_ID, LaunchType.Types.NODE.getId());
		InterfaceDescription desc2 = new InterfaceDescription(Launcher.class, ScriptLauncher.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

}

// extension.setProperty(LaunchType.PROP_LAUNCHER_IDS, new String[] { NodeLauncher.ID });
