package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.UserRegistryRelayControl;
import org.orbit.platform.sdk.extensions.WSRelayControl;

public class UserRegistryRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.user_registry.relay";

	public UserRegistryRelayExtension() {
		addInterface(WSRelayControl.class, UserRegistryRelayControl.INSTANCE);
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
