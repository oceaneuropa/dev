package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.TransferAgentRelayControl;
import org.orbit.platform.sdk.extensions.WSRelayControl;

public class NodeControlRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.node_control.relay";

	public NodeControlRelayExtension() {
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
