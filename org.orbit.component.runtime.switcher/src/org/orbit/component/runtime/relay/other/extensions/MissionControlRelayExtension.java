package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.MissionControlWSRelayControl;
import org.orbit.os.runtime.api.WSRelayControl;
import org.orbit.service.program.impl.ProgramExtensionImpl;

public class MissionControlRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.mission_control.relay";

	public MissionControlRelayExtension() {
		adapt(WSRelayControl.class, MissionControlWSRelayControl.INSTANCE);
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
