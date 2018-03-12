package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.UserRegistryWSRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

public class UserRegistryRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.user_registry.relay";

	public UserRegistryRelayExtension() {
		addInterface(WSRelayControl.class, UserRegistryWSRelayControl.INSTANCE);
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
