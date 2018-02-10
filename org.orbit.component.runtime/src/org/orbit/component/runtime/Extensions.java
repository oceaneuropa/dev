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
import org.orbit.sdk.ServiceControl;
import org.orbit.sdk.extension.util.ProgramExtension;
import org.orbit.sdk.extension.util.ProgramExtensions;

public class Extensions extends ProgramExtensions {

	public static final String USER_REGISTRY_SERVICE_EXTENSION_ID = "component.user_registry.service";
	public static final String AUTH_SERVICEE_EXTENSION_ID = "component.auth.service";
	public static final String CONFIG_REGISTRY_SERVICE_EXTENSION_ID = "component.config_registry.service";
	public static final String APP_STORE_SERVICE_EXTENSION_ID = "component.app_store.service";
	public static final String DOMAIN_SERVICE_SERVICE_EXTENSION_ID = "component.domain_service.service";
	public static final String TRANSFER_AGENT_SERVICE_EXTENSION_ID = "component.transfer_agent.service";
	public static final String MISSION_CONTROL_SERVICE_EXTENSION_ID = "component.mission_control.service";

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
		add(userRegistryServiceExtension);

		ProgramExtension authServiceExtension = new ProgramExtension(extensionTypeId, AUTH_SERVICEE_EXTENSION_ID);
		authServiceExtension.setName("Auth service");
		authServiceExtension.setDescription("Auth service description");
		authServiceExtension.adapt(ServiceControl.class, AuthServiceControl.INSTANCE);
		add(authServiceExtension);

		ProgramExtension configRegistryServiceExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_SERVICE_EXTENSION_ID);
		configRegistryServiceExtension.setName("Config registration service");
		configRegistryServiceExtension.setDescription("Config registration service description");
		configRegistryServiceExtension.adapt(ServiceControl.class, ConfigRegistryServiceControl.INSTANCE);
		add(configRegistryServiceExtension);

		// tier 2
		ProgramExtension appStoreServiceExtension = new ProgramExtension(extensionTypeId, APP_STORE_SERVICE_EXTENSION_ID);
		appStoreServiceExtension.setName("App store service");
		appStoreServiceExtension.setDescription("App store service description");
		appStoreServiceExtension.adapt(ServiceControl.class, AppStoreServiceControl.INSTANCE);
		add(appStoreServiceExtension);

		// tier 3
		ProgramExtension domainServiceServiceExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_SERVICE_EXTENSION_ID);
		domainServiceServiceExtension.setName("Domain service");
		domainServiceServiceExtension.setDescription("Domain service description");
		domainServiceServiceExtension.adapt(ServiceControl.class, DomainServiceServiceControl.INSTANCE);
		add(domainServiceServiceExtension);

		ProgramExtension transferAgentServiceExtension = new ProgramExtension(extensionTypeId, TRANSFER_AGENT_SERVICE_EXTENSION_ID);
		transferAgentServiceExtension.setName("Transfer agent service");
		transferAgentServiceExtension.setDescription("Transfer agent service description");
		transferAgentServiceExtension.adapt(ServiceControl.class, TransferAgentServiceControl.INSTANCE);
		add(transferAgentServiceExtension);

		// tier 4
		ProgramExtension missionControlServiceExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_SERVICE_EXTENSION_ID);
		missionControlServiceExtension.setName("Mission control service");
		missionControlServiceExtension.setDescription("Mission control service description");
		missionControlServiceExtension.adapt(ServiceControl.class, MissionControlServiceControl.INSTANCE);
		add(missionControlServiceExtension);
	}

}
