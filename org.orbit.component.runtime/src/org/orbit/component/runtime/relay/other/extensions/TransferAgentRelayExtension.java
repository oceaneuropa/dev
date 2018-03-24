package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.TransferAgentRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

public class TransferAgentRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.transfer_agent.relay";

	public TransferAgentRelayExtension() {
		addInterface(WSRelayControl.class, TransferAgentRelayControl.INSTANCE);
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
