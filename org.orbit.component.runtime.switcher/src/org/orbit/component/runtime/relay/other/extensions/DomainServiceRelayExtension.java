package org.orbit.component.runtime.relay.other.extensions;

import org.orbit.component.runtime.relay.extensions.DomainServiceWSRelayControl;
import org.orbit.os.runtime.api.WSRelayControl;
import org.orbit.service.program.impl.ProgramExtensionImpl;

public class DomainServiceRelayExtension extends ProgramExtensionImpl {

	public static final String ID = "orbit.component.domain_service.relay";

	public DomainServiceRelayExtension() {
		adapt(WSRelayControl.class, DomainServiceWSRelayControl.INSTANCE);
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
