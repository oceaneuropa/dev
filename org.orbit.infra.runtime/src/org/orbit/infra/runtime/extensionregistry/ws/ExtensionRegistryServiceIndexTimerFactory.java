package org.orbit.infra.runtime.extensionregistry.ws;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;

public class ExtensionRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ExtensionRegistryService> {

	@Override
	public ExtensionRegistryServiceIndexTimer create(IndexProvider indexProvider, ExtensionRegistryService service) {
		return new ExtensionRegistryServiceIndexTimer(indexProvider, service);
	}

}
