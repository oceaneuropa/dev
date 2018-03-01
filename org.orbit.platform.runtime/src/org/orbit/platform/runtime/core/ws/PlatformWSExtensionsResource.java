package org.orbit.platform.runtime.core.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.platform.model.dto.ExtensionDTO;
import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Platform extensions resource.
 * 
 * {contextRoot} example:
 * /platform/v1/extensions
 * 
 * Extensions
 * 
 * - get all extensions from all extension types
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions
 * 
 * - get extensions with specified extension type id
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions?extensionTypeId={extensionTypeId}
 * 
 * - get the one extension with specified extension type id and extension id
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions?extensionTypeId={extensionTypeId}&extensionId={extensionId}
 * 
 * @see DomainServiceWSMachinesResource
 * @see TransferAgentServiceResource
 */
@Path("/extensions")
@Produces(MediaType.APPLICATION_JSON)
public class PlatformWSExtensionsResource extends AbstractWSApplicationResource {

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
	public Response get(@QueryParam("extensionTypeId") String extensionTypeId, @QueryParam("extensionId") String extensionId) {
		List<ExtensionDTO> extensionDTOs = new ArrayList<ExtensionDTO>();

		Platform platform = getService();
		IProgramExtensionService service = platform.getProgramExtensionService();

		IProgramExtension[] extensions = null;
		if ((extensionTypeId == null || extensionTypeId.isEmpty()) && (extensionId == null || extensionId.isEmpty())) {
			extensions = platform.getProgramExtensionService().getExtensions();

		} else if (extensionId == null || extensionId.isEmpty()) {
			extensions = service.getExtensions(extensionTypeId);

		} else {
			IProgramExtension extension = service.getExtension(extensionTypeId, extensionId);
			if (extension != null) {
				extensions = new IProgramExtension[] { extension };
			}
		}

		if (extensions != null) {
			for (IProgramExtension extension : extensions) {
				ExtensionDTO extensionDTO = ModelConverter.getInstance().toDTO(extension);
				extensionDTOs.add(extensionDTO);
			}
		}

		return Response.ok().entity(extensionDTOs).build();
	}

}
