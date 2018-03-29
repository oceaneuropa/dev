package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.AuthRelayControl;
import org.orbit.platform.sdk.extensions.WSRelayControl;

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
