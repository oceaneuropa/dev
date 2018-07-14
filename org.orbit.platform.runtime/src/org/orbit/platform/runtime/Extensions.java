package org.orbit.platform.runtime;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.runtime.cli.PlatformCommand;
import org.orbit.platform.runtime.command.ws.CommandServiceIndexTimerFactory;
import org.orbit.platform.runtime.core.ws.PlatformIndexTimerFactory;
import org.orbit.platform.runtime.extensions.PlatformNodeBuilder;
import org.orbit.platform.runtime.extensions.PlatformNodeBuilderPropertyTester;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.resources.extension.ResourceBuilder;
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
		createCommandExtensions();
		createIndexProviderExtensions();
	}

	protected void createFolderConfiguratorExtensions() {
		Extension extension = new Extension(ResourceBuilder.TYPE_ID, PlatformNodeBuilder.ID, "Platform Node Configurator");
		InterfaceDescription desc = new InterfaceDescription(ResourceBuilder.class, PlatformNodeBuilder.class);
		desc.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(PlatformNodeBuilderPropertyTester.ID));
		extension.addInterface(desc);
		addExtension(extension);
	}

	protected void createPropertyTesterExtensions() {
		Extension extension = new Extension(IPropertyTester.TYPE_ID, PlatformNodeBuilderPropertyTester.ID);
		extension.addInterface(IPropertyTester.class, PlatformNodeBuilderPropertyTester.class);
		addExtension(extension);
	}

	protected void createCommandExtensions() {
		// Services command
		Extension extension1 = new Extension(CommandActivator.TYPE_ID, PlatformCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, PlatformCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createIndexProviderExtensions() {
		String typeId = InfraConstants.INDEX_PROVIDER_EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension1 = new Extension(typeId, PlatformConstants.PLATFORM_INDEXER_ID, "Platform Index Provider");
		extension1.addInterface(factoryClass, PlatformIndexTimerFactory.class);
		addExtension(extension1);

		Extension extension2 = new Extension(typeId, PlatformConstants.COMMAND_SERVICE_INDEXER_ID, "Command Service Index Provider");
		extension2.addInterface(factoryClass, CommandServiceIndexTimerFactory.class);
		addExtension(extension1);
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
