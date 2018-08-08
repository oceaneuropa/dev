/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime;

import org.orbit.component.runtime.cli.DomainManagementCommand;
import org.orbit.component.runtime.cli.NodeControlCommand;
import org.orbit.component.runtime.cli.ServicesCommand;
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
import org.orbit.component.runtime.extension.configregistry.ConfigRegistryServiceActivator;
import org.orbit.component.runtime.extension.configregistry.ConfigRegistryServicePropertyTester;
import org.orbit.component.runtime.extension.domain.DomainManagementRelayActivator;
import org.orbit.component.runtime.extension.domain.DomainManagementRelayPropertyTester;
import org.orbit.component.runtime.extension.domain.DomainManagementServiceActivator;
import org.orbit.component.runtime.extension.domain.DomainManagementServicePropertyTester;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlRelayActivator;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlRelayPropertyTester;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlServiceActivator;
import org.orbit.component.runtime.extension.missioncontrol.MissionControlServicePropertyTester;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlRelayActivator;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlRelayPropertyTester;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlServiceActivator;
import org.orbit.component.runtime.extension.nodecontrol.NodeControlServicePropertyTester;
import org.orbit.component.runtime.extension.userregistry.UserRegistryRelayActivator;
import org.orbit.component.runtime.extension.userregistry.UserRegistryRelayPropertyTester;
import org.orbit.component.runtime.extension.userregistry.UserRegistryServiceActivator;
import org.orbit.component.runtime.extension.userregistry.UserRegistryServicePropertyTester;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryServiceIndexTimerFactory;
import org.orbit.component.runtime.tier1.auth.ws.AuthServiceIndexTimerFactory;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryServiceIndexTimerFactory;
import org.orbit.component.runtime.tier1.session.ws.OAuth2ServiceIndexTimerFactory;
import org.orbit.component.runtime.tier2.appstore.ws.AppStoreServiceIndexTimerFactory;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceTimerFactory;
import org.orbit.component.runtime.tier3.nodecontrol.ws.NodeControlServiceTimerFactory;
import org.orbit.component.runtime.tier4.missioncontrol.ws.MissionControlIndexTimeractory;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.component.runtime");
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
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// tier 1
		Extension extension1 = new Extension(typeId, UserRegistryServiceActivator.ID, "User registration service activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, UserRegistryServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(UserRegistryServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(typeId, AuthServiceActivator.ID, "Auth service activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, AuthServiceActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AuthServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(typeId, ConfigRegistryServiceActivator.ID, "Config registration service activator");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryServiceActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryServicePropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier 2
		Extension extension4 = new Extension(typeId, AppStoreServiceActivator.ID, "App store service activator");
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, AppStoreServiceActivator.class);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AppStoreServicePropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier 3
		Extension extension5 = new Extension(typeId, DomainManagementServiceActivator.ID, "Domain management service activator");
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, DomainManagementServiceActivator.class);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DomainManagementServicePropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(typeId, NodeControlServiceActivator.ID, "Node management service activator");
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, NodeControlServiceActivator.class);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(NodeControlServicePropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);

		// tier 4
		Extension extension7 = new Extension(typeId, MissionControlServiceActivator.ID, "Mission control service activator");
		InterfaceDescription desc7 = new InterfaceDescription(ServiceActivator.class, MissionControlServiceActivator.class);
		desc7.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(MissionControlServicePropertyTester.ID));
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createPropertyTesterExtensions1() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// User Registry Service Property Tester
		Extension extension1 = new Extension(typeId, UserRegistryServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, UserRegistryServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth Service Property Tester
		Extension extension2 = new Extension(typeId, AuthServicePropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, AuthServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config Registry Service Property Tester
		Extension extension3 = new Extension(typeId, ConfigRegistryServicePropertyTester.ID);
		InterfaceDescription desc3 = new InterfaceDescription(IPropertyTester.class, ConfigRegistryServicePropertyTester.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// App Store Service Property Tester
		Extension extension4 = new Extension(typeId, AppStoreServicePropertyTester.ID);
		InterfaceDescription desc4 = new InterfaceDescription(IPropertyTester.class, AppStoreServicePropertyTester.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Domain Management Service Property Tester
		Extension extension5 = new Extension(typeId, DomainManagementServicePropertyTester.ID);
		InterfaceDescription desc5 = new InterfaceDescription(IPropertyTester.class, DomainManagementServicePropertyTester.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Control Service Property Tester
		Extension extension6 = new Extension(typeId, NodeControlServicePropertyTester.ID);
		InterfaceDescription desc6 = new InterfaceDescription(IPropertyTester.class, NodeControlServicePropertyTester.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Mission Control Service Property Tester
		Extension extension7 = new Extension(typeId, MissionControlServicePropertyTester.ID);
		InterfaceDescription desc7 = new InterfaceDescription(IPropertyTester.class, MissionControlServicePropertyTester.class);
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createServiceActivatorExtensions2() {
		String typeId = ServiceActivator.EXTENSION_TYPE_ID;

		// User Registry Relay
		Extension extension1 = new Extension(typeId, UserRegistryRelayActivator.ID);
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, UserRegistryRelayActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(UserRegistryRelayPropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth Relay
		Extension extension2 = new Extension(typeId, AuthRelayActivator.ID);
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, AuthRelayActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AuthRelayPropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config Registry Relay
		Extension extension3 = new Extension(typeId, ConfigRegistryRelayActivator.ID);
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryRelayActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryRelayPropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// App Store Relay
		Extension extension4 = new Extension(typeId, AppStoreRelayActivator.ID);
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, AppStoreRelayActivator.class);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AppStoreRelayPropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Domain Management Relay
		Extension extension5 = new Extension(typeId, DomainManagementRelayActivator.ID);
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, DomainManagementRelayActivator.class);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DomainManagementRelayPropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Management Relay
		Extension extension6 = new Extension(typeId, NodeControlRelayActivator.ID);
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, NodeControlRelayActivator.class);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(NodeControlRelayPropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Mission Control Relay
		Extension extension7 = new Extension(typeId, MissionControlRelayActivator.ID);
		InterfaceDescription desc7 = new InterfaceDescription(ServiceActivator.class, MissionControlRelayActivator.class);
		desc7.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(MissionControlRelayPropertyTester.ID));
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createPropertyTesterExtensions2() {
		String typeId = IPropertyTester.EXTENSION_TYPE_ID;

		// User Registry Relay Property Tester
		Extension extension1 = new Extension(typeId, UserRegistryRelayPropertyTester.ID);
		extension1.addInterface(IPropertyTester.class, UserRegistryRelayPropertyTester.class);
		addExtension(extension1);

		// Auth Relay Property Tester
		Extension extension2 = new Extension(typeId, AuthRelayPropertyTester.ID);
		extension2.addInterface(IPropertyTester.class, AuthRelayPropertyTester.class);
		addExtension(extension2);

		// Config Registry Relay Property Tester
		Extension extension3 = new Extension(typeId, ConfigRegistryRelayPropertyTester.ID);
		extension3.addInterface(IPropertyTester.class, ConfigRegistryRelayPropertyTester.class);
		addExtension(extension3);

		// App Store Relay Property Tester
		Extension extension4 = new Extension(typeId, AppStoreRelayPropertyTester.ID);
		extension4.addInterface(IPropertyTester.class, AppStoreRelayPropertyTester.class);
		addExtension(extension4);

		// Domain Management Relay Property Tester
		Extension extension5 = new Extension(typeId, DomainManagementRelayPropertyTester.ID);
		extension5.addInterface(IPropertyTester.class, DomainManagementRelayPropertyTester.class);
		addExtension(extension5);

		// Node Management Relay Property Tester
		Extension extension6 = new Extension(typeId, NodeControlRelayPropertyTester.ID);
		extension6.addInterface(IPropertyTester.class, NodeControlRelayPropertyTester.class);
		addExtension(extension6);

		// Mission Control Relay Property Tester
		Extension extension7 = new Extension(typeId, MissionControlRelayPropertyTester.ID);
		extension7.addInterface(IPropertyTester.class, MissionControlRelayPropertyTester.class);
		addExtension(extension7);
	}

	protected void createCommandExtensions() {
		String typeId = CommandActivator.EXTENSION_TYPE_ID;

		// Services command
		Extension extension1 = new Extension(typeId, ServicesCommand.ID, "Services Command", "Services command description");
		InterfaceDescription desc1 = new InterfaceDescription(CommandActivator.class, ServicesCommand.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Domain Management command
		Extension extension2 = new Extension(typeId, DomainManagementCommand.ID, "Domain Management Command", "Domain Management command description");
		InterfaceDescription desc2 = new InterfaceDescription(CommandActivator.class, DomainManagementCommand.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Node Control command
		Extension extension3 = new Extension(typeId, NodeControlCommand.ID, "Node Control Command", "Node Control command description");
		InterfaceDescription desc3 = new InterfaceDescription(CommandActivator.class, NodeControlCommand.class);
		extension3.addInterface(desc3);
		addExtension(extension3);
	}

	protected void createIndexProvideExtensions() {
		String typeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension11 = new Extension(typeId, OrbitConstants.USER_REGISTRY_INDEXER_ID, "User Account Service Index Provider");
		extension11.addInterface(factoryClass, UserRegistryServiceIndexTimerFactory.class);
		addExtension(extension11);

		Extension extension12 = new Extension(typeId, OrbitConstants.AUTH_INDEXER_ID, "Auth Service Index Provider");
		extension12.addInterface(factoryClass, AuthServiceIndexTimerFactory.class);
		addExtension(extension12);

		Extension extension13 = new Extension(typeId, OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, "Config Registry Service Index Provider");
		extension13.addInterface(factoryClass, ConfigRegistryServiceIndexTimerFactory.class);
		addExtension(extension13);

		Extension extension14 = new Extension(typeId, OrbitConstants.OAUTH2_INDEXER_ID, "OAuth2 Service Index Provider");
		extension14.addInterface(factoryClass, OAuth2ServiceIndexTimerFactory.class);
		addExtension(extension14);

		Extension extension21 = new Extension(typeId, OrbitConstants.APP_STORE_INDEXER_ID, "App Store Service Index Provider");
		extension21.addInterface(factoryClass, AppStoreServiceIndexTimerFactory.class);
		addExtension(extension21);

		Extension extension31 = new Extension(typeId, OrbitConstants.DOMAIN_SERVICE_INDEXER_ID, "Domain Management Service Index Provider");
		extension31.addInterface(factoryClass, DomainServiceTimerFactory.class);
		addExtension(extension31);

		Extension extension32 = new Extension(typeId, OrbitConstants.NODE_CONTROL_INDEXER_ID, "Node Control Service Index Provider");
		extension32.addInterface(factoryClass, NodeControlServiceTimerFactory.class);
		addExtension(extension32);

		Extension extension33 = new Extension(typeId, OrbitConstants.MISSION_CONTROL_INDEXER_ID, "Mission Control Service Index Provider");
		extension33.addInterface(factoryClass, MissionControlIndexTimeractory.class);
		addExtension(extension33);
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
