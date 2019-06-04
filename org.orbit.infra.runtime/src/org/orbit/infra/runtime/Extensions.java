package org.orbit.infra.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.cli.InfraRuntimeCommand;
import org.orbit.infra.runtime.configregistry.ws.ConfigRegistryServiceIndexTimerFactory;
import org.orbit.infra.runtime.configregistry.ws.command.ConfigElementExistsWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.ConfigRegistryExistsWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.CreateConfigElementWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.CreateConfigRegistryWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.DeleteConfigElementWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.DeleteConfigRegistryWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.GetConfigElementPathWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.GetConfigElementWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.GetConfigRegistryWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.ListConfigElementsWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.ListConfigRegistriesWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.RemoveConfigElementAttributeWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.RemoveConfigElementAttributesWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.RemoveConfigRegistryPropertiesWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.SetConfigElementAttributeWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.SetConfigElementAttributesWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.SetConfigRegistryPropertiesWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.SetConfigRegistryPropertyWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.UpdateConfigElementWSCommand;
import org.orbit.infra.runtime.configregistry.ws.command.UpdateConfigRegistryWSCommand;
import org.orbit.infra.runtime.datacast.ws.DataCastServiceIndexTimerFactory;
import org.orbit.infra.runtime.datacast.ws.command.channel.AllocateDataTubeIdForNewChannelCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.ChannelMetadataExistsCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.ClearChannelMetadataStatusCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.CreateChannelMetadataCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.DeleteChannelMetadataCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.GetChannelMetadataCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.ListChannelMetadatasCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.RemoveChannelMetadataAccountConfigsCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.RemoveChannelMetadataPropertiesCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.SetChannelMetadataAccountConfigsCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.SetChannelMetadataPropertiesCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.SetChannelMetadataStatusCommand;
import org.orbit.infra.runtime.datacast.ws.command.channel.UpdateChannelMetadataCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.CreateDataTubeConfigCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.DeleteDataTubeConfigCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.GetDataTubeConfigCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.ListDataTubeConfigsCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.RemoveDataTubeConfigPropertiesCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.SetDataTubeConfigPropertiesCommand;
import org.orbit.infra.runtime.datacast.ws.command.datatube.UpdateDataTubeConfigCommand;
import org.orbit.infra.runtime.datatube.ws.DataTubeServiceIndexTimerFactory;
import org.orbit.infra.runtime.datatube.ws.command.CreateRuntimeChannelCommand;
import org.orbit.infra.runtime.datatube.ws.command.DeleteRuntimeChannelCommand;
import org.orbit.infra.runtime.datatube.ws.command.GetRuntimeChannelCommand;
import org.orbit.infra.runtime.datatube.ws.command.ListRuntimeChannelsCommand;
import org.orbit.infra.runtime.datatube.ws.command.RuntimeChannelExistsCommand;
import org.orbit.infra.runtime.datatube.ws.command.RuntimeChannelOnActionCommand;
import org.orbit.infra.runtime.extensionregistry.ws.ExtensionRegistryServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryServiceActivator;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryServicePropertyTester;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceActivator;
import org.orbit.infra.runtime.extensions.datacast.DataCastServicePropertyTester;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceRelayActivator;
import org.orbit.infra.runtime.extensions.datacast.DataCastServiceRelayPropertyTester;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceActivator;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServicePropertyTester;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceRelayActivator;
import org.orbit.infra.runtime.extensions.datatube.DataTubeServiceRelayPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServicePropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayPropertyTester;
import org.orbit.infra.runtime.util.RankingProviderOfDataTubeNodesForChannel;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.ranking.RankingProvider;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ParameterDefinition;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.rest.editpolicy.WSCommand;
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

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
		createIndexProviderExtensions();

		createEditPolicyCommandExtensions_ExtensionRegistry();
		createEditPolicyCommandExtensions_DataCast();
		createEditPolicyCommandExtensions_DataTube();

		createCommandExtensions();

		createRankingProviderExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		// Index Service
		Extension extension11 = new Extension(extensionTypeId, IndexServiceActivator.ID, "Index Service", "Service for indexing.");
		InterfaceDescription desc11 = new InterfaceDescription(ServiceActivator.class, IndexServiceActivator.class);
		desc11.setParameterDefinitions( //
				new ParameterDefinition("component.index_service.name", "instance name", true, null), //
				new ParameterDefinition("component.index_service.context_root", "web service context root", true, null), //
				new ParameterDefinition("component.index_service.jdbc.driver", "JDBC driver", true, null), //
				new ParameterDefinition("component.index_service.jdbc.url", "JDBC URL", true, null), //
				new ParameterDefinition("component.index_service.jdbc.username", "JDBC username", true, null), //
				new ParameterDefinition("component.index_service.jdbc.password", "JDBC password", true, null) //
		);
		desc11.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServicePropertyTester.ID));
		extension11.addInterface(desc11);
		addExtension(extension11);

		// ExtensionRegistry Service
		Extension extension12 = new Extension(extensionTypeId, ExtensionRegistryActivator.ID, "Extension Registry Service", "Servicefor registering extensions.");
		InterfaceDescription desc12 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryActivator.class);
		desc12.setParameterDefinitions( //
				new ParameterDefinition("component.extension_registry.name", "instance name", true, null), //
				new ParameterDefinition("component.extension_registry.context_root", "web service context root", true, null), //
				new ParameterDefinition("component.extension_registry.jdbc.driver", "JDBC driver", true, null), //
				new ParameterDefinition("component.extension_registry.jdbc.url", "JDBC URL", true, null), //
				new ParameterDefinition("component.extension_registry.jdbc.username", "JDBC username", true, null), //
				new ParameterDefinition("component.extension_registry.jdbc.password", "JDBC password", true, null) //
		);
		desc12.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryPropertyTester.ID));
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Config Registry Service
		Extension extension13 = new Extension(extensionTypeId, ConfigRegistryServiceActivator.ID, "Config Registry Service", "Service for configuration registry.");
		InterfaceDescription desc13 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryServiceActivator.class);
		desc13.setParameterDefinitions( //
				new ParameterDefinition("infra.config_registry.name", "instance name", true, null), //
				new ParameterDefinition("infra.config_registry.context_root", "web service context root", true, null), //
				new ParameterDefinition("infra.config_registry.jdbc.driver", "JDBC driver", true, null), //
				new ParameterDefinition("infra.config_registry.jdbc.url", "JDBC URL", true, null), //
				new ParameterDefinition("infra.config_registry.jdbc.username", "JDBC username", true, null), //
				new ParameterDefinition("infra.config_registry.jdbc.password", "JDBC password", true, null) //
		);
		desc13.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryServicePropertyTester.ID));
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Data Cast Service
		Extension extension14 = new Extension(extensionTypeId, DataCastServiceActivator.ID, "DataCast Service", "Service for pushing data to clients through data tubes.");
		InterfaceDescription desc14 = new InterfaceDescription(ServiceActivator.class, DataCastServiceActivator.class);
		desc14.setParameterDefinitions( //
				new ParameterDefinition("infra.data_cast.id", "data tube id", true, null), //
				new ParameterDefinition("infra.data_cast.name", "service name", true, null), //
				new ParameterDefinition("infra.data_cast.context_root", "web service context root", true, null) //
		);
		desc14.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataCastServicePropertyTester.ID));
		extension14.addInterface(desc14);
		addExtension(extension14);

		// Data Tube Service
		Extension extension15 = new Extension(extensionTypeId, DataTubeServiceActivator.ID, "DataTube Service", "Service for broadcasting data to client.");
		InterfaceDescription desc15 = new InterfaceDescription(ServiceActivator.class, DataTubeServiceActivator.class);
		desc15.setParameterDefinitions( //
				new ParameterDefinition("infra.data_tube.data_cast_id", "data cast id", true, null), //
				new ParameterDefinition("infra.data_tube.id", "data tube id", true, null), //
				new ParameterDefinition("infra.data_tube.name", "service name", true, null), //
				new ParameterDefinition("infra.data_tube.context_root", "web service context root", true, null), //
				new ParameterDefinition("infra.data_tube.http_port", "web socket http port", true, null) //
		);
		desc15.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataTubeServicePropertyTester.ID));
		extension15.addInterface(desc15);
		addExtension(extension15);

		// Index Service Relay
		Extension extension21 = new Extension(extensionTypeId, IndexServiceRelayActivator.ID, "Index Service Relay", "Gateway for accessing distributed Index services.");
		InterfaceDescription desc21 = new InterfaceDescription(ServiceActivator.class, IndexServiceRelayActivator.class);
		desc21.setParameterDefinitions( //
				new ParameterDefinition(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT, "web service relay context root", true, null), //
				new ParameterDefinition(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS, "target urls", false, null) //
		);
		desc21.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IndexServiceRelayPropertyTester.ID));
		extension21.addInterface(desc21);
		addExtension(extension21);

		// Extension Registry Service Relay
		Extension extension22 = new Extension(extensionTypeId, ExtensionRegistryRelayActivator.ID, "Extension Registry Service Relay", "Gateway for accessing distributed Extension Registry services.");
		InterfaceDescription desc22 = new InterfaceDescription(ServiceActivator.class, ExtensionRegistryRelayActivator.class);
		desc22.setParameterDefinitions( //
				new ParameterDefinition(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_CONTEXT_ROOT, "web service relay context root", true, null), //
				new ParameterDefinition(InfraConstants.COMPONENT_EXTENSION_REGISTRY_RELAY_URLS, "target urls", false, null) //
		);
		desc22.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ExtensionRegistryRelayPropertyTester.ID));
		extension22.addInterface(desc22);
		addExtension(extension22);

		// Config Registry Service Relay
		Extension extension23 = new Extension(extensionTypeId, ConfigRegistryRelayActivator.ID, "Config Registry Service Relay", "Gateway for accessing distributed Config Registry services.");
		InterfaceDescription desc223 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryRelayActivator.class);
		desc223.setParameterDefinitions( //
				new ParameterDefinition(InfraConstants.CONFIG_REGISTRY__RELAY_CONTEXT_ROOT, "web service relay context root", true, null), //
				new ParameterDefinition(InfraConstants.CONFIG_REGISTRY__RELAY_URLS, "target urls", false, null) //
		);
		desc223.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryRelayPropertyTester.ID));
		extension23.addInterface(desc223);
		addExtension(extension23);

		// DataCast Service Relay
		Extension extension24 = new Extension(extensionTypeId, DataCastServiceRelayActivator.ID, "DataCast Service Relay", "Gateway for accessing distributed DataCast services.");
		InterfaceDescription desc24 = new InterfaceDescription(ServiceActivator.class, DataCastServiceRelayActivator.class);
		desc24.setParameterDefinitions( //
				new ParameterDefinition(InfraConstants.DATATUBE__RELAY_CONTEXT_ROOT, "web service relay context root", true, null), //
				new ParameterDefinition(InfraConstants.DATATUBE__RELAY_URLS, "target urls", false, null) //
		);
		desc24.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataCastServiceRelayPropertyTester.ID));
		extension24.addInterface(desc24);
		addExtension(extension24);

		// DataTube Service Relay
		Extension extension25 = new Extension(extensionTypeId, DataTubeServiceRelayActivator.ID, "DataTube Service Relay", "Gateway for accessing distributed DataTube services.");
		InterfaceDescription desc25 = new InterfaceDescription(ServiceActivator.class, DataTubeServiceRelayActivator.class);
		desc25.setParameterDefinitions( //
				new ParameterDefinition(InfraConstants.DATATUBE__RELAY_CONTEXT_ROOT, "web service relay context root", true, null), //
				new ParameterDefinition(InfraConstants.DATATUBE__RELAY_URLS, "target urls", false, null) //
		);
		desc25.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DataTubeServiceRelayPropertyTester.ID));
		extension25.addInterface(desc25);
		addExtension(extension25);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		// Index Service Property Tester
		Extension extension11 = new Extension(extensionTypeId, IndexServicePropertyTester.ID);
		InterfaceDescription desc11 = new InterfaceDescription(IPropertyTester.class, IndexServicePropertyTester.class);
		extension11.addInterface(desc11);
		addExtension(extension11);

		// Extension Registry Service Property Tester
		Extension extension12 = new Extension(extensionTypeId, ExtensionRegistryPropertyTester.ID);
		InterfaceDescription desc12 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryPropertyTester.class);
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Config Registry Service Property Tester
		Extension extension13 = new Extension(extensionTypeId, ConfigRegistryServicePropertyTester.ID);
		InterfaceDescription desc13 = new InterfaceDescription(IPropertyTester.class, ConfigRegistryServicePropertyTester.class);
		extension13.addInterface(desc13);
		addExtension(extension13);

		// DataCast Service Property Tester
		Extension extension14 = new Extension(extensionTypeId, DataCastServicePropertyTester.ID);
		InterfaceDescription desc14 = new InterfaceDescription(IPropertyTester.class, DataCastServicePropertyTester.class);
		extension14.addInterface(desc14);
		addExtension(extension14);

		// DataTube Service Property Tester
		Extension extension15 = new Extension(extensionTypeId, DataTubeServicePropertyTester.ID);
		InterfaceDescription desc15 = new InterfaceDescription(IPropertyTester.class, DataTubeServicePropertyTester.class);
		extension15.addInterface(desc15);
		addExtension(extension15);

		// Index Service Relay Property Tester
		Extension extension21 = new Extension(extensionTypeId, IndexServiceRelayPropertyTester.ID, "Index Service Relay Property Tester");
		InterfaceDescription desc21 = new InterfaceDescription(IPropertyTester.class, IndexServiceRelayPropertyTester.class);
		extension21.addInterface(desc21);
		addExtension(extension21);

		// Extension Registry Service Relay Property Tester
		Extension extension22 = new Extension(extensionTypeId, ExtensionRegistryRelayPropertyTester.ID, "Extension Registry Service Relay Property Tester");
		InterfaceDescription desc22 = new InterfaceDescription(IPropertyTester.class, ExtensionRegistryRelayPropertyTester.class);
		extension22.addInterface(desc22);
		addExtension(extension22);

		// Config Registry Service Relay Property Tester
		Extension extension23 = new Extension(extensionTypeId, ConfigRegistryRelayPropertyTester.ID, "Config Registry Service Relay Property Tester");
		InterfaceDescription desc23 = new InterfaceDescription(IPropertyTester.class, ConfigRegistryRelayPropertyTester.class);
		extension23.addInterface(desc23);
		addExtension(extension23);

		// DataCast Service Relay Property Tester
		Extension extension24 = new Extension(extensionTypeId, DataCastServiceRelayPropertyTester.ID, "DataCast Service Relay Property Tester");
		InterfaceDescription desc24 = new InterfaceDescription(IPropertyTester.class, DataCastServiceRelayPropertyTester.class);
		extension24.addInterface(desc24);
		addExtension(extension24);

		// DataTube Service Relay Property Tester
		Extension extension25 = new Extension(extensionTypeId, DataTubeServiceRelayPropertyTester.ID, "DataTube Service Relay Property Tester");
		InterfaceDescription desc25 = new InterfaceDescription(IPropertyTester.class, DataTubeServiceRelayPropertyTester.class);
		extension25.addInterface(desc25);
		addExtension(extension25);
	}

	protected void createIndexProviderExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension11 = new Extension(extensionTypeId, InfraConstants.INDEX_SERVICE_INDEXER_ID, "Index Service Index Provider");
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "Extension Registry Service Index Provider");
		extension12.addInterface(factoryClass, ExtensionRegistryServiceIndexTimerFactory.class);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "Config Registry Service Index Provider");
		extension13.addInterface(factoryClass, ConfigRegistryServiceIndexTimerFactory.class);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, InfraConstants.IDX__DATACAST__INDEXER_ID, "DataCast Service Index Provider");
		extension14.addInterface(factoryClass, DataCastServiceIndexTimerFactory.class);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, InfraConstants.IDX__DATATUBE__INDEXER_ID, "DataTube Service Index Provider");
		extension15.addInterface(factoryClass, DataTubeServiceIndexTimerFactory.class);
		addExtension(extension15);
	}

	protected void createEditPolicyCommandExtensions_ExtensionRegistry() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = InfraConstants.CONFIG_REGISTRY__SERVICE_NAME;

		// -----------------------------------------------------------------------------------
		// Config Registries
		// -----------------------------------------------------------------------------------
		Extension extension11 = new Extension(extensionTypeId, ListConfigRegistriesWSCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, ListConfigRegistriesWSCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, GetConfigRegistryWSCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, GetConfigRegistryWSCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, ConfigRegistryExistsWSCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, ConfigRegistryExistsWSCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, CreateConfigRegistryWSCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, CreateConfigRegistryWSCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, UpdateConfigRegistryWSCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, UpdateConfigRegistryWSCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, SetConfigRegistryPropertyWSCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, SetConfigRegistryPropertyWSCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		Extension extension17 = new Extension(extensionTypeId, SetConfigRegistryPropertiesWSCommand.ID);
		extension17.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc17 = new InterfaceDescription(WSCommand.class, SetConfigRegistryPropertiesWSCommand.class);
		desc17.setSingleton(false);
		extension17.addInterface(desc17);
		addExtension(extension17);

		Extension extension18 = new Extension(extensionTypeId, RemoveConfigRegistryPropertiesWSCommand.ID);
		extension18.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc18 = new InterfaceDescription(WSCommand.class, RemoveConfigRegistryPropertiesWSCommand.class);
		desc18.setSingleton(false);
		extension18.addInterface(desc18);
		addExtension(extension18);

		Extension extension19 = new Extension(extensionTypeId, DeleteConfigRegistryWSCommand.ID);
		extension19.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc19 = new InterfaceDescription(WSCommand.class, DeleteConfigRegistryWSCommand.class);
		desc19.setSingleton(false);
		extension19.addInterface(desc19);
		addExtension(extension19);

		// -----------------------------------------------------------------------------------
		// Config Elements
		// -----------------------------------------------------------------------------------
		Extension extension21 = new Extension(extensionTypeId, ListConfigElementsWSCommand.ID);
		extension21.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc21 = new InterfaceDescription(WSCommand.class, ListConfigElementsWSCommand.class);
		desc21.setSingleton(false);
		extension21.addInterface(desc21);
		addExtension(extension21);

		Extension extension22 = new Extension(extensionTypeId, GetConfigElementWSCommand.ID);
		extension22.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc22 = new InterfaceDescription(WSCommand.class, GetConfigElementWSCommand.class);
		desc22.setSingleton(false);
		extension22.addInterface(desc22);
		addExtension(extension22);

		Extension extension23 = new Extension(extensionTypeId, GetConfigElementPathWSCommand.ID);
		extension23.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc23 = new InterfaceDescription(WSCommand.class, GetConfigElementPathWSCommand.class);
		desc23.setSingleton(false);
		extension23.addInterface(desc23);
		addExtension(extension23);

		Extension extension24 = new Extension(extensionTypeId, ConfigElementExistsWSCommand.ID);
		extension24.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc24 = new InterfaceDescription(WSCommand.class, ConfigElementExistsWSCommand.class);
		desc24.setSingleton(false);
		extension24.addInterface(desc24);
		addExtension(extension24);

		Extension extension25 = new Extension(extensionTypeId, CreateConfigElementWSCommand.ID);
		extension25.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc25 = new InterfaceDescription(WSCommand.class, CreateConfigElementWSCommand.class);
		desc25.setSingleton(false);
		extension25.addInterface(desc25);
		addExtension(extension25);

		Extension extension26 = new Extension(extensionTypeId, UpdateConfigElementWSCommand.ID);
		extension26.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc26 = new InterfaceDescription(WSCommand.class, UpdateConfigElementWSCommand.class);
		desc26.setSingleton(false);
		extension26.addInterface(desc26);
		addExtension(extension26);

		Extension extension27 = new Extension(extensionTypeId, SetConfigElementAttributeWSCommand.ID);
		extension27.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc27 = new InterfaceDescription(WSCommand.class, SetConfigElementAttributeWSCommand.class);
		desc27.setSingleton(false);
		extension27.addInterface(desc27);
		addExtension(extension27);

		Extension extension28 = new Extension(extensionTypeId, SetConfigElementAttributesWSCommand.ID);
		extension28.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc28 = new InterfaceDescription(WSCommand.class, SetConfigElementAttributesWSCommand.class);
		desc28.setSingleton(false);
		extension28.addInterface(desc28);
		addExtension(extension28);

		Extension extension29 = new Extension(extensionTypeId, RemoveConfigElementAttributeWSCommand.ID);
		extension29.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc29 = new InterfaceDescription(WSCommand.class, RemoveConfigElementAttributeWSCommand.class);
		desc29.setSingleton(false);
		extension29.addInterface(desc29);
		addExtension(extension29);

		Extension extension30 = new Extension(extensionTypeId, RemoveConfigElementAttributesWSCommand.ID);
		extension30.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc30 = new InterfaceDescription(WSCommand.class, RemoveConfigElementAttributesWSCommand.class);
		desc30.setSingleton(false);
		extension30.addInterface(desc30);
		addExtension(extension30);

		Extension extension31 = new Extension(extensionTypeId, DeleteConfigElementWSCommand.ID);
		extension31.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc31 = new InterfaceDescription(WSCommand.class, DeleteConfigElementWSCommand.class);
		desc31.setSingleton(false);
		extension31.addInterface(desc31);
		addExtension(extension31);
	}

	protected void createEditPolicyCommandExtensions_DataCast() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = InfraConstants.DATACAST__SERVICE_NAME;

		// -----------------------------------------------------------------------------------
		// DataCast - DataTube configs
		// -----------------------------------------------------------------------------------
		Extension extension11 = new Extension(extensionTypeId, ListDataTubeConfigsCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, ListDataTubeConfigsCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, GetDataTubeConfigCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, GetDataTubeConfigCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, CreateDataTubeConfigCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, CreateDataTubeConfigCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, UpdateDataTubeConfigCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, UpdateDataTubeConfigCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, SetDataTubeConfigPropertiesCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, SetDataTubeConfigPropertiesCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, RemoveDataTubeConfigPropertiesCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, RemoveDataTubeConfigPropertiesCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		Extension extension17 = new Extension(extensionTypeId, DeleteDataTubeConfigCommand.ID);
		extension17.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc17 = new InterfaceDescription(WSCommand.class, DeleteDataTubeConfigCommand.class);
		desc17.setSingleton(false);
		extension17.addInterface(desc17);
		addExtension(extension17);

		// -----------------------------------------------------------------------------------
		// DataCast - ChannelMetadatas
		// -----------------------------------------------------------------------------------
		Extension extension21 = new Extension(extensionTypeId, ListChannelMetadatasCommand.ID);
		extension21.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc21 = new InterfaceDescription(WSCommand.class, ListChannelMetadatasCommand.class);
		desc21.setSingleton(false);
		extension21.addInterface(desc21);
		addExtension(extension21);

		Extension extension22 = new Extension(extensionTypeId, GetChannelMetadataCommand.ID);
		extension22.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc22 = new InterfaceDescription(WSCommand.class, GetChannelMetadataCommand.class);
		desc22.setSingleton(false);
		extension22.addInterface(desc22);
		addExtension(extension22);

		Extension extension23 = new Extension(extensionTypeId, ChannelMetadataExistsCommand.ID);
		extension23.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc23 = new InterfaceDescription(WSCommand.class, ChannelMetadataExistsCommand.class);
		desc23.setSingleton(false);
		extension23.addInterface(desc23);
		addExtension(extension23);

		Extension extension24 = new Extension(extensionTypeId, CreateChannelMetadataCommand.ID);
		extension24.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc24 = new InterfaceDescription(WSCommand.class, CreateChannelMetadataCommand.class);
		desc24.setSingleton(false);
		extension24.addInterface(desc24);
		addExtension(extension24);

		Extension extension25 = new Extension(extensionTypeId, AllocateDataTubeIdForNewChannelCommand.ID);
		extension25.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc25 = new InterfaceDescription(WSCommand.class, AllocateDataTubeIdForNewChannelCommand.class);
		desc25.setSingleton(false);
		extension25.addInterface(desc25);
		addExtension(extension25);

		Extension extension26 = new Extension(extensionTypeId, UpdateChannelMetadataCommand.ID);
		extension26.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc26 = new InterfaceDescription(WSCommand.class, UpdateChannelMetadataCommand.class);
		desc26.setSingleton(false);
		extension26.addInterface(desc26);
		addExtension(extension26);

		Extension extension27 = new Extension(extensionTypeId, SetChannelMetadataStatusCommand.ID);
		extension27.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc27 = new InterfaceDescription(WSCommand.class, SetChannelMetadataStatusCommand.class);
		desc27.setSingleton(false);
		extension27.addInterface(desc27);
		addExtension(extension27);

		Extension extension28 = new Extension(extensionTypeId, ClearChannelMetadataStatusCommand.ID);
		extension28.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc28 = new InterfaceDescription(WSCommand.class, ClearChannelMetadataStatusCommand.class);
		desc28.setSingleton(false);
		extension28.addInterface(desc28);
		addExtension(extension28);

		Extension extension29 = new Extension(extensionTypeId, SetChannelMetadataAccountConfigsCommand.ID);
		extension29.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc29 = new InterfaceDescription(WSCommand.class, SetChannelMetadataAccountConfigsCommand.class);
		desc29.setSingleton(false);
		extension29.addInterface(desc29);
		addExtension(extension29);

		Extension extension30 = new Extension(extensionTypeId, RemoveChannelMetadataAccountConfigsCommand.ID);
		extension30.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc30 = new InterfaceDescription(WSCommand.class, RemoveChannelMetadataAccountConfigsCommand.class);
		desc30.setSingleton(false);
		extension30.addInterface(desc30);
		addExtension(extension30);

		Extension extension31 = new Extension(extensionTypeId, SetChannelMetadataPropertiesCommand.ID);
		extension31.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc31 = new InterfaceDescription(WSCommand.class, SetChannelMetadataPropertiesCommand.class);
		desc31.setSingleton(false);
		extension31.addInterface(desc31);
		addExtension(extension31);

		Extension extension32 = new Extension(extensionTypeId, RemoveChannelMetadataPropertiesCommand.ID);
		extension32.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc32 = new InterfaceDescription(WSCommand.class, RemoveChannelMetadataPropertiesCommand.class);
		desc32.setSingleton(false);
		extension32.addInterface(desc32);
		addExtension(extension32);

		Extension extension33 = new Extension(extensionTypeId, DeleteChannelMetadataCommand.ID);
		extension33.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc33 = new InterfaceDescription(WSCommand.class, DeleteChannelMetadataCommand.class);
		desc33.setSingleton(false);
		extension33.addInterface(desc33);
		addExtension(extension33);
	}

	protected void createEditPolicyCommandExtensions_DataTube() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = InfraConstants.DATATUBE__SERVICE_NAME;

		// -----------------------------------------------------------------------------------
		// DataTube - RuntimeChannels
		// -----------------------------------------------------------------------------------
		Extension extension11 = new Extension(extensionTypeId, ListRuntimeChannelsCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, ListRuntimeChannelsCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, GetRuntimeChannelCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, GetRuntimeChannelCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, RuntimeChannelExistsCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, RuntimeChannelExistsCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, CreateRuntimeChannelCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, CreateRuntimeChannelCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, RuntimeChannelOnActionCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, RuntimeChannelOnActionCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, DeleteRuntimeChannelCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, DeleteRuntimeChannelCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// Infra server side command
		Extension extension1 = new Extension(extensionTypeId, InfraRuntimeCommand.ID, "Infra server side command", "Infra server side command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, InfraRuntimeCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

	protected void createRankingProviderExtensions() {
		String extensionTypeId = RankingProvider.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, RankingProviderOfDataTubeNodesForChannel.ID, "Ranking Provider of Data Tube Nodes for new Channel");
		extension1.setProperty(RankingProvider.PROP__RANKING_TARGET, InfraConstants.RANKING_PROVIDER__DATA_TUBE_NODES__NEW_CHANNEL);
		InterfaceDescription desc1 = new InterfaceDescription(RankingProvider.class, RankingProviderOfDataTubeNodesForChannel.class);
		desc1.setSingleton(true);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

// Extension extension15 = new Extension(extensionTypeId, SyncChannelMetadataCommand.ID);
// extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
// InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, SyncChannelMetadataCommand.class);
// desc15.setSingleton(false);
// extension15.addInterface(desc15);
// addExtension(extension15);
