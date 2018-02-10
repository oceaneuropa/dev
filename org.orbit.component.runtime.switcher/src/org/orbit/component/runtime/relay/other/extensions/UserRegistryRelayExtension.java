package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.UserRegistryWSRelayControl;
import org.orbit.sdk.WSRelayControl;
import org.orbit.sdk.extension.impl.ProgramExtensionImpl;

public class UserRegistryRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.user_registry.relay";

	public UserRegistryRelayExtension() {
		adapt(WSRelayControl.class, UserRegistryWSRelayControl.INSTANCE);
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
