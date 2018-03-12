package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.ConfigRegistryWSRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

public class ConfigRegistryRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.config_registry.relay";

	public ConfigRegistryRelayExtension() {
		addInterface(WSRelayControl.class, ConfigRegistryWSRelayControl.INSTANCE);
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
