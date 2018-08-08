package org.orbit.infra.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.channel.ws.ChannelServiceIndexTimerFactory;
import org.orbit.infra.runtime.cli.InfraCommand;
import org.orbit.infra.runtime.extensionregistry.ws.ExtensionRegistryServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensions.channelservice.ChannelServiceActivator;
import org.orbit.infra.runtime.extensions.channelservice.ChannelServicePropertyTester;
import org.orbit.infra.runtime.extensions.channelservice.ChannelServiceRelayActivator;
import org.orbit.infra.runtime.extensions.channelservice.ChannelServiceRelayPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServicePropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayPropertyTester;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.Parameter;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infra extensions
 *
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.infra.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions1();
		createServiceActivatorExtensions2();

		createPropertyTesterExtensions1();
		createPropertyTesterExtensions2();

		createCommandExtensions();
		createIndexProvideExtensions();
	}

	protected void createServiceActivatorExtensions1() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Index Service
		Extension extension1 = new Extension(extensionTypeId, IndexServiceActivator.ID, "Index Service Activator", "Index Service activator description");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, IndexServiceActivator.class);
		desc1.setParameters( //
				new Parameter("component.index_service.name", "instance name"), //
				new Parameter("component.index_service.context_root", "web service context root"), //
				new Parameter("component.index_service.jdbc.driver", "JDBC driver"), //
				new Parameter("component.index_service.jdbc.url", "JDBC URL"), //
				new Parameter("component.index_service.jdbc.username", "JDBC username"), //
				new Parameter("component.index_service.jdbc.password", "JDBC password") //
		);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// ExtensionRegistry Service
		Extension extension2 = new Extension(extensionTypeId, ExtensionRegistryActivator.ID, "Extension Registry Service Activator", "Extension Registry Service activator description");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryActivator.class);
		desc2.setParameters( //
				new Parameter("component.extension_registry.name", "instance name"), //
				new Parameter("component.extension_registry.context_root", "web service context root"), //
				new Parameter("component.extension_registry.jdbc.driver", "JDBC driver"), //
				new Parameter("component.extension_registry.jdbc.url", "JDBC URL"), //
				new Parameter("component.extension_registry.jdbc.username", "JDBC username"), //
				new Parameter("component.extension_registry.jdbc.password", "JDBC password") //
		);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryPropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Channel Service
		Extension extension3 = new Extension(extensionTypeId, ChannelServiceActivator.ID, "Channel Service Activator", "Channel Service activator description");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ChannelServiceActivator.class);
		desc3.setParameters( //
				new Parameter("component.channel.name", "instance name"), //
				new Parameter("component.channel.context_root", "web service context root"), //
				new Parameter("component.channel.http_port", "web socket http port") //
		);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ChannelServicePropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);
	}

	protected void createPropertyTesterExtensions1() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Index Service Property Tester
		Extension extension1 = new Extension(extensionTypeId, IndexServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, IndexServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Extension Registry Service Property Tester
		Extension extension2 = new Extension(extensionTypeId, ExtensionRegistryPropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryPropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Channel Service Property Tester
		Extension extension3 = new Extension(extensionTypeId, ChannelServicePropertyTester.ID);
		InterfaceDescription desc3 = new InterfaceDescription(IPropertyTester.class, ChannelServicePropertyTester.class);
		extension3.addInterface(desc3);
		addExtension(extension3);
	}

	protected void createServiceActivatorExtensions2() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Index Service Relay
		Extension extension4 = new Extension(extensionTypeId, IndexServiceRelayActivator.ID, "Index Service Relay Activator");
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, IndexServiceRelayActivator.class);
		desc4.setParameters( //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls") //
		);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServiceRelayPropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Extension Registry Service Relay
		Extension extension5 = new Extension(extensionTypeId, ExtensionRegistryRelayActivator.ID, "Extension Registry Service Relay Activator");
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryRelayActivator.class);
		desc5.setParameters( //
				new Parameter(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_URLS, "target urls") //
		);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryRelayPropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Channel Service Relay
		Extension extension6 = new Extension(extensionTypeId, ChannelServiceRelayActivator.ID, "Channel Service Relay Activator");
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, ChannelServiceRelayActivator.class);
		desc6.setParameters( //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT, "web service relay context root"), //
				new Parameter(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS, "target urls") //
		);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ChannelServiceRelayPropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);
	}

	protected void createPropertyTesterExtensions2() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Index Service Relay Property Tester
		Extension extension4 = new Extension(extensionTypeId, IndexServiceRelayPropertyTester.ID, "Index Service Relay Property Tester");
		InterfaceDescription desc4 = new InterfaceDescription(IPropertyTester.class, IndexServiceRelayPropertyTester.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Extension Registry Service Relay Property Tester
		Extension extension5 = new Extension(extensionTypeId, ExtensionRegistryRelayPropertyTester.ID, "Extension Registry Service Relay Property Tester");
		InterfaceDescription desc5 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryRelayPropertyTester.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Channel Service Relay Property Tester
		Extension extension6 = new Extension(extensionTypeId, ChannelServiceRelayPropertyTester.ID, "Channel Service Relay Property Tester");
		InterfaceDescription desc6 = new InterfaceDescription(IPropertyTester.class, ChannelServiceRelayPropertyTester.class);
		extension6.addInterface(desc6);
		addExtension(extension6);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// Infra server side command
		Extension extension1 = new Extension(extensionTypeId, InfraCommand.ID, "Infra server side command", "Infra server side command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, InfraCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createIndexProvideExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension1 = new Extension(extensionTypeId, InfraConstants.INDEX_SERVICE_INDEXER_ID, "Index Service Index Provider");
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "Extension Registry Service Index Provider");
		extension2.addInterface(factoryClass, ExtensionRegistryServiceIndexTimerFactory.class);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, InfraConstants.CHANNEL_INDEXER_ID, "Channel Service Index Provider");
		extension3.addInterface(factoryClass, ChannelServiceIndexTimerFactory.class);
		addExtension(extension3);
	}

}
