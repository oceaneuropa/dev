package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.NodeControlWSRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.ProgramExtensionImpl;

public class TransferAgentRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.transfer_agent.relay";

	public TransferAgentRelayExtension() {
		adapt(WSRelayControl.class, NodeControlWSRelayControl.INSTANCE);
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
