/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime;

import org.orbit.component.runtime.extensions.propertytester.AppStoreServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.AuthServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.ConfigRegistryServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.DomainManagementServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.MissionControlServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.NodeManagementServicePropertyTester;
import org.orbit.component.runtime.extensions.propertytester.UserRegistryServicePropertyTester;
import org.orbit.component.runtime.extensions.serviceactivator.AppStoreServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.AuthServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.ConfigRegistryServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.DomainManagementServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.MissionControlServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.NodeManagementServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.UserRegistryServiceActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.condition.ConditionFactory;
import org.orbit.platform.sdk.extension.IPropertyTester;
import org.orbit.platform.sdk.extension.desc.InterfaceDescription;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.orbit.platform.sdk.extension.desc.ProgramExtensions;

/**
 * Component extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	@Override
	public void createExtensions() {
		createPropertyTesterExtensions1();
		createPropertyTesterExtensions2();

		createServiceActivatorExtensions1();
		createServiceActivatorExtensions2();
	}

	protected void createPropertyTesterExtensions1() {
		String typeId = IPropertyTester.TYPE_ID;

		// User Registry Service Property Tester
		ProgramExtension extension1 = new ProgramExtension(typeId, UserRegistryServicePropertyTester.ID, "User Registry Service Property Tester");
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, UserRegistryServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		// Auth Service Property Tester
		ProgramExtension extension2 = new ProgramExtension(typeId, AuthServicePropertyTester.ID, "Auth Service Property Tester");
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, AuthServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);

		// Config Registry Service Property Tester
		ProgramExtension extension3 = new ProgramExtension(typeId, ConfigRegistryServicePropertyTester.ID, "Config Registry Service Property Tester");
		InterfaceDescription desc3 = new InterfaceDescription(IPropertyTester.class, ConfigRegistryServicePropertyTester.class);
		extension3.addInterface(desc3);
		addExtension(extension3);

		// App Store Service Property Tester
		ProgramExtension extension4 = new ProgramExtension(typeId, AppStoreServicePropertyTester.ID, "App Store Service Property Tester");
		InterfaceDescription desc4 = new InterfaceDescription(IPropertyTester.class, AppStoreServicePropertyTester.class);
		extension4.addInterface(desc4);
		addExtension(extension4);

		// Domain Management Service Property Tester
		ProgramExtension extension5 = new ProgramExtension(typeId, DomainManagementServicePropertyTester.ID, "Domain Management Service Property Tester");
		InterfaceDescription desc5 = new InterfaceDescription(IPropertyTester.class, DomainManagementServicePropertyTester.class);
		extension5.addInterface(desc5);
		addExtension(extension5);

		// Node Management Service Property Tester
		ProgramExtension extension6 = new ProgramExtension(typeId, NodeManagementServicePropertyTester.ID, "Node Management Service Property Tester");
		InterfaceDescription desc6 = new InterfaceDescription(IPropertyTester.class, NodeManagementServicePropertyTester.class);
		extension6.addInterface(desc6);
		addExtension(extension6);

		// Mission Control Service Property Tester
		ProgramExtension extension7 = new ProgramExtension(typeId, MissionControlServicePropertyTester.ID, "Mission Control Service Property Tester");
		InterfaceDescription desc7 = new InterfaceDescription(IPropertyTester.class, MissionControlServicePropertyTester.class);
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createPropertyTesterExtensions2() {

	}

	protected void createServiceActivatorExtensions1() {
		String typeId = ServiceActivator.TYPE_ID;

		// tier 1
		ProgramExtension extension1 = new ProgramExtension(typeId, UserRegistryServiceActivator.ID, "User registration service activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, UserRegistryServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(UserRegistryServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		ProgramExtension extension2 = new ProgramExtension(typeId, AuthServiceActivator.ID, "Auth service activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, AuthServiceActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AuthServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);

		ProgramExtension extension3 = new ProgramExtension(typeId, ConfigRegistryServiceActivator.ID, "Config registration service activator");
		InterfaceDescription desc3 = new InterfaceDescription(ServiceActivator.class, ConfigRegistryServiceActivator.class);
		desc3.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(ConfigRegistryServicePropertyTester.ID));
		extension3.addInterface(desc3);
		addExtension(extension3);

		// tier 2
		ProgramExtension extension4 = new ProgramExtension(typeId, AppStoreServiceActivator.ID, "App store service activator");
		InterfaceDescription desc4 = new InterfaceDescription(ServiceActivator.class, AppStoreServiceActivator.class);
		desc4.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(AppStoreServicePropertyTester.ID));
		extension4.addInterface(desc4);
		addExtension(extension4);

		// tier 3
		ProgramExtension extension5 = new ProgramExtension(typeId, DomainManagementServiceActivator.ID, "Domain management service activator");
		InterfaceDescription desc5 = new InterfaceDescription(ServiceActivator.class, DomainManagementServiceActivator.class);
		desc5.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(DomainManagementServicePropertyTester.ID));
		extension5.addInterface(desc5);
		addExtension(extension5);

		ProgramExtension extension6 = new ProgramExtension(typeId, NodeManagementServiceActivator.ID, "Node management service activator");
		InterfaceDescription desc6 = new InterfaceDescription(ServiceActivator.class, NodeManagementServiceActivator.class);
		desc6.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(NodeManagementServicePropertyTester.ID));
		extension6.addInterface(desc6);
		addExtension(extension6);

		// tier 4
		ProgramExtension extension7 = new ProgramExtension(typeId, MissionControlServiceActivator.ID, "Mission control service activator");
		InterfaceDescription desc7 = new InterfaceDescription(ServiceActivator.class, MissionControlServiceActivator.class);
		desc7.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(MissionControlServicePropertyTester.ID));
		extension7.addInterface(desc7);
		addExtension(extension7);
	}

	protected void createServiceActivatorExtensions2() {

	}

}
