package org.orbit.infra.runtime.extensionregistry.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ExtensionRegistryWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryWSResource.class);

	@Inject
	public ExtensionRegistryService service;

	protected ExtensionRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ExtensionRegistryService is not available.");
		}
		return this.service;
	}

}