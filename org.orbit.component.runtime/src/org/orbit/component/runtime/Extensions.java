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
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensions;

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
		ProgramExtension userRegistryActivatorExtension = new ProgramExtension(typeId, UserRegistryServiceActivator.ID);
		userRegistryActivatorExtension.setName("User registration service activator");
		userRegistryActivatorExtension.setDescription("User registration service activator description");
		userRegistryActivatorExtension.adapt(ServiceActivator.class, UserRegistryServiceActivator.INSTANCE);
		addExtension(userRegistryActivatorExtension);

		ProgramExtension authActivatorExtension = new ProgramExtension(typeId, AuthServiceActivator.ID);
		authActivatorExtension.setName("Auth service activator");
		authActivatorExtension.setDescription("Auth service activator description");
		authActivatorExtension.adapt(ServiceActivator.class, AuthServiceActivator.INSTANCE);
		addExtension(authActivatorExtension);

		ProgramExtension configRegistryActivatorExtension = new ProgramExtension(typeId, ConfigRegistryServiceActivator.ID);
		configRegistryActivatorExtension.setName("Config registration service activator");
		configRegistryActivatorExtension.setDescription("Config registration service activator description");
		configRegistryActivatorExtension.adapt(ServiceActivator.class, ConfigRegistryServiceActivator.INSTANCE);
		addExtension(configRegistryActivatorExtension);

		// tier 2
		ProgramExtension appStoreActivatorExtension = new ProgramExtension(typeId, AppStoreServiceActivator.ID);
		appStoreActivatorExtension.setName("App store service activator");
		appStoreActivatorExtension.setDescription("App store service activator description");
		appStoreActivatorExtension.adapt(ServiceActivator.class, AppStoreServiceActivator.INSTANCE);
		addExtension(appStoreActivatorExtension);

		// tier 3
		ProgramExtension domainManagementActivatorExtension = new ProgramExtension(typeId, DomainManagementServiceActivator.ID);
		domainManagementActivatorExtension.setName("Domain management service activator");
		domainManagementActivatorExtension.setDescription("Domain management service activator description");
		domainManagementActivatorExtension.adapt(ServiceActivator.class, DomainManagementServiceActivator.INSTANCE);
		addExtension(domainManagementActivatorExtension);

		ProgramExtension nodeManagementActivatorExtension = new ProgramExtension(typeId, NodeManagementServiceActivator.ID);
		nodeManagementActivatorExtension.setName("Node management service activator");
		nodeManagementActivatorExtension.setDescription("Node management service activator description");
		nodeManagementActivatorExtension.adapt(ServiceActivator.class, NodeManagementServiceActivator.INSTANCE);
		addExtension(nodeManagementActivatorExtension);

		// tier 4
		ProgramExtension missionControlActivatorExtension = new ProgramExtension(typeId, MissionControlServiceActivator.ID);
		missionControlActivatorExtension.setName("Mission control service activator");
		missionControlActivatorExtension.setDescription("Mission control service activator description");
		missionControlActivatorExtension.adapt(ServiceActivator.class, MissionControlServiceActivator.INSTANCE);
		addExtension(missionControlActivatorExtension);
	}

}
