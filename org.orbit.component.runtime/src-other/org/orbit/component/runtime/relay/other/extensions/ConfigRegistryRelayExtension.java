package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.ConfigRegistryRelayControl;
import org.orbit.platform.sdk.extensions.WSRelayControl;

public class ConfigRegistryRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.config_registry.relay";

	public ConfigRegistryRelayExtension() {
		addInterface(WSRelayControl.class, ConfigRegistryRelayControl.INSTANCE);
	}

	@Override
	public String getTypeId() {
		return WSRelayControl.EXTENSION_TYPE_ID;
	}

	@Override
	public String getId() {
		return ID;
	}

}
