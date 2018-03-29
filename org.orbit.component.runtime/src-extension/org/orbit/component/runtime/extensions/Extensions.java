/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.extensions.appstore.AppStoreRelayActivator;
import org.orbit.component.runtime.extensions.appstore.AppStoreRelayPropertyTester;
import org.orbit.component.runtime.extensions.appstore.AppStoreServiceActivator;
import org.orbit.component.runtime.extensions.appstore.AppStoreServicePropertyTester;
import org.orbit.component.runtime.extensions.auth.AuthRelayActivator;
import org.orbit.component.runtime.extensions.auth.AuthRelayPropertyTester;
import org.orbit.component.runtime.extensions.auth.AuthServiceActivator;
import org.orbit.component.runtime.extensions.auth.AuthServicePropertyTester;
import org.orbit.component.runtime.extensions.configregistry.ConfigRegistryRelayActivator;
import org.orbit.component.runtime.extensions.configregistry.ConfigRegistryRelayPropertyTester;
import org.orbit.component.runtime.extensions.configregistry.ConfigRegistryServiceActivator;
import org.orbit.component.runtime.extensions.configregistry.ConfigRegistryServicePropertyTester;
import org.orbit.component.runtime.extensions.domainmanagement.DomainManagementRelayActivator;
import org.orbit.component.runtime.extensions.domainmanagement.DomainManagementRelayPropertyTester;
import org.orbit.component.runtime.extensions.domainmanagement.DomainManagementServiceActivator;
import org.orbit.component.runtime.extensions.domainmanagement.DomainManagementServicePropertyTester;
import org.orbit.component.runtime.extensions.missioncontrol.MissionControlRelayActivator;
import org.orbit.component.runtime.extensions.missioncontrol.MissionControlRelayPropertyTester;
import org.orbit.component.runtime.extensions.missioncontrol.MissionControlServiceActivator;
import org.orbit.component.runtime.extensions.missioncontrol.MissionControlServicePropertyTester;
import org.orbit.component.runtime.extensions.nodecontrol.NodeControlRelayActivator;
import org.orbit.component.runtime.extensions.nodecontrol.NodeControlRelayPropertyTester;
import org.orbit.component.runtime.extensions.nodecontrol.NodeControlServiceActivator;
import org.orbit.component.runtime.extensions.nodecontrol.NodeControlServicePropertyTester;
import org.orbit.component.runtime.extensions.userregistry.UserRegistryRelayActivator;
import org.orbit.component.runtime.extensions.userregistry.UserRegistryRelayPropertyTester;
import org.orbit.component.runtime.extensions.userregistry.UserRegistryServiceActivator;
import org.orbit.component.runtime.extensions.userregistry.UserRegistryServicePropertyTester;
import org.orbit.platform.sdk.extensions.ServiceActivator;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;

/**
 * Component extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.component.runtime");
	}

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions1();
		createServiceActivatorExtensions2();
		createPropertyTesterExtensions1();
		createPropertyTesterExtensions2();
	}

	protected void createServiceActivatorExtensions1() {
		String typeId = ServiceActivator.TYPE_ID;

		// tier 1
		Extension extension1 = new Extension(typeId, UserRegistryServiceActivator.ID, "User registration service activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, UserRegistryServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), UserRegistryServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(typeId, AuthServiceActivator.ID, "Auth service activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, AuthServiceActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), AuthServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		Extension extension3 = new Extension(typeId, ConfigRegistryServiceActivator.ID, "Config registration service activator");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryServiceActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), ConfigRegistryServicePropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier 2
		Extension extension4 = new Extension(typeId, AppStoreServiceActivator.ID, "App store service activator");
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, AppStoreServiceActivator.class);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), AppStoreServicePropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier 3
		Extension extension5 = new Extension(typeId, DomainManagementServiceActivator.ID, "Domain management service activator");
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, DomainManagementServiceActivator.class);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), DomainManagementServicePropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		Extension extension6 = new Extension(typeId, NodeControlServiceActivator.ID, "Node management service activator");
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, NodeControlServiceActivator.class);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), NodeControlServicePropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);

		// tier 4
		Extension extension7 = new Extension(typeId, MissionControlServiceActivator.ID, "Mission control service activator");
		InterfaceDescription desc7 = new InterfaceDescription(ServiceActivator.class, MissionControlServiceActivator.class);
		desc7.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), MissionControlServicePropertyTester.ID));
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createPropertyTesterExtensions1() {
		String typeId = IPropertyTester.TYPE_ID;

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
		String typeId = ServiceActivator.TYPE_ID;

		// User Registry Relay
		Extension extension1 = new Extension(typeId, UserRegistryRelayActivator.ID);
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, UserRegistryRelayActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), UserRegistryRelayPropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth Relay
		Extension extension2 = new Extension(typeId, AuthRelayActivator.ID);
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, AuthRelayActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), AuthRelayPropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config Registry Relay
		Extension extension3 = new Extension(typeId, ConfigRegistryRelayActivator.ID);
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryRelayActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), ConfigRegistryRelayPropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// App Store Relay
		Extension extension4 = new Extension(typeId, AppStoreRelayActivator.ID);
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, AppStoreRelayActivator.class);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), AppStoreRelayPropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Domain Management Relay
		Extension extension5 = new Extension(typeId, DomainManagementRelayActivator.ID);
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, DomainManagementRelayActivator.class);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), DomainManagementRelayPropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Management Relay
		Extension extension6 = new Extension(typeId, NodeControlRelayActivator.ID);
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, NodeControlRelayActivator.class);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), NodeControlRelayPropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Mission Control Relay
		Extension extension7 = new Extension(typeId, MissionControlRelayActivator.ID);
		InterfaceDescription desc7 = new InterfaceDescription(ServiceActivator.class, MissionControlRelayActivator.class);
		desc7.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(getRealm(), MissionControlRelayPropertyTester.ID));
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createPropertyTesterExtensions2() {
		String typeId = IPropertyTester.TYPE_ID;

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
