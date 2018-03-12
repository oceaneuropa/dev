package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.DomainServiceWSRelayControl;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.impl.other.ProgramExtensionImplV1;

public class DomainServiceRelayExtension extends ProgramExtensionImplV1 {

	public static final String ID = "orbit.component.domain_service.relay";

	public DomainServiceRelayExtension() {
		addInterface(WSRelayControl.class, DomainServiceWSRelayControl.INSTANCE);
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
