package org.orbit.platform.runtime;

import org.orbit.platform.runtime.extensions.PlatformNodePreConfigurator;
import org.orbit.platform.runtime.extensions.PlatformNodePreConfiguratorPropertyTester;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.resources.extension.ResourceConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.platform.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createFolderConfiguratorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createFolderConfiguratorExtensions() {
		Extension extension = new Extension(ResourceConfigurator.TYPE_ID, PlatformNodePreConfigurator.ID, "Platform Node Configurator");
		InterfaceDescription desc = new InterfaceDescription(ResourceConfigurator.class, PlatformNodePreConfigurator.class);
		desc.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(PlatformNodePreConfiguratorPropertyTester.ID));
		extension.addInterface(desc);
		addExtension(extension);
	}

	protected void createPropertyTesterExtensions() {
		Extension extension = new Extension(IPropertyTester.TYPE_ID, PlatformNodePreConfiguratorPropertyTester.ID);
		extension.addInterface(IPropertyTester.class, PlatformNodePreConfiguratorPropertyTester.class);
		addExtension(extension);
	}

}

// createLaunchTypeExtensions();
// createLauncherExtensions();

// protected void createLaunchTypeExtensions() {
// Extension extension = new Extension(LaunchType.TYPE_ID, "node", "Node");
// // extension.setProperty(LaunchType.PROP_LAUNCHER_IDS, new String[] { NodeLauncher.ID });
// addExtension(extension);
// }

// protected void createLauncherExtensions() {
// Extension extension = new Extension(Launcher.TYPE_ID, NodeLauncher.ID, "Platform Node Launcher");
// extension.setProperty(Launcher.PROP_TYPE_ID, "node");
// InterfaceDescription desc = new InterfaceDescription(Launcher.class, NodeLauncher.class);
// extension.addInterface(desc);
// addExtension(extension);
// }
