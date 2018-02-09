package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.TransferAgentWSRelayControl;
import org.orbit.os.runtime.api.WSRelayControl;
import org.orbit.service.program.impl.ProgramExtensionImpl;

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
