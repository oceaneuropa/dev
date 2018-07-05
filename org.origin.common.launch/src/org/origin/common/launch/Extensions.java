package org.origin.common.launch;

import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.launch.launcher.OSGiLauncher;
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
		Extension extension = new Extension(LaunchType.TYPE_ID, "node", "Node");
		// extension.setProperty(LaunchType.PROP_LAUNCHER_IDS, new String[] { NodeLauncher.ID });
		addExtension(extension);
	}

	protected void createLauncherExtensions() {
		Extension extension = new Extension(Launcher.TYPE_ID, OSGiLauncher.ID, "OSGi Launcher");
		extension.setProperty(Launcher.PROP_TYPE_ID, "node");
		InterfaceDescription desc = new InterfaceDescription(Launcher.class, OSGiLauncher.class);
		extension.addInterface(desc);
		addExtension(extension);
	}

}
