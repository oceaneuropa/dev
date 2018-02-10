package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.ConfigRegistryWSRelayControl;
import org.orbit.sdk.WSRelayControl;
import org.orbit.sdk.extension.impl.ProgramExtensionImpl;

public class ConfigRegistryRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.config_registry.relay";

	public ConfigRegistryRelayExtension() {
		adapt(WSRelayControl.class, ConfigRegistryWSRelayControl.INSTANCE);
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
