package org.orbit.infra.runtime;

import org.orbit.infra.runtime.extensions.IndexServiceActivator;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.util.ProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensions;

public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	@Override
	public void createExtensions() {
		createServiceActivatorExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String typeId = ServiceActivator.TYPE_ID;

		ProgramExtension indexServiceActivatorExtension = new ProgramExtension(typeId, IndexServiceActivator.ID);
		indexServiceActivatorExtension.setName("Index Service activator");
		indexServiceActivatorExtension.setDescription("Index Service activator description");
		indexServiceActivatorExtension.adapt(ServiceActivator.class, IndexServiceActivator.INSTANCE);
		addExtension(indexServiceActivatorExtension);
	}

}
