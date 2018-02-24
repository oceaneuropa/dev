package org.orbit.platform.runtime.platform.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.platform.runtime.platform.Platform;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Platform extension services resource.
 * 
 * {contextRoot} example:
 * /platform/v1/extensions
 * 
 * Extensions
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions?extensionTypeId={extensionTypeId}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/extensions?extensionTypeId={extensionTypeId}&extensionId={extensionId}
 * 
 * @see DomainServiceWSMachinesResource
 * @see TransferAgentServiceResource
 */
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
public class PlatformWSServicesResource extends AbstractWSApplicationResource {

	@Inject
	public Platform service;

	public Platform getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("Platform is not available.");
		}
		return this.service;
	}

}
