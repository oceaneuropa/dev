package org.orbit.platform.runtime.platform.ws;

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

import org.orbit.platform.model.platform.dto.ServiceExtensionInfoDTO;
import org.orbit.platform.runtime.platform.Platform;
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
		List<ServiceExtensionInfoDTO> extensionDTOs = new ArrayList<ServiceExtensionInfoDTO>();

		Platform platform = getService();
		IProgramExtensionService service = platform.getProgramExtensionService();

		if (extensionTypeId == null || extensionTypeId.isEmpty()) {
			// extensionTypeId is not specified
			// no matter extensionId is specified or not
			// - get all extensions from all extension types
			String[] extensionTypeIds = service.getExtensionTypeIds();
			if (extensionTypeIds != null) {
				for (String currExtensionTypeId : extensionTypeIds) {
					IProgramExtension[] extensions = platform.getProgramExtensionService().getExtensions(currExtensionTypeId);
					if (extensions != null) {
						for (IProgramExtension extension : extensions) {
							ServiceExtensionInfoDTO extensionDTO = ModelConverter.getInstance().toDTO(extension);
							extensionDTOs.add(extensionDTO);
						}
					}
				}
			}

		} else {
			if (extensionId == null || extensionId.isEmpty()) {
				// extensionTypeId is specified
				// extensionId is not specified
				// - get extensions with specified extension type id
				IProgramExtension[] extensions = service.getExtensions(extensionTypeId);
				if (extensions != null) {
					for (IProgramExtension extension : extensions) {
						ServiceExtensionInfoDTO extensionDTO = ModelConverter.getInstance().toDTO(extension);
						extensionDTOs.add(extensionDTO);
					}
				}

			} else {
				// extensionTypeId is specified
				// extensionId is specified
				// - get the one extension with specified extension type id and extension id
				IProgramExtension extension = service.getExtension(extensionTypeId, extensionId);
				if (extension != null) {
					ServiceExtensionInfoDTO extensionDTO = ModelConverter.getInstance().toDTO(extension);
					extensionDTOs.add(extensionDTO);
				}
			}
		}

		return Response.ok().entity(extensionDTOs).build();
	}

}
