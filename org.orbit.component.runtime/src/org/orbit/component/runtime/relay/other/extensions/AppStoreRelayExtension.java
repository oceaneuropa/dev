package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.AppStoreRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

public class AppStoreRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.app_store.relay";

	public AppStoreRelayExtension() {
		addInterface(WSRelayControl.class, AppStoreRelayControl.INSTANCE);
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
