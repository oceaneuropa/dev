/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime;

import org.orbit.component.runtime.extensions.servicecontrol.AppStoreServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.AuthServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.ConfigRegistryServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.DomainServiceServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.MissionControlServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.TransferAgentServiceControl;
import org.orbit.component.runtime.extensions.servicecontrol.UserRegistryServiceControl;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensions;
import org.orbit.platform.sdk.servicecontrol.ServiceControl;

public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	// extension id constants for services
	public static final String USER_REGISTRY_SERVICE_EXTENSION_ID = "component.user_registry.service";
	public static final String AUTH_SERVICEE_EXTENSION_ID = "component.auth.service";
	public static final String CONFIG_REGISTRY_SERVICE_EXTENSION_ID = "component.config_registry.service";
	public static final String APP_STORE_SERVICE_EXTENSION_ID = "component.app_store.service";
	public static final String DOMAIN_SERVICE_SERVICE_EXTENSION_ID = "component.domain_service.service";
	public static final String TRANSFER_AGENT_SERVICE_EXTENSION_ID = "component.transfer_agent.service";
	public static final String MISSION_CONTROL_SERVICE_EXTENSION_ID = "component.mission_control.service";

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
		createServiceControlExtensions();
	}

	protected void createServiceCommandExtensions() {
		// String extensionTypeId = ServiceCommand.EXTENSION_TYPE_ID;
	}

	protected void createServiceControlExtensions() {
		String extensionTypeId = ServiceControl.EXTENSION_TYPE_ID;

		// tier 1
		ProgramExtension userRegistryServiceExtension = new ProgramExtension(extensionTypeId, USER_REGISTRY_SERVICE_EXTENSION_ID);
		userRegistryServiceExtension.setName("User registration service");
		userRegistryServiceExtension.setDescription("User registration service description");
		userRegistryServiceExtension.adapt(ServiceControl.class, UserRegistryServiceControl.INSTANCE);
		addExtension(userRegistryServiceExtension);

		ProgramExtension authServiceExtension = new ProgramExtension(extensionTypeId, AUTH_SERVICEE_EXTENSION_ID);
		authServiceExtension.setName("Auth service");
		authServiceExtension.setDescription("Auth service description");
		authServiceExtension.adapt(ServiceControl.class, AuthServiceControl.INSTANCE);
		addExtension(authServiceExtension);

		ProgramExtension configRegistryServiceExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_SERVICE_EXTENSION_ID);
		configRegistryServiceExtension.setName("Config registration service");
		configRegistryServiceExtension.setDescription("Config registration service description");
		configRegistryServiceExtension.adapt(ServiceControl.class, ConfigRegistryServiceControl.INSTANCE);
		addExtension(configRegistryServiceExtension);

		// tier 2
		ProgramExtension appStoreServiceExtension = new ProgramExtension(extensionTypeId, APP_STORE_SERVICE_EXTENSION_ID);
		appStoreServiceExtension.setName("App store service");
		appStoreServiceExtension.setDescription("App store service description");
		appStoreServiceExtension.adapt(ServiceControl.class, AppStoreServiceControl.INSTANCE);
		addExtension(appStoreServiceExtension);

		// tier 3
		ProgramExtension domainServiceServiceExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_SERVICE_EXTENSION_ID);
		domainServiceServiceExtension.setName("Domain service");
		domainServiceServiceExtension.setDescription("Domain service description");
		domainServiceServiceExtension.adapt(ServiceControl.class, DomainServiceServiceControl.INSTANCE);
		addExtension(domainServiceServiceExtension);

		ProgramExtension transferAgentServiceExtension = new ProgramExtension(extensionTypeId, TRANSFER_AGENT_SERVICE_EXTENSION_ID);
		transferAgentServiceExtension.setName("Transfer agent service");
		transferAgentServiceExtension.setDescription("Transfer agent service description");
		transferAgentServiceExtension.adapt(ServiceControl.class, TransferAgentServiceControl.INSTANCE);
		addExtension(transferAgentServiceExtension);

		// tier 4
		ProgramExtension missionControlServiceExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_SERVICE_EXTENSION_ID);
		missionControlServiceExtension.setName("Mission control service");
		missionControlServiceExtension.setDescription("Mission control service description");
		missionControlServiceExtension.adapt(ServiceControl.class, MissionControlServiceControl.INSTANCE);
		addExtension(missionControlServiceExtension);
	}

}
