package org.orbit.component.runtime;

import org.orbit.component.runtime.extensions.AppStoreServiceControl;
import org.orbit.component.runtime.extensions.AuthServiceControl;
import org.orbit.component.runtime.extensions.ConfigRegistryServiceControl;
import org.orbit.component.runtime.extensions.DomainServiceServiceControl;
import org.orbit.component.runtime.extensions.MissionControlServiceControl;
import org.orbit.component.runtime.extensions.TransferAgentServiceControl;
import org.orbit.component.runtime.extensions.UserRegistryServiceControl;
import org.orbit.os.runtime.api.ServiceControl;
import org.orbit.service.program.util.ProgramExtension;
import org.orbit.service.program.util.ProgramExtensions;

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
		createServiceExtensions();
	}

	protected void createServiceExtensions() {
		String extensionTypeId = ServiceControl.EXTENSION_TYPE_ID;

		// tier 1
		ProgramExtension userRegistryServiceExtension = new ProgramExtension(extensionTypeId, USER_REGISTRY_SERVICE_EXTENSION_ID);
		userRegistryServiceExtension.adapt(ServiceControl.class, UserRegistryServiceControl.INSTANCE);
		add(userRegistryServiceExtension);

		ProgramExtension authServiceExtension = new ProgramExtension(extensionTypeId, AUTH_SERVICEE_EXTENSION_ID);
		authServiceExtension.adapt(ServiceControl.class, AuthServiceControl.INSTANCE);
		add(authServiceExtension);

		ProgramExtension configRegistryServiceExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_SERVICE_EXTENSION_ID);
		configRegistryServiceExtension.adapt(ServiceControl.class, ConfigRegistryServiceControl.INSTANCE);
		add(configRegistryServiceExtension);

		// tier 2
		ProgramExtension appStoreServiceExtension = new ProgramExtension(extensionTypeId, APP_STORE_SERVICE_EXTENSION_ID);
		appStoreServiceExtension.adapt(ServiceControl.class, AppStoreServiceControl.INSTANCE);
		add(appStoreServiceExtension);

		// tier 3
		ProgramExtension domainServiceServiceExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_SERVICE_EXTENSION_ID);
		domainServiceServiceExtension.adapt(ServiceControl.class, DomainServiceServiceControl.INSTANCE);
		add(domainServiceServiceExtension);

		ProgramExtension transferAgentServiceExtension = new ProgramExtension(extensionTypeId, TRANSFER_AGENT_SERVICE_EXTENSION_ID);
		transferAgentServiceExtension.adapt(ServiceControl.class, TransferAgentServiceControl.INSTANCE);
		add(transferAgentServiceExtension);

		// tier 4
		ProgramExtension missionControlServiceExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_SERVICE_EXTENSION_ID);
		missionControlServiceExtension.adapt(ServiceControl.class, MissionControlServiceControl.INSTANCE);
		add(missionControlServiceExtension);
	}

}
