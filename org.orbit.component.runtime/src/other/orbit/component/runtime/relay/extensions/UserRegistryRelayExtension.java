package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.spi.WSRelayControl;

import other.orbit.component.runtime.extensions.UserRegistryRelayControl;

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
