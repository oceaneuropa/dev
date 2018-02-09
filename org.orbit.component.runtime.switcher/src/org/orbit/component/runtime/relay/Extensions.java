package org.orbit.component.runtime.relay;

import org.orbit.component.runtime.relay.extensions.AppStoreWSRelayControl;
import org.orbit.component.runtime.relay.extensions.AuthWSRelayControl;
import org.orbit.component.runtime.relay.extensions.ConfigRegistryWSRelayControl;
import org.orbit.component.runtime.relay.extensions.DomainServiceWSRelayControl;
import org.orbit.component.runtime.relay.extensions.MissionControlWSRelayControl;
import org.orbit.component.runtime.relay.extensions.TransferAgentWSRelayControl;
import org.orbit.component.runtime.relay.extensions.UserRegistryWSRelayControl;
import org.orbit.os.runtime.api.WSRelayControl;
import org.orbit.service.program.util.ProgramExtension;
import org.orbit.service.program.util.ProgramExtensions;

public class Extensions extends ProgramExtensions {

	// ws relay extension id constants
	public static final String USER_REGISTRY_RELAY_EXTENSION_ID = "component.user_registry.relay";
	public static final String AUTH_RELAY_EXTENSION_ID = "component.auth.relay";
	public static final String CONFIG_REGISTRY_RELAY_EXTENSION_ID = "component.config_registry.relay";
	public static final String APP_STORE_RELAY_EXTENSION_ID = "component.app_store.relay";
	public static final String DOMAIN_SERVICE_RELAY_EXTENSION_ID = "component.domain_service.relay";
	public static final String TRANSFER_AGENT_RELAY_EXTENSION_ID = "component.transfer_agent.relay";
	public static final String MISSION_CONTROL_RELAY_EXTENSION_ID = "component.mission_control.relay";

	@Override
	public void createExtensions() {
		createWSRelayExtensions();
	}

	protected void createWSRelayExtensions() {
		String extensionTypeId = WSRelayControl.EXTENSION_TYPE_ID;

		// tier 1
		ProgramExtension userRegistryRelayExtension = new ProgramExtension(extensionTypeId, USER_REGISTRY_RELAY_EXTENSION_ID);
		userRegistryRelayExtension.adapt(WSRelayControl.class, UserRegistryWSRelayControl.INSTANCE);
		add(userRegistryRelayExtension);

		ProgramExtension authRelayExtension = new ProgramExtension(extensionTypeId, AUTH_RELAY_EXTENSION_ID);
		authRelayExtension.adapt(WSRelayControl.class, AuthWSRelayControl.INSTANCE);
		add(authRelayExtension);

		ProgramExtension configRegistryRelayExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_RELAY_EXTENSION_ID);
		configRegistryRelayExtension.adapt(WSRelayControl.class, ConfigRegistryWSRelayControl.INSTANCE);
		add(configRegistryRelayExtension);

		// tier 2
		ProgramExtension appStoreRelayExtension = new ProgramExtension(extensionTypeId, APP_STORE_RELAY_EXTENSION_ID);
		appStoreRelayExtension.adapt(WSRelayControl.class, AppStoreWSRelayControl.INSTANCE);
		add(appStoreRelayExtension);

		// tier 3
		ProgramExtension domainServiceRelayExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_RELAY_EXTENSION_ID);
		domainServiceRelayExtension.adapt(WSRelayControl.class, DomainServiceWSRelayControl.INSTANCE);
		add(domainServiceRelayExtension);

		ProgramExtension transferAgentRelayExtension = new ProgramExtension(extensionTypeId, TRANSFER_AGENT_RELAY_EXTENSION_ID);
		transferAgentRelayExtension.adapt(WSRelayControl.class, TransferAgentWSRelayControl.INSTANCE);
		add(transferAgentRelayExtension);

		// tier 4
		ProgramExtension missionControlRelayExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_RELAY_EXTENSION_ID);
		missionControlRelayExtension.adapt(WSRelayControl.class, MissionControlWSRelayControl.INSTANCE);
		add(missionControlRelayExtension);
	}

}
