package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.AppStoreWSRelayControl;
import org.orbit.os.runtime.api.WSRelayControl;
import org.orbit.service.program.impl.ProgramExtensionImpl;

public class AppStoreRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.app_store.relay";

	public AppStoreRelayExtension() {
		adapt(WSRelayControl.class, AppStoreWSRelayControl.INSTANCE);
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
