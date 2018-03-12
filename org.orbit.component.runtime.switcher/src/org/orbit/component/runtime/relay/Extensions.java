/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.component.runtime.relay;

import org.orbit.component.runtime.relay.extensions.AppStoreWSRelayControl;
import org.orbit.component.runtime.relay.extensions.AuthWSRelayControl;
import org.orbit.component.runtime.relay.extensions.ConfigRegistryWSRelayControl;
import org.orbit.component.runtime.relay.extensions.DomainServiceWSRelayControl;
import org.orbit.component.runtime.relay.extensions.MissionControlWSRelayControl;
import org.orbit.component.runtime.relay.extensions.NodeControlWSRelayControl;
import org.orbit.component.runtime.relay.extensions.UserRegistryWSRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.desc.ProgramExtension;
import org.orbit.platform.sdk.extension.desc.ProgramExtensions;

/**
 * Component relay extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	// extension id constants for WS relay
	public static final String USER_REGISTRY_RELAY_EXTENSION_ID = "component.user_registry.relay";
	public static final String AUTH_RELAY_EXTENSION_ID = "component.auth.relay";
	public static final String CONFIG_REGISTRY_RELAY_EXTENSION_ID = "component.config_registry.relay";
	public static final String APP_STORE_RELAY_EXTENSION_ID = "component.app_store.relay";
	public static final String DOMAIN_SERVICE_RELAY_EXTENSION_ID = "component.domain_service.relay";
	public static final String NODE_CONTROL_RELAY_EXTENSION_ID = "component.node_control.relay";
	public static final String MISSION_CONTROL_RELAY_EXTENSION_ID = "component.mission_control.relay";

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
		createWSRelayExtensions();
	}

	protected void createWSRelayExtensions() {
		String extensionTypeId = WSRelayControl.EXTENSION_TYPE_ID;

		// tier 1
		ProgramExtension userRegistryRelayExtension = new ProgramExtension(extensionTypeId, USER_REGISTRY_RELAY_EXTENSION_ID);
		userRegistryRelayExtension.setName("User registration relay");
		userRegistryRelayExtension.setDescription("User registration relay description");
		userRegistryRelayExtension.addInterface(WSRelayControl.class, UserRegistryWSRelayControl.INSTANCE);
		addExtension(userRegistryRelayExtension);

		ProgramExtension authRelayExtension = new ProgramExtension(extensionTypeId, AUTH_RELAY_EXTENSION_ID);
		authRelayExtension.setName("Auth relay");
		authRelayExtension.setDescription("Auth relay description");
		authRelayExtension.addInterface(WSRelayControl.class, AuthWSRelayControl.INSTANCE);
		addExtension(authRelayExtension);

		ProgramExtension configRegistryRelayExtension = new ProgramExtension(extensionTypeId, CONFIG_REGISTRY_RELAY_EXTENSION_ID);
		configRegistryRelayExtension.setName("Config registration relay");
		configRegistryRelayExtension.setDescription("Config registration relay description");
		configRegistryRelayExtension.addInterface(WSRelayControl.class, ConfigRegistryWSRelayControl.INSTANCE);
		addExtension(configRegistryRelayExtension);

		// tier 2
		ProgramExtension appStoreRelayExtension = new ProgramExtension(extensionTypeId, APP_STORE_RELAY_EXTENSION_ID);
		appStoreRelayExtension.setName("App store relay");
		appStoreRelayExtension.setDescription("App store relay description");
		appStoreRelayExtension.addInterface(WSRelayControl.class, AppStoreWSRelayControl.INSTANCE);
		addExtension(appStoreRelayExtension);

		// tier 3
		ProgramExtension domainServiceRelayExtension = new ProgramExtension(extensionTypeId, DOMAIN_SERVICE_RELAY_EXTENSION_ID);
		domainServiceRelayExtension.setName("Domain service relay");
		domainServiceRelayExtension.setDescription("Domain service relay description");
		domainServiceRelayExtension.addInterface(WSRelayControl.class, DomainServiceWSRelayControl.INSTANCE);
		addExtension(domainServiceRelayExtension);

		ProgramExtension nodeControlRelayExtension = new ProgramExtension(extensionTypeId, NODE_CONTROL_RELAY_EXTENSION_ID);
		nodeControlRelayExtension.setName("Node control relay");
		nodeControlRelayExtension.setDescription("Node control relay description");
		nodeControlRelayExtension.addInterface(WSRelayControl.class, NodeControlWSRelayControl.INSTANCE);
		addExtension(nodeControlRelayExtension);

		// tier 4
		ProgramExtension missionControlRelayExtension = new ProgramExtension(extensionTypeId, MISSION_CONTROL_RELAY_EXTENSION_ID);
		missionControlRelayExtension.setName("Mission control relay");
		missionControlRelayExtension.setDescription("Mission control relay description");
		missionControlRelayExtension.addInterface(WSRelayControl.class, MissionControlWSRelayControl.INSTANCE);
		addExtension(missionControlRelayExtension);
	}

}
