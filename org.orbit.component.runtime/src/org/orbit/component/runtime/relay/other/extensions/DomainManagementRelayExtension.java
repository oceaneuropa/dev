package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.extensions.other.DomainServiceRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

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
