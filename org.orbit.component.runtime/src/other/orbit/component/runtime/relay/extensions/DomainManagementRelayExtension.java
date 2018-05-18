package other.orbit.component.runtime.relay.extensions;

import org.orbit.platform.sdk.spi.WSRelayControl;

import other.orbit.component.runtime.extensions.DomainServiceRelayControl;

public class DomainManagementRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.domain_management.relay";

	public DomainManagementRelayExtension() {
		addInterface(WSRelayControl.class, DomainServiceRelayControl.INSTANCE);
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
