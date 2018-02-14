package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.AuthWSRelayControl;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionImpl;
import org.orbit.platform.sdk.relay.WSRelayControl;

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
