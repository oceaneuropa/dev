/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime;

import org.orbit.component.runtime.extensions.serviceactivator.AppStoreServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.AuthServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.ConfigRegistryServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.DomainManagementServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.MissionControlServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.NodeManagementServiceActivator;
import org.orbit.component.runtime.extensions.serviceactivator.UserRegistryServiceActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.orbit.platform.sdk.extension.desc.ProgramExtensions;

/**
 * Component extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	// extension id constants for URL providers
	public static final String USER_REGISTRY_URL_PROVIDER_EXTENSION_ID = "component.user_registry.url_provider";
	public static final String AUTH_URL_PROVIDER_EXTENSION_ID = "component.auth.url_provider";
	public static final String CONFIG_REGISTRY_URL_PROVIDER_EXTENSION_ID = "component.config_registry.url_provider";
	public static final String APP_STORE_URL_PROVIDER_EXTENSION_ID = "component.app_store.url_provider";
	public static final String DOMAIN_SERVICE_URL_PROVIDER_EXTENSION_ID = "component.domain_service.url_provider";
	public static final String NODE_CONTROL_URL_PROVIDER_EXTENSION_ID = "component.node_control.url_provider";
	public static final String MISSION_CONTROL_URL_PROVIDER_EXTENSION_ID = "component.mission_control.url_provider";

	@Override
	public void createExtensions() {
		createServiceCommandExtensions();
		createServiceActivatorExtensions();
	}

	protected void createServiceCommandExtensions() {
		// String extensionTypeId = ServiceCommand.EXTENSION_TYPE_ID;
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		// tier 1
		ProgramExtension userRegistryActivatorExtension = new ProgramExtension(typeId, UserRegistryServiceActivator.ID, "User registration service activator", "User registration service activator description");
		userRegistryActivatorExtension.addInterface(ServiceActivator.class, UserRegistryServiceActivator.class.getName());
		addExtension(userRegistryActivatorExtension);

		ProgramExtension authActivatorExtension = new ProgramExtension(typeId, AuthServiceActivator.ID, "Auth service activator", "Auth service activator description");
		authActivatorExtension.addInterface(ServiceActivator.class, AuthServiceActivator.class.getName());
		addExtension(authActivatorExtension);

		ProgramExtension configRegistryActivatorExtension = new ProgramExtension(typeId, ConfigRegistryServiceActivator.ID, "Config registration service activator", "Config registration service activator description");
		configRegistryActivatorExtension.addInterface(ServiceActivator.class, ConfigRegistryServiceActivator.class.getName());
		addExtension(configRegistryActivatorExtension);

		// tier 2
		ProgramExtension appStoreActivatorExtension = new ProgramExtension(typeId, AppStoreServiceActivator.ID, "App store service activator", "App store service activator description");
		appStoreActivatorExtension.addInterface(ServiceActivator.class, AppStoreServiceActivator.class.getName());
		addExtension(appStoreActivatorExtension);

		// tier 3
		ProgramExtension domainManagementActivatorExtension = new ProgramExtension(typeId, DomainManagementServiceActivator.ID, "Domain management service activator", "Domain management service activator description");
		domainManagementActivatorExtension.addInterface(ServiceActivator.class, DomainManagementServiceActivator.class.getName());
		addExtension(domainManagementActivatorExtension);

		ProgramExtension nodeManagementActivatorExtension = new ProgramExtension(typeId, NodeManagementServiceActivator.ID, "Node management service activator", "Node management service activator description");
		nodeManagementActivatorExtension.addInterface(ServiceActivator.class, NodeManagementServiceActivator.class.getName());
		addExtension(nodeManagementActivatorExtension);

		// tier 4
		ProgramExtension missionControlActivatorExtension = new ProgramExtension(typeId, MissionControlServiceActivator.ID, "Mission control service activator", "Mission control service activator description");
		missionControlActivatorExtension.addInterface(ServiceActivator.class, MissionControlServiceActivator.class.getName());
		addExtension(missionControlActivatorExtension);
	}

}
