package org.orbit.infra.runtime.extensionregistry.ws;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;

public class ExtensionRegistryServiceIndexTimerFactory implements ServiceIndexTimerFactory<ExtensionRegistryService> {

	@Override
	public ExtensionRegistryServiceIndexTimer create(IndexServiceClient indexProvider, ExtensionRegistryService service) {
		return new ExtensionRegistryServiceIndexTimer(indexProvider, service);
	}

}
