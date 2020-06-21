package org.orbit.component.runtime;

import org.orbit.component.runtime.cli.DomainManagementRuntimeCommand;
import org.orbit.component.runtime.cli.NodeControlRuntimeCommand;
import org.orbit.component.runtime.cli.ServicesRuntimeCommand;
import org.orbit.component.runtime.extension.appstore.AppStoreRelayActivator;
import org.orbit.component.runtime.extension.appstore.AppStoreRelayPropertyTester;
import org.orbit.component.runtime.extension.appstore.AppStoreServiceActivator;
import org.orbit.component.runtime.extension.appstore.AppStoreServicePropertyTester;
import org.orbit.component.runtime.extension.auth.AuthRelayActivator;
import org.orbit.component.runtime.extension.auth.AuthRelayPropertyTester;
import org.orbit.component.runtime.extension.auth.AuthServiceActivator;
import org.orbit.component.runtime.extension.auth.AuthServicePropertyTester;
import org.orbit.component.runtime.extension.configregistry.ConfigRegistryRelayActivator;
import org.orbit.component.runtime.extension.configregistry.ConfigRegistryRelayPropertyTester;
import org.orbit.component.runtime.extension.configregistry.ConfigRegistryServicePropertyTester;
import org.orbit.component.runtime.extension.domain.DomainManagementRelayActivator;
import org.orbit.component.runtime.extension.domain.DomainManagementRelayPropertyTester;
import org.orbit.component.runtime.extension.domain.DomainManagementServiceActivator;
import org.orbit.component.runtime.extension.domain.DomainManagementServicePropertyTester;
import org.orbit.component.runtime.extension.identity.IdentityRelayActivator;
import org.orbit.component.runtime.extension.identity.IdentityRelayPropertyTester;
import org.orbit.component.runtime.extension.identity.IdentityServiceActivator;
import org.orbit.component.runtime.extension.identity.IdentityServicePropertyTester;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlRelayActivator;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlRelayPropertyTester;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlServiceActivator;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlServicePropertyTester;
import org.orbit.component.runtime.extension.nodecontrol.NodeConfigIniPropertiesRegulator;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlRelayActivator;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlRelayPropertyTester;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlServiceActivator;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlServicePropertyTester;
import org.orbit.component.runtime.extension.userregistry.UserRegistryRelayActivator;
import org.orbit.component.runtime.extension.userregistry.UserRegistryRelayPropertyTester;
import org.orbit.component.runtime.extension.userregistry.UserRegistryServiceActivator;
import org.orbit.component.runtime.extension.userregistry.UserRegistryServicePropertyTester;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryServiceIndexTimerFactory;
import org.orbit.component.runtime.tier1.identity.ws.IdentityServiceTimerFactory;
import org.orbit.component.runtime.tier2.appstore.ws.AppStoreServiceIndexTimerFactory;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceTimerFactory;
import org.orbit.component.runtime.tier3.nodecontrol.ws.NodeControlServiceTimerFactory;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeAttributeAddWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeAttributeDeleteWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeAttributeUpdateWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeCreateWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeDeleteWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeExistWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeGetWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeListWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeStartWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeStatusWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeStopWSCommand;
import org.orbit.component.runtime.tier3.nodecontrol.ws.command.NodeUpdateWSCommand;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.common.PropertiesRegulator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.rest.editpolicy.WSCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	// public Extensions() {
	// setBundleId("org.orbit.component.runtime");
	// }

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		// For regular services
		createServiceActivatorExtensions1();
		createPropertyTesterExtensions1();

		// For Relays
		createServiceActivatorExtensions2();
		createPropertyTesterExtensions2();

		createCommandExtensions();
		createIndexProvideExtensions();

		createNodeControlEditPolicyCommandExtensions();

		createPropertiesRegulatorExtensions();
	}

	protected void createServiceActivatorExtensions1() {
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// tier 1
		Extension extension11 = new Extension(typeId, IdentityServiceActivator.ID, "Identity Service", "Service for user login/logout");
		InterfaceDescription desc11 = new InterfaceDescription(ServiceActivator.class, IdentityServiceActivator.class);
		desc11.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IdentityServicePropertyTester.ID));
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(typeId, UserRegistryServiceActivator.ID, "User Registration Service", "Service for managing user registrations.");
		InterfaceDescription desc12 = new InterfaceDescription(ServiceActivator.class, UserRegistryServiceActivator.class);
		desc12.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(UserRegistryServicePropertyTester.ID));
		extension12.addInterface(desc12);
		addExtension(extension12);

		Extension extension13 = new Extension(typeId, AuthServiceActivator.ID, "Auth Service", "Service for user authentication.");
		InterfaceDescription desc13 = new InterfaceDescription(ServiceActivator.class, AuthServiceActivator.class);
		desc13.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AuthServicePropertyTester.ID));
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Extension extension14 = new Extension(typeId, ConfigRegistryServiceActivator.ID, "Config Registration Service", "Service for distributed
		// configuration registrions.");
		// InterfaceDescription desc14 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryServiceActivator.class);
		// desc14.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryServicePropertyTester.ID));
		// extension14.addInterface(desc14);
		// addExtension(extension14);

		// tier 2
		Extension extension21 = new Extension(typeId, AppStoreServiceActivator.ID, "App Store Service", "Service for managing apps.");
		InterfaceDescription desc21 = new InterfaceDescription(ServiceActivator.class, AppStoreServiceActivator.class);
		desc21.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AppStoreServicePropertyTester.ID));
		extension21.addInterface(desc21);
		addExtension(extension21);

		// tier 3
		Extension extension31 = new Extension(typeId, DomainManagementServiceActivator.ID, "Domain Management Service", "Service for managing machines, domains and nodes metadata.");
		InterfaceDescription desc31 = new InterfaceDescription(ServiceActivator.class, DomainManagementServiceActivator.class);
		desc31.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DomainManagementServicePropertyTester.ID));
		extension31.addInterface(desc31);
		addExtension(extension31);

		Extension extension32 = new Extension(typeId, NodeControlServiceActivator.ID, "Node Control Service", "Service for controlling Node lifecycles");
		InterfaceDescription desc32 = new InterfaceDescription(ServiceActivator.class, NodeControlServiceActivator.class);
		desc32.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(NodeControlServicePropertyTester.ID));
		extension32.addInterface(desc32);
		addExtension(extension32);

		// tier 4
		Extension extension41 = new Extension(typeId, MissionControlServiceActivator.ID, "Mission Control Service", "Service for controlling missions.");
		InterfaceDescription desc41 = new InterfaceDescription(ServiceActivator.class, MissionControlServiceActivator.class);
		desc41.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(MissionControlServicePropertyTester.ID));
		extension41.addInterface(desc41);
		addExtension(extension41);
	}

	protected void createPropertyTesterExtensions1() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// tier 1
		// Identity Service Property Tester
		Extension extension11 = new Extension(typeId, IdentityServicePropertyTester.ID);
		InterfaceDescription desc11 = new InterfaceDescription(IPropertyTester.class, IdentityServicePropertyTester.class);
		extension11.addInterface(desc11);
		addExtension(extension11);

		// User Registry Service Property Tester
		Extension extension12 = new Extension(typeId, UserRegistryServicePropertyTester.ID);
		InterfaceDescription desc12 = new InterfaceDescription(IPropertyTester.class, UserRegistryServicePropertyTester.class);
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Auth Service Property Tester
		Extension extension13 = new Extension(typeId, AuthServicePropertyTester.ID);
		InterfaceDescription desc13 = new InterfaceDescription(IPropertyTester.class, AuthServicePropertyTester.class);
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Config Registry Service Property Tester
		Extension extension14 = new Extension(typeId, ConfigRegistryServicePropertyTester.ID);
		InterfaceDescription desc14 = new InterfaceDescription(IPropertyTester.class, ConfigRegistryServicePropertyTester.class);
		extension14.addInterface(desc14);
		addExtension(extension14);

		// tier 2
		// App Store Service Property Tester
		Extension extension21 = new Extension(typeId, AppStoreServicePropertyTester.ID);
		InterfaceDescription desc21 = new InterfaceDescription(IPropertyTester.class, AppStoreServicePropertyTester.class);
		extension21.addInterface(desc21);
		addExtension(extension21);

		// tier 3
		// Domain Management Service Property Tester
		Extension extension31 = new Extension(typeId, DomainManagementServicePropertyTester.ID);
		InterfaceDescription desc31 = new InterfaceDescription(IPropertyTester.class, DomainManagementServicePropertyTester.class);
		extension31.addInterface(desc31);
		addExtension(extension31);

		// Node Control Service Property Tester
		Extension extension32 = new Extension(typeId, NodeControlServicePropertyTester.ID);
		InterfaceDescription desc32 = new InterfaceDescription(IPropertyTester.class, NodeControlServicePropertyTester.class);
		extension32.addInterface(desc32);
		addExtension(extension32);

		// tier 4
		// Mission Control Service Property Tester
		Extension extension41 = new Extension(typeId, MissionControlServicePropertyTester.ID);
		InterfaceDescription desc41 = new InterfaceDescription(IPropertyTester.class, MissionControlServicePropertyTester.class);
		extension41.addInterface(desc41);
		addExtension(extension41);
	}

	protected void createServiceActivatorExtensions2() {
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// tier 1
		// Identity Reply
		Extension extension11 = new Extension(typeId, IdentityRelayActivator.ID, "Identity Reply", "Gateway for accessing distributed Identity services.");
		InterfaceDescription desc11 = new InterfaceDescription(ServiceActivator.class, IdentityRelayActivator.class);
		desc11.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(IdentityRelayPropertyTester.ID));
		extension11.addInterface(desc11);
		addExtension(extension11);

		// User Registry Relay
		Extension extension12 = new Extension(typeId, UserRegistryRelayActivator.ID, "User Registry Reply", "Gateway for accessing to distributed User Registry services.");
		InterfaceDescription desc12 = new InterfaceDescription(ServiceActivator.class, UserRegistryRelayActivator.class);
		desc12.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(UserRegistryRelayPropertyTester.ID));
		extension12.addInterface(desc12);
		addExtension(extension12);

		// Auth Relay
		Extension extension13 = new Extension(typeId, AuthRelayActivator.ID, "Authentication Reply", "Gateway for accessing to distributed Authentication services.");
		InterfaceDescription desc13 = new InterfaceDescription(ServiceActivator.class, AuthRelayActivator.class);
		desc13.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AuthRelayPropertyTester.ID));
		extension13.addInterface(desc13);
		addExtension(extension13);

		// Config Registry Relay
		Extension extension14 = new Extension(typeId, ConfigRegistryRelayActivator.ID, "Config Registry Reply", "Gateway for accessing to distributed Config Registry services.");
		InterfaceDescription desc14 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryRelayActivator.class);
		desc14.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryRelayPropertyTester.ID));
		extension14.addInterface(desc14);
		addExtension(extension14);

		// tier 2
		// App Store Relay
		Extension extension21 = new Extension(typeId, AppStoreRelayActivator.ID, "App Store Reply", "Gateway for accessing distributed App Store services.");
		InterfaceDescription desc21 = new InterfaceDescription(ServiceActivator.class, AppStoreRelayActivator.class);
		desc21.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AppStoreRelayPropertyTester.ID));
		extension21.addInterface(desc21);
		addExtension(extension21);

		// tier 3
		// Domain Management Relay
		Extension extension31 = new Extension(typeId, DomainManagementRelayActivator.ID, "Domain Management Reply", "Gateway for accessing distributed Domain Management services.");
		InterfaceDescription desc31 = new InterfaceDescription(ServiceActivator.class, DomainManagementRelayActivator.class);
		desc31.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DomainManagementRelayPropertyTester.ID));
		extension31.addInterface(desc31);
		addExtension(extension31);

		// Node Control Relay
		Extension extension32 = new Extension(typeId, NodeControlRelayActivator.ID, "Node Control Reply", "Gateway for accessing distributed Node Control services.");
		InterfaceDescription desc32 = new InterfaceDescription(ServiceActivator.class, NodeControlRelayActivator.class);
		desc32.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(NodeControlRelayPropertyTester.ID));
		extension32.addInterface(desc32);
		addExtension(extension32);

		// tier 4
		// Mission Control Relay
		Extension extension41 = new Extension(typeId, MissionControlRelayActivator.ID, "Mission Control Reply", "Gateway for accessing distributed Mission Control services.");
		InterfaceDescription desc41 = new InterfaceDescription(ServiceActivator.class, MissionControlRelayActivator.class);
		desc41.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(MissionControlRelayPropertyTester.ID));
		extension41.addInterface(desc41);
		addExtension(extension41);
	}

	protected void createPropertyTesterExtensions2() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// tier 1
		// Identity Relay Property Tester
		Extension extension11 = new Extension(typeId, IdentityRelayPropertyTester.ID);
		extension11.addInterface(IPropertyTester.class, IdentityRelayPropertyTester.class);
		addExtension(extension11);

		// User Registry Relay Property Tester
		Extension extension12 = new Extension(typeId, UserRegistryRelayPropertyTester.ID);
		extension12.addInterface(IPropertyTester.class, UserRegistryRelayPropertyTester.class);
		addExtension(extension12);

		// Auth Relay Property Tester
		Extension extension13 = new Extension(typeId, AuthRelayPropertyTester.ID);
		extension13.addInterface(IPropertyTester.class, AuthRelayPropertyTester.class);
		addExtension(extension13);

		// Config Registry Relay Property Tester
		Extension extension3 = new Extension(typeId, ConfigRegistryRelayPropertyTester.ID);
		extension3.addInterface(IPropertyTester.class, ConfigRegistryRelayPropertyTester.class);
		addExtension(extension3);

		// tier 2
		// App Store Relay Property Tester
		Extension extension21 = new Extension(typeId, AppStoreRelayPropertyTester.ID);
		extension21.addInterface(IPropertyTester.class, AppStoreRelayPropertyTester.class);
		addExtension(extension21);

		// tier 3
		// Domain Management Relay Property Tester
		Extension extension31 = new Extension(typeId, DomainManagementRelayPropertyTester.ID);
		extension31.addInterface(IPropertyTester.class, DomainManagementRelayPropertyTester.class);
		addExtension(extension31);

		// Node Control Relay Property Tester
		Extension extension32 = new Extension(typeId, NodeControlRelayPropertyTester.ID);
		extension32.addInterface(IPropertyTester.class, NodeControlRelayPropertyTester.class);
		addExtension(extension32);

		// tier 4
		// Mission Control Relay Property Tester
		Extension extension41 = new Extension(typeId, MissionControlRelayPropertyTester.ID);
		extension41.addInterface(IPropertyTester.class, MissionControlRelayPropertyTester.class);
		addExtension(extension41);
	}

	protected void createCommandExtensions() {
		String typeId = CommandActivator.EXTENSION_TYPE_ID;

		// Services command
		Extension extension1 = new Extension(typeId, ServicesRuntimeCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, ServicesRuntimeCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Domain Management command
		Extension extension2 = new Extension(typeId, DomainManagementRuntimeCommand.ID, "Domain Management Command", "Domain Management command description");
		InterfaceDescription desc2 = new InterfaceDescription(CommandActivator.class, DomainManagementRuntimeCommand.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Node Control command
		Extension extension3 = new Extension(typeId, NodeControlRuntimeCommand.ID, "Node Control Command", "Node Control command description");
		InterfaceDescription desc3 = new InterfaceDescription(CommandActivator.class, NodeControlRuntimeCommand.class);
		extension3.addInterface(desc3);
		addExtension(extension3);
	}

	protected void createIndexProvideExtensions() {
		String typeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		// tier 1
		Extension extension11 = new Extension(typeId, ComponentConstants.IDENTITY_INDEXER_ID, "Identity Service Index Provider");
		extension11.addInterface(factoryClass, IdentityServiceTimerFactory.class);
		addExtension(extension11);

		Extension extension12 = new Extension(typeId, ComponentConstants.USER_REGISTRY_INDEXER_ID, "User Account Service Index Provider");
		extension12.addInterface(factoryClass, UserRegistryServiceIndexTimerFactory.class);
		addExtension(extension12);

		// Extension extension13 = new Extension(typeId, ComponentConstants.AUTH_INDEXER_ID, "Auth Service Index Provider");
		// extension13.addInterface(factoryClass, AuthServiceIndexTimerFactory.class);
		// addExtension(extension13);

		// Extension extension14 = new Extension(typeId, ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, "Config Registry Service Index Provider");
		// extension14.addInterface(factoryClass, ConfigRegistryServiceIndexTimerFactory.class);
		// addExtension(extension14);

		// Extension extension15 = new Extension(typeId, ComponentConstants.OAUTH2_INDEXER_ID, "OAuth2 Service Index Provider");
		// extension15.addInterface(factoryClass, OAuth2ServiceIndexTimerFactory.class);
		// addExtension(extension15);

		// tier 2
		Extension extension21 = new Extension(typeId, ComponentConstants.APP_STORE_INDEXER_ID, "App Store Service Index Provider");
		extension21.addInterface(factoryClass, AppStoreServiceIndexTimerFactory.class);
		addExtension(extension21);

		// tier 3
		Extension extension31 = new Extension(typeId, ComponentConstants.DOMAIN_SERVICE_INDEXER_ID, "Domain Service Index Provider");
		extension31.addInterface(factoryClass, DomainServiceTimerFactory.class);
		addExtension(extension31);

		Extension extension32 = new Extension(typeId, ComponentConstants.NODE_CONTROL_INDEXER_ID, "Node Service Index Provider");
		extension32.addInterface(factoryClass, NodeControlServiceTimerFactory.class);
		addExtension(extension32);

		// tier 4
		// Extension extension41 = new Extension(typeId, ComponentConstants.MISSION_CONTROL_INDEXER_ID, "Mission Control Service Index Provider");
		// extension41.addInterface(factoryClass, MissionControlIndexTimeractory.class);
		// addExtension(extension41);
	}

	protected void createNodeControlEditPolicyCommandExtensions() {
		String extensionTypeId = WSCommand.EXTENSION_TYPE_ID;
		String serviceName = ComponentConstants.NODE_CONTROL__SERVICE_NAME;

		Extension extension1 = new Extension(extensionTypeId, NodeListWSCommand.ID);
		extension1.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc1 = new InterfaceDescription(WSCommand.class, NodeListWSCommand.class);
		desc1.setSingleton(false);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, NodeGetWSCommand.ID);
		extension2.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc2 = new InterfaceDescription(WSCommand.class, NodeGetWSCommand.class);
		desc2.setSingleton(false);
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(extensionTypeId, NodeExistWSCommand.ID);
		extension3.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc3 = new InterfaceDescription(WSCommand.class, NodeExistWSCommand.class);
		desc3.setSingleton(false);
		extension3.addInterface(desc3);
		addExtension(extension3);

		Extension extension4 = new Extension(extensionTypeId, NodeCreateWSCommand.ID);
		extension4.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc4 = new InterfaceDescription(WSCommand.class, NodeCreateWSCommand.class);
		desc4.setSingleton(false);
		extension4.addInterface(desc4);
		addExtension(extension4);

		Extension extension5 = new Extension(extensionTypeId, NodeUpdateWSCommand.ID);
		extension5.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc5 = new InterfaceDescription(WSCommand.class, NodeUpdateWSCommand.class);
		desc5.setSingleton(false);
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(extensionTypeId, NodeDeleteWSCommand.ID);
		extension6.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc6 = new InterfaceDescription(WSCommand.class, NodeDeleteWSCommand.class);
		desc6.setSingleton(false);
		extension6.addInterface(desc6);
		addExtension(extension6);

		Extension extension7 = new Extension(extensionTypeId, NodeStartWSCommand.ID);
		extension7.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc7 = new InterfaceDescription(WSCommand.class, NodeStartWSCommand.class);
		desc7.setSingleton(false);
		extension7.addInterface(desc7);
		addExtension(extension7);

		Extension extension8 = new Extension(extensionTypeId, NodeStopWSCommand.ID);
		extension8.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc8 = new InterfaceDescription(WSCommand.class, NodeStopWSCommand.class);
		desc8.setSingleton(false);
		extension8.addInterface(desc8);
		addExtension(extension8);

		Extension extension9 = new Extension(extensionTypeId, NodeStatusWSCommand.ID);
		extension9.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc9 = new InterfaceDescription(WSCommand.class, NodeStatusWSCommand.class);
		desc9.setSingleton(false);
		extension9.addInterface(desc9);
		addExtension(extension9);

		Extension extension10 = new Extension(extensionTypeId, NodeAttributeAddWSCommand.ID);
		extension10.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc10 = new InterfaceDescription(WSCommand.class, NodeAttributeAddWSCommand.class);
		desc10.setSingleton(false);
		extension10.addInterface(desc10);
		addExtension(extension10);

		Extension extension11 = new Extension(extensionTypeId, NodeAttributeUpdateWSCommand.ID);
		extension11.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc11 = new InterfaceDescription(WSCommand.class, NodeAttributeUpdateWSCommand.class);
		desc11.setSingleton(false);
		extension11.addInterface(desc11);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, NodeAttributeDeleteWSCommand.ID);
		extension12.setProperty(WSCommand.PROP__SERVICE_NAME, serviceName);
		InterfaceDescription desc12 = new InterfaceDescription(WSCommand.class, NodeAttributeDeleteWSCommand.class);
		desc12.setSingleton(false);
		extension12.addInterface(desc12);
		addExtension(extension12);
	}

	protected void createPropertiesRegulatorExtensions() {
		String typeId = PropertiesRegulator.EXTENSION_TYPE_ID;

		// Node config.ini properties regulator
		Extension extension1 = new Extension(typeId, NodeConfigIniPropertiesRegulator.ID, "Node config.ini properties regulator");
		extension1.setProperty(PropertiesRegulator.PROP__TYPE, PropertiesRegulator.TYPE__NODE__CONFIG_INI);
		InterfaceDescription desc1 = new InterfaceDescription(PropertiesRegulator.class, NodeConfigIniPropertiesRegulator.class);
		desc1.setSingleton(true);
		extension1.addInterface(desc1);
		addExtension(extension1);
	}

}

// extension id constants for WS relay
// public static final String USER_REGISTRY_RELAY_EXTENSION_ID = "component.user_registry.relay";
// public static final String AUTH_RELAY_EXTENSION_ID = "component.auth.relay";
// public static final String CONFIG_REGISTRY_RELAY_EXTENSION_ID = "component.config_registry.relay";
// public static final String APP_STORE_RELAY_EXTENSION_ID = "component.app_store.relay";
// public static final String DOMAIN_SERVICE_RELAY_EXTENSION_ID = "component.domain_service.relay";
// public static final String NODE_CONTROL_RELAY_EXTENSION_ID = "component.node_control.relay";
// public static final String MISSION_CONTROL_RELAY_EXTENSION_ID = "component.mission_control.relay";

// protected void createWSRelayExtensions() {
// String extensionTypeId = WSRelayControl.EXTENSION_TYPE_ID;
//
// // tier 1
// ProgramExtension userRegistryRelayExtension = new ProgramExtension(extensionTypeId, USER_REGISTRY_RELAY_EXTENSION_ID, "User registration relay", "User
// registration relay description");
// userRegistryRelayExtension.addInterface(WSRelayControl.class, UserRegistryRelayControl.class.getName());
// addExtension(userRegistryRelayExtension);
//
// ProgramExtension authRelayExtension = new ProgramExtension(extensionTypeId, AUTH_RELAY_EXTENSION_ID, "Auth relay", "Auth relay description");
// authRelayExtension.addInterface(WSRelayControl.class, AuthRelayControl.class.getName());
// addExtension(authRelayExtension);
//
// ProgramExtension configRegistryRelayExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_RELAY_EXTENSION_ID, "Config registration relay",
// "Config registration relay description");
// configRegistryRelayExtension.addInterface(WSRelayControl.class, ConfigRegistryRelayControl.class.getName());
// addExtension(configRegistryRelayExtension);
//
// // tier 2
// ProgramExtension appStoreRelayExtension = new ProgramExtension(extensionTypeId, APP_STORE_RELAY_EXTENSION_ID, "App store relay", "App store relay
// description");
// appStoreRelayExtension.addInterface(WSRelayControl.class, AppStoreRelayControl.class.getName());
// addExtension(appStoreRelayExtension);
//
// // tier 3
// ProgramExtension domainServiceRelayExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_RELAY_EXTENSION_ID, "Domain service relay", "Domain
// service relay description");
// domainServiceRelayExtension.addInterface(WSRelayControl.class, DomainServiceWSRelayControl.class.getName());
// addExtension(domainServiceRelayExtension);
//
// ProgramExtension nodeControlRelayExtension = new ProgramExtension(extensionTypeId, NODE_CONTROL_RELAY_EXTENSION_ID, "Node control relay", "Node control
// relay description");
// nodeControlRelayExtension.addInterface(WSRelayControl.class, NodeControlWSRelayControl.class.getName());
// addExtension(nodeControlRelayExtension);
//
// // tier 4
// ProgramExtension missionControlRelayExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_RELAY_EXTENSION_ID, "Mission control relay",
// "Mission control relay description");
// missionControlRelayExtension.addInterface(WSRelayControl.class, MissionControlWSRelayControl.class.getName());
// addExtension(missionControlRelayExtension);
// }
