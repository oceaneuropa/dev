package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.MissionControlRelayControl;
import org.orbit.platform.sdk.extensions.WSRelayControl;

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
