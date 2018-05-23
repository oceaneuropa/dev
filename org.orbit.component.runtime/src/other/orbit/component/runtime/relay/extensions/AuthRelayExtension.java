package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.relaycontrol.WSRelayControl;

import other.orbit.component.runtime.extensions.AuthRelayControl;

public class AuthRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.auth.relay";

	public AuthRelayExtension() {
		addInterface(WSRelayControl.class, AuthRelayControl.INSTANCE);
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
