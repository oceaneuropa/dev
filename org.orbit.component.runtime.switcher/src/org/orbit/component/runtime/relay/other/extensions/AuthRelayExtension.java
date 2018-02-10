package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.AuthWSRelayControl;
import org.orbit.sdk.WSRelayControl;
import org.orbit.sdk.extension.impl.ProgramExtensionImpl;

public class AuthRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.auth.relay";

	public AuthRelayExtension() {
		adapt(WSRelayControl.class, AuthWSRelayControl.INSTANCE);
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
