package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.TransferAgentWSRelayControl;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionImpl;
import org.orbit.platform.sdk.relay.WSRelayControl;

public class TransferAgentRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.transfer_agent.relay";

	public TransferAgentRelayExtension() {
		adapt(WSRelayControl.class, TransferAgentWSRelayControl.INSTANCE);
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
