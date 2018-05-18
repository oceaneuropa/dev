package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.spi.WSRelayControl;

import other.orbit.component.runtime.extensions.TransferAgentRelayControl;

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
