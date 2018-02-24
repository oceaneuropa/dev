package org.orbit.platform.runtime.platform.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.platform.runtime.platform.Platform;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Platform extension types resource.
 * 
 * {contextRoot} example:
 * /platform/v1/extension_types
 * 
 * Extensions
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extension_types
 * 
 * @see DomainServiceWSMachinesResource
 * @see TransferAgentServiceResource
 */
@Path("/extension_types")
@Produces(MediaType.APPLICATION_JSON)
public class PlatformWSExtensionTypesResource extends AbstractWSApplicationResource {

	@Inject
	public Platform service;

	public Platform getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("Platform is not available.");
		}
		return this.service;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getExtensionTypes() {
		List<String> extensionTypeIds = new ArrayList<String>();

		Platform platform = getService();
		IProgramExtensionService service = platform.getProgramExtensionService();

		String[] extensionTypeIdArrays = service.getExtensionTypeIds();
		for (String extensionTypeId : extensionTypeIdArrays) {
			extensionTypeIds.add(extensionTypeId);
		}

		return Response.ok().entity(extensionTypeIds).build();
	}

}
