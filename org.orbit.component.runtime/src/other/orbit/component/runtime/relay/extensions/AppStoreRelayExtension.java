package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.relaycontrol.WSRelayControl;

import other.orbit.component.runtime.extensions.AppStoreRelayControl;

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
