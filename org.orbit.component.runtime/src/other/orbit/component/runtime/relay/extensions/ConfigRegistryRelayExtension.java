package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.spi.WSRelayControl;

import other.orbit.component.runtime.extensions.ConfigRegistryRelayControl;

public class ConfigRegistryRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.config_registry.relay";

	public ConfigRegistryRelayExtension() {
		addInterface(WSRelayControl.class, ConfigRegistryRelayControl.INSTANCE);
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
