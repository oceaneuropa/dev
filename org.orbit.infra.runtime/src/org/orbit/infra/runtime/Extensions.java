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
import org.orbit.infra.runtime.extensionregistry.ws.ExtensionRegistryServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryServiceActivator;
import org.orbit.infra.runtime.extensions.configregistry.ConfigRegistryServicePropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryPropertyTester;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayActivator;
import org.orbit.infra.runtime.extensions.extensionregistry.ExtensionRegistryRelayPropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServicePropertyTester;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayActivator;
import org.orbit.infra.runtime.extensions.indexservice.IndexServiceRelayPropertyTester;
import org.orbit.infra.runtime.subscription.ws.command.mapping.CreateMappingWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.DeleteMappingWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.GetMappingWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.ListMappingsWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.ListSourceTypesMappingOfTargetWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.ListTargetTypesMappingOfSourceWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.MappingExistsWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.mapping.UpdateMappingWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.CreateSourceWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.DeleteSourceWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.GetSourceWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.ListSourcesWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.SourceExistsWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.source.UpdateSourceWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.CreateSourceTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.DeleteSourceTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.GetSourceTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.ListSourceTypesWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.UpdateSourceTypeNameWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.sourcetype.UpdateSourceTypeTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.CreateTargetWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.DeleteTargetWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.GetTargetWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.ListTargetsWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.TargetExistsWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.target.UpdateTargetWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.CreateTargetTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.DeleteTargetTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.GetTargetTypeWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.ListTargetTypesWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.UpdateTargetTypeNameWSCommand;
import org.orbit.infra.runtime.subscription.ws.command.targettype.UpdateTargetTypeTypeWSCommand;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.command.CommandActivator;
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

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
		createIndexProviderExtensions();

		createEditPolicyCommandExtensions_ConfigRegistry();
		createEditPolicyCommandExtensions_Subscription();

		createCommandExtensions();
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
	}

	protected void createIndexProviderExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension11 = new Extension(extensionTypeId, InfraConstants.INDEX_SERVICE_INDEXER_ID, "Index Service Index Provider");
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, InfraConstants.EXTENSION_REGISTRY_INDEXER_ID, "Extension Registry Service Index Provider");
		extension12.addInterface(factoryClass, ExtensionRegistryServiceIndexTimerFactory.class);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, InfraConstants.CONFIG_REGISTRY__INDEXER_ID, "Config Registry Service Index Provider");
		extension13.addInterface(factoryClass, ConfigRegistryServiceIndexTimerFactory.class);
		addExtension(extension13);
	}

	protected void createEditPolicyCommandExtensions_ConfigRegistry() {
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

	protected void createEditPolicyCommandExtensions_Subscription() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = InfraConstants.SUBS_SERVER__SERVICE_NAME;

		// -----------------------------------------------------------------------------------
		// Source Types
		// -----------------------------------------------------------------------------------
		Extension extension41 = new Extension(extensionTypeId, ListSourceTypesWSCommand.ID);
		extension41.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc41 = new InterfaceDescription(WSCommand.class, ListSourceTypesWSCommand.class);
		desc41.setSingleton(false);
		extension41.addInterface(desc41);
		addExtension(extension41);

		Extension extension42 = new Extension(extensionTypeId, GetSourceTypeWSCommand.ID);
		extension42.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc42 = new InterfaceDescription(WSCommand.class, GetSourceTypeWSCommand.class);
		desc42.setSingleton(false);
		extension42.addInterface(desc42);
		addExtension(extension42);

		Extension extension43 = new Extension(extensionTypeId, CreateSourceTypeWSCommand.ID);
		extension43.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc43 = new InterfaceDescription(WSCommand.class, CreateSourceTypeWSCommand.class);
		desc43.setSingleton(false);
		extension43.addInterface(desc43);
		addExtension(extension43);

		Extension extension44 = new Extension(extensionTypeId, UpdateSourceTypeTypeWSCommand.ID);
		extension44.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc44 = new InterfaceDescription(WSCommand.class, UpdateSourceTypeTypeWSCommand.class);
		desc44.setSingleton(false);
		extension44.addInterface(desc44);
		addExtension(extension44);

		Extension extension45 = new Extension(extensionTypeId, UpdateSourceTypeNameWSCommand.ID);
		extension45.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc45 = new InterfaceDescription(WSCommand.class, UpdateSourceTypeNameWSCommand.class);
		desc45.setSingleton(false);
		extension45.addInterface(desc45);
		addExtension(extension45);

		Extension extension46 = new Extension(extensionTypeId, DeleteSourceTypeWSCommand.ID);
		extension46.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc46 = new InterfaceDescription(WSCommand.class, DeleteSourceTypeWSCommand.class);
		desc46.setSingleton(false);
		extension46.addInterface(desc46);
		addExtension(extension46);

		// -----------------------------------------------------------------------------------
		// Target Types
		// -----------------------------------------------------------------------------------
		Extension extension51 = new Extension(extensionTypeId, ListTargetTypesWSCommand.ID);
		extension51.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc51 = new InterfaceDescription(WSCommand.class, ListTargetTypesWSCommand.class);
		desc51.setSingleton(false);
		extension51.addInterface(desc51);
		addExtension(extension51);

		Extension extension52 = new Extension(extensionTypeId, GetTargetTypeWSCommand.ID);
		extension52.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc52 = new InterfaceDescription(WSCommand.class, GetTargetTypeWSCommand.class);
		desc52.setSingleton(false);
		extension52.addInterface(desc52);
		addExtension(extension52);

		Extension extension53 = new Extension(extensionTypeId, CreateTargetTypeWSCommand.ID);
		extension53.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc53 = new InterfaceDescription(WSCommand.class, CreateTargetTypeWSCommand.class);
		desc53.setSingleton(false);
		extension53.addInterface(desc53);
		addExtension(extension53);

		Extension extension54 = new Extension(extensionTypeId, UpdateTargetTypeTypeWSCommand.ID);
		extension54.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc54 = new InterfaceDescription(WSCommand.class, UpdateTargetTypeTypeWSCommand.class);
		desc54.setSingleton(false);
		extension54.addInterface(desc54);
		addExtension(extension54);

		Extension extension55 = new Extension(extensionTypeId, UpdateTargetTypeNameWSCommand.ID);
		extension55.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc55 = new InterfaceDescription(WSCommand.class, UpdateTargetTypeNameWSCommand.class);
		desc55.setSingleton(false);
		extension55.addInterface(desc55);
		addExtension(extension55);

		Extension extension56 = new Extension(extensionTypeId, DeleteTargetTypeWSCommand.ID);
		extension56.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc56 = new InterfaceDescription(WSCommand.class, DeleteTargetTypeWSCommand.class);
		desc56.setSingleton(false);
		extension56.addInterface(desc56);
		addExtension(extension56);

		// -----------------------------------------------------------------------------------
		// Sources
		// -----------------------------------------------------------------------------------
		Extension extension11 = new Extension(extensionTypeId, ListSourcesWSCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, ListSourcesWSCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, GetSourceWSCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, GetSourceWSCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(extensionTypeId, SourceExistsWSCommand.ID);
		extension13.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc13 = new InterfaceDescription(WSCommand.class, SourceExistsWSCommand.class);
		desc13.setSingleton(false);
		extension13.addInterface(desc13);
		addExtension(extension13);

		Extension extension14 = new Extension(extensionTypeId, CreateSourceWSCommand.ID);
		extension14.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc14 = new InterfaceDescription(WSCommand.class, CreateSourceWSCommand.class);
		desc14.setSingleton(false);
		extension14.addInterface(desc14);
		addExtension(extension14);

		Extension extension15 = new Extension(extensionTypeId, UpdateSourceWSCommand.ID);
		extension15.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc15 = new InterfaceDescription(WSCommand.class, UpdateSourceWSCommand.class);
		desc15.setSingleton(false);
		extension15.addInterface(desc15);
		addExtension(extension15);

		Extension extension16 = new Extension(extensionTypeId, DeleteSourceWSCommand.ID);
		extension16.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc16 = new InterfaceDescription(WSCommand.class, DeleteSourceWSCommand.class);
		desc16.setSingleton(false);
		extension16.addInterface(desc16);
		addExtension(extension16);

		// -----------------------------------------------------------------------------------
		// Targets
		// -----------------------------------------------------------------------------------
		Extension extension21 = new Extension(extensionTypeId, ListTargetsWSCommand.ID);
		extension21.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc21 = new InterfaceDescription(WSCommand.class, ListTargetsWSCommand.class);
		desc21.setSingleton(false);
		extension21.addInterface(desc21);
		addExtension(extension21);

		Extension extension22 = new Extension(extensionTypeId, GetTargetWSCommand.ID);
		extension22.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc22 = new InterfaceDescription(WSCommand.class, GetTargetWSCommand.class);
		desc22.setSingleton(false);
		extension22.addInterface(desc22);
		addExtension(extension22);

		Extension extension23 = new Extension(extensionTypeId, TargetExistsWSCommand.ID);
		extension23.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc23 = new InterfaceDescription(WSCommand.class, TargetExistsWSCommand.class);
		desc23.setSingleton(false);
		extension23.addInterface(desc23);
		addExtension(extension23);

		Extension extension24 = new Extension(extensionTypeId, CreateTargetWSCommand.ID);
		extension24.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc24 = new InterfaceDescription(WSCommand.class, CreateTargetWSCommand.class);
		desc24.setSingleton(false);
		extension24.addInterface(desc24);
		addExtension(extension24);

		Extension extension25 = new Extension(extensionTypeId, UpdateTargetWSCommand.ID);
		extension25.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc25 = new InterfaceDescription(WSCommand.class, UpdateTargetWSCommand.class);
		desc25.setSingleton(false);
		extension25.addInterface(desc25);
		addExtension(extension25);

		Extension extension26 = new Extension(extensionTypeId, DeleteTargetWSCommand.ID);
		extension26.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc26 = new InterfaceDescription(WSCommand.class, DeleteTargetWSCommand.class);
		desc26.setSingleton(false);
		extension26.addInterface(desc26);
		addExtension(extension26);

		// -----------------------------------------------------------------------------------
		// Mappings
		// -----------------------------------------------------------------------------------
		Extension extension31 = new Extension(extensionTypeId, ListMappingsWSCommand.ID);
		extension31.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc31 = new InterfaceDescription(WSCommand.class, ListMappingsWSCommand.class);
		desc31.setSingleton(false);
		extension31.addInterface(desc31);
		addExtension(extension31);

		Extension extension32 = new Extension(extensionTypeId, GetMappingWSCommand.ID);
		extension32.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc32 = new InterfaceDescription(WSCommand.class, GetMappingWSCommand.class);
		desc32.setSingleton(false);
		extension32.addInterface(desc32);
		addExtension(extension32);

		Extension extension33 = new Extension(extensionTypeId, MappingExistsWSCommand.ID);
		extension33.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc33 = new InterfaceDescription(WSCommand.class, MappingExistsWSCommand.class);
		desc33.setSingleton(false);
		extension33.addInterface(desc33);
		addExtension(extension33);

		Extension extension34 = new Extension(extensionTypeId, CreateMappingWSCommand.ID);
		extension34.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc34 = new InterfaceDescription(WSCommand.class, CreateMappingWSCommand.class);
		desc34.setSingleton(false);
		extension34.addInterface(desc34);
		addExtension(extension34);

		Extension extension35 = new Extension(extensionTypeId, UpdateMappingWSCommand.ID);
		extension35.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc35 = new InterfaceDescription(WSCommand.class, UpdateMappingWSCommand.class);
		desc35.setSingleton(false);
		extension35.addInterface(desc35);
		addExtension(extension35);

		Extension extension36 = new Extension(extensionTypeId, DeleteMappingWSCommand.ID);
		extension36.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc36 = new InterfaceDescription(WSCommand.class, DeleteMappingWSCommand.class);
		desc36.setSingleton(false);
		extension36.addInterface(desc36);
		addExtension(extension36);

		Extension extension37 = new Extension(extensionTypeId, ListTargetTypesMappingOfSourceWSCommand.ID);
		extension37.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc37 = new InterfaceDescription(WSCommand.class, ListTargetTypesMappingOfSourceWSCommand.class);
		desc37.setSingleton(false);
		extension37.addInterface(desc37);
		addExtension(extension37);

		Extension extension38 = new Extension(extensionTypeId, ListSourceTypesMappingOfTargetWSCommand.ID);
		extension38.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc38 = new InterfaceDescription(WSCommand.class, ListSourceTypesMappingOfTargetWSCommand.class);
		desc38.setSingleton(false);
		extension38.addInterface(desc38);
		addExtension(extension38);
	}

	protected void createCommandExtensions() {
		String extensionTypeId = CommandActivator.EXTENSION_TYPE_ID;

		// Infra server side command
		Extension extension1 = new Extension(extensionTypeId, InfraRuntimeCommand.ID, "Infra server side command", "Infra server side command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, InfraRuntimeCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}
