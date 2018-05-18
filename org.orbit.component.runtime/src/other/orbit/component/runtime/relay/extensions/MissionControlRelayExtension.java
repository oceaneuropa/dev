package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.spi.WSRelayControl;

import other.orbit.component.runtime.extensions.MissionControlRelayControl;

public class MissionControlRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.mission_control.relay";

	public MissionControlRelayExtension() {
		addInterface(WSRelayControl.class, MissionControlRelayControl.INSTANCE);
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
